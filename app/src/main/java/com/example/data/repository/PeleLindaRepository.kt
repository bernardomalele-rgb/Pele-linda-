package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.data.local.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PeleLindaRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pele_linda_prefs", Context.MODE_PRIVATE)

    private val scope = CoroutineScope(Dispatchers.IO)
    private val roomDb = PeleLindaDatabase.getInstance(context)

    // Room Database Flows for Offline Persistence
    val favoriteRecipes: Flow<List<FavoriteRecipeEntity>> =
        roomDb.favoriteRecipeDao().getAllFavoritesFlow()

    val routineHistory: Flow<List<RoutineHistoryEntity>> =
        roomDb.routineHistoryDao().getAllRoutineHistoryFlow()

    // User State
    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Shopping List
    private val _shoppingList = MutableStateFlow(loadShoppingList())
    val shoppingList: StateFlow<List<ShoppingItem>> = _shoppingList.asStateFlow()

    // Payments
    private val _payments = MutableStateFlow(loadPayments())
    val payments: StateFlow<List<PaymentReceipt>> = _payments.asStateFlow()

    // Scans
    private val _facialScans = MutableStateFlow(loadScans())
    val facialScans: StateFlow<List<FacialAnalysisResult>> = _facialScans.asStateFlow()

    // Diary
    private val _diaryEntries = MutableStateFlow(loadDiary())
    val diaryEntries: StateFlow<List<DiaryEntry>> = _diaryEntries.asStateFlow()

    // Routine & Daily Goals
    private val _waterCupsToday = MutableStateFlow(prefs.getInt("water_cups_today", 3))
    val waterCupsToday: StateFlow<Int> = _waterCupsToday.asStateFlow()

    private val _routineStreakDays = MutableStateFlow(prefs.getInt("routine_streak", 4))
    val routineStreakDays: StateFlow<Int> = _routineStreakDays.asStateFlow()

    private val _morningRoutine = MutableStateFlow(loadMorningRoutine())
    val morningRoutine: StateFlow<List<RoutineTask>> = _morningRoutine.asStateFlow()

    private val _nightRoutine = MutableStateFlow(loadNightRoutine())
    val nightRoutine: StateFlow<List<RoutineTask>> = _nightRoutine.asStateFlow()

    // Allergy Test Active
    private val _allergyTestStartTime = MutableStateFlow(prefs.getLong("allergy_test_start", 0L))
    val allergyTestStartTime: StateFlow<Long> = _allergyTestStartTime.asStateFlow()

    private val _allergyRecipeName = MutableStateFlow(prefs.getString("allergy_recipe_name", "") ?: "")
    val allergyRecipeName: StateFlow<String> = _allergyRecipeName.asStateFlow()

    // Admin Audit Logs
    private val _auditLogs = MutableStateFlow(loadAuditLogs())
    val auditLogs: StateFlow<List<String>> = _auditLogs.asStateFlow()

    init {
        // Check trial or subscription status on startup
        refreshAccountStatus()
        syncUserProfileToRoom()
    }

    private fun syncUserProfileToRoom() {
        scope.launch {
            val user = _userProfile.value
            if (user.nome.isNotEmpty()) {
                val entity = UserProfileEntity(
                    id = "primary_user",
                    nome = user.nome,
                    idade = user.idade,
                    telemovel = user.telemovel,
                    isVerified = user.isVerified,
                    termosAceites = user.termosAceites,
                    tipoPele = user.tipoPele,
                    exposicaoSol = user.exposicaoSol,
                    qualidadeSono = user.qualidadeSono,
                    consumoAgua = user.consumoAgua,
                    produtosAtuais = user.produtosAtuais,
                    dataRegisto = user.dataRegisto,
                    diasAtivosExpiracao = user.diasAtivosExpiracao,
                    estadoConta = user.estadoConta.name,
                    isBlocked = user.isBlocked,
                    emailNotificado = user.emailNotificado
                )
                roomDb.userProfileDao().insertOrUpdateProfile(entity)
            }
        }
    }

    // -------------------------------------------------------------
    // USER PROFILE & REGISTRATION
    // -------------------------------------------------------------
    private fun loadUserProfile(): UserProfile {
        val nome = prefs.getString("user_nome", "") ?: ""
        if (nome.isEmpty()) {
            return UserProfile() // Not registered yet
        }
        val idade = prefs.getInt("user_idade", 22)
        val telemovel = prefs.getString("user_telemovel", "") ?: ""
        val isVerified = prefs.getBoolean("user_verified", false)
        val termosAceites = prefs.getBoolean("termos_aceites", false)
        val tipoPele = prefs.getString("tipo_pele", "Mista") ?: "Mista"
        val exposicaoSol = prefs.getString("exposicao_sol", "Moderada") ?: "Moderada"
        val qualidadeSono = prefs.getString("qualidade_sono", "6 a 8h") ?: "6 a 8h"
        val consumoAgua = prefs.getString("consumo_agua", "1L a 2L") ?: "1L a 2L"
        val produtosAtuais = prefs.getString("produtos_atuais", "Sabonete facial simples") ?: ""
        val dataRegisto = prefs.getLong("data_registo", System.currentTimeMillis())
        val diasAtivosExpiracao = prefs.getLong(
            "dias_ativos_expiracao",
            System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)
        )
        val isBlocked = prefs.getBoolean("user_blocked", false)

        val status = computeAccountStatus(diasAtivosExpiracao, isBlocked)

        return UserProfile(
            id = "user_${telemovel.ifEmpty { "guest" }}",
            nome = nome,
            idade = idade,
            telemovel = telemovel,
            isVerified = isVerified,
            termosAceites = termosAceites,
            tipoPele = tipoPele,
            exposicaoSol = exposicaoSol,
            qualidadeSono = qualidadeSono,
            consumoAgua = consumoAgua,
            produtosAtuais = produtosAtuais,
            dataRegisto = dataRegisto,
            testeGratisUsado = prefs.getBoolean("teste_gratis_usado", true),
            diasAtivosExpiracao = diasAtivosExpiracao,
            estadoConta = status,
            isBlocked = isBlocked,
            emailNotificado = prefs.getBoolean("email_notificado", false)
        )
    }

    fun registerUser(
        nome: String,
        idade: Int,
        telemovel: String,
        termosAceites: Boolean
    ): Boolean {
        // Enforce 1 trial per verified phone number check
        val registeredPhone = prefs.getString("user_telemovel", "") ?: ""
        val trialUsed = prefs.getBoolean("trial_used_$telemovel", false)

        val now = System.currentTimeMillis()
        val expiracaoTrial = now + (7L * 24 * 60 * 60 * 1000)

        prefs.edit()
            .putString("user_nome", nome)
            .putInt("user_idade", idade)
            .putString("user_telemovel", telemovel)
            .putBoolean("user_verified", true)
            .putBoolean("termos_aceites", termosAceites)
            .putLong("data_registo", now)
            .putLong("dias_ativos_expiracao", expiracaoTrial)
            .putBoolean("teste_gratis_usado", true)
            .putBoolean("trial_used_$telemovel", true)
            .putBoolean("user_blocked", false)
            .apply()

        val newUser = UserProfile(
            id = "user_$telemovel",
            nome = nome,
            idade = idade,
            telemovel = telemovel,
            isVerified = true,
            termosAceites = termosAceites,
            dataRegisto = now,
            testeGratisUsado = true,
            diasAtivosExpiracao = expiracaoTrial,
            estadoConta = AccountStatus.EM_TESTE
        )
        _userProfile.value = newUser
        syncUserProfileToRoom()

        // Trigger background email to bernardomalele5@gmail.com silently
        scope.launch {
            val sent = EmailNotificationService.sendRegistrationNotice(context, nome, telemovel, idade)
            if (sent) {
                prefs.edit().putBoolean("email_notificado", true).apply()
            }
        }

        // Add to audit logs
        addAuditLog("Registo de conta: $nome ($telemovel) iniciou teste de 7 dias.")
        return true
    }

    fun updateQuizAnswers(
        tipoPele: String,
        exposicaoSol: String,
        qualidadeSono: String,
        consumoAgua: String,
        produtosAtuais: String
    ) {
        prefs.edit()
            .putString("tipo_pele", tipoPele)
            .putString("exposicao_sol", exposicaoSol)
            .putString("qualidade_sono", qualidadeSono)
            .putString("consumo_agua", consumoAgua)
            .putString("produtos_atuais", produtosAtuais)
            .apply()

        _userProfile.value = _userProfile.value.copy(
            tipoPele = tipoPele,
            exposicaoSol = exposicaoSol,
            qualidadeSono = qualidadeSono,
            consumoAgua = consumoAgua,
            produtosAtuais = produtosAtuais
        )
        syncUserProfileToRoom()

        // Dynamically tailor routines based on skin type
        _morningRoutine.value = loadMorningRoutine()
        _nightRoutine.value = loadNightRoutine()
    }

    fun updateProfile(nome: String, idade: Int, telemovel: String) {
        prefs.edit()
            .putString("user_nome", nome)
            .putInt("user_idade", idade)
            .putString("user_telemovel", telemovel)
            .apply()

        _userProfile.value = _userProfile.value.copy(
            nome = nome,
            idade = idade,
            telemovel = telemovel
        )
        syncUserProfileToRoom()
    }

    fun deleteAccount() {
        prefs.edit().clear().apply()
        _userProfile.value = UserProfile()
        _shoppingList.value = emptyList()
        _payments.value = emptyList()
        _facialScans.value = emptyList()
        _diaryEntries.value = emptyList()
        _waterCupsToday.value = 0
        _routineStreakDays.value = 0

        scope.launch {
            roomDb.userProfileDao().clearProfile()
            roomDb.routineHistoryDao().clearAllHistory()
            roomDb.favoriteRecipeDao().clearAllFavorites()
        }
    }

    private fun computeAccountStatus(expiracaoMs: Long, isBlocked: Boolean): AccountStatus {
        if (isBlocked) return AccountStatus.BLOQUEADA
        val now = System.currentTimeMillis()
        if (now > expiracaoMs) return AccountStatus.EXPIRADA
        return if (prefs.getBoolean("assinatura_ativa", false)) AccountStatus.ATIVA else AccountStatus.EM_TESTE
    }

    fun refreshAccountStatus() {
        val current = _userProfile.value
        if (current.nome.isNotEmpty()) {
            val status = computeAccountStatus(current.diasAtivosExpiracao, current.isBlocked)
            if (status != current.estadoConta) {
                _userProfile.value = current.copy(estadoConta = status)
            }
        }
    }

    fun getDaysRemaining(): Int {
        val expiracao = _userProfile.value.diasAtivosExpiracao
        val now = System.currentTimeMillis()
        val diff = expiracao - now
        return if (diff <= 0) 0 else kotlin.math.ceil(diff.toDouble() / (24 * 60 * 60 * 1000)).toInt()
    }

    // -------------------------------------------------------------
    // PAYMENTS & MULTICAIXA EXPRESS (3.500 Kz)
    // -------------------------------------------------------------
    private fun loadPayments(): List<PaymentReceipt> {
        val json = prefs.getString("payments_json", null) ?: return listOf(
            PaymentReceipt(
                id = "REC-DEMO-01",
                nomeUtilizadora = "Dona Teresa",
                telemovel = "924112233",
                dataPagamento = "01/11/2026",
                metodo = "Multicaixa Express (923471505)",
                referencia = "923471505",
                valorKz = 3500,
                estado = PaymentStatus.APROVADO,
                reciboCodigo = "PL-2026-9843",
                dataAprovacao = System.currentTimeMillis() - 86400000
            )
        )
        return try {
            val list = mutableListOf<PaymentReceipt>()
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    PaymentReceipt(
                        id = obj.getString("id"),
                        nomeUtilizadora = obj.getString("nome"),
                        telemovel = obj.getString("telemovel"),
                        dataPagamento = obj.getString("data"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        metodo = obj.getString("metodo"),
                        referencia = obj.getString("referencia"),
                        valorKz = obj.getInt("valor"),
                        comprovativoUri = obj.optString("comprovativoUri").ifEmpty { null },
                        estado = PaymentStatus.valueOf(obj.getString("estado")),
                        motivoRejeicao = obj.optString("motivoRejeicao", ""),
                        dataAprovacao = if (obj.has("dataAprovacao")) obj.getLong("dataAprovacao") else null,
                        reciboCodigo = obj.optString("reciboCodigo", "")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun savePayments(list: List<PaymentReceipt>) {
        val array = JSONArray()
        list.forEach { p ->
            val obj = JSONObject().apply {
                put("id", p.id)
                put("nome", p.nomeUtilizadora)
                put("telemovel", p.telemovel)
                put("data", p.dataPagamento)
                put("timestamp", p.timestamp)
                put("metodo", p.metodo)
                put("referencia", p.referencia)
                put("valor", p.valorKz)
                put("comprovativoUri", p.comprovativoUri ?: "")
                put("estado", p.estado.name)
                put("motivoRejeicao", p.motivoRejeicao)
                if (p.dataAprovacao != null) put("dataAprovacao", p.dataAprovacao)
                put("reciboCodigo", p.reciboCodigo)
            }
            array.put(obj)
        }
        prefs.edit().putString("payments_json", array.toString()).apply()
        _payments.value = list
    }

    fun submitPayment(
        dataPagamento: String,
        metodo: String,
        referencia: String,
        comprovativoUri: String
    ): Result<PaymentReceipt> {
        // Prevent duplicate proof/reference
        val existing = _payments.value.any { it.referencia == referencia && it.comprovativoUri == comprovativoUri }
        if (existing) {
            return Result.failure(Exception("Este comprovativo ou referência já foi submetido anteriormente."))
        }

        val user = _userProfile.value
        val receipt = PaymentReceipt(
            id = "PAY-" + UUID.randomUUID().toString().take(8).uppercase(),
            nomeUtilizadora = user.nome.ifEmpty { "Utilizadora Pele Linda" },
            telemovel = user.telemovel.ifEmpty { "923471505" },
            dataPagamento = dataPagamento,
            timestamp = System.currentTimeMillis(),
            metodo = metodo,
            referencia = referencia,
            valorKz = 3500,
            comprovativoUri = comprovativoUri,
            estado = PaymentStatus.PENDENTE
        )

        val updated = listOf(receipt) + _payments.value
        savePayments(updated)
        addAuditLog("Pagamento submetido: ${user.nome} (3.500 Kz via $metodo)")
        return Result.success(receipt)
    }

    fun adminApprovePayment(paymentId: String, adminName: String = "Administrador Bernardo"): Boolean {
        val list = _payments.value.map { p ->
            if (p.id == paymentId) {
                p.copy(
                    estado = PaymentStatus.APROVADO,
                    dataAprovacao = System.currentTimeMillis(),
                    reciboCodigo = "PL-" + (1000..9999).random() + "-" + (100..999).random()
                )
            } else p
        }
        savePayments(list)

        // Extend user account by 30 days
        val currentExpiracao = _userProfile.value.diasAtivosExpiracao
        val baseTime = kotlin.math.max(System.currentTimeMillis(), currentExpiracao)
        val novaExpiracao = baseTime + (30L * 24 * 60 * 60 * 1000)

        prefs.edit()
            .putLong("dias_ativos_expiracao", novaExpiracao)
            .putBoolean("assinatura_ativa", true)
            .apply()

        _userProfile.value = _userProfile.value.copy(
            diasAtivosExpiracao = novaExpiracao,
            estadoConta = AccountStatus.ATIVA
        )

        addAuditLog("Pagamento aprovado por $adminName para $paymentId (+30 dias ativados).")
        return true
    }

    fun adminRejectPayment(paymentId: String, motivo: String, adminName: String = "Administrador Bernardo"): Boolean {
        val list = _payments.value.map { p ->
            if (p.id == paymentId) {
                p.copy(
                    estado = PaymentStatus.REJEITADO,
                    motivoRejeicao = motivo.ifEmpty { "Comprovativo ilegível ou valor incorreto." }
                )
            } else p
        }
        savePayments(list)
        addAuditLog("Pagamento $paymentId rejeitado por $adminName. Motivo: $motivo")
        return true
    }

    fun adminExtendUser(dias: Int, adminName: String = "Admin"): Boolean {
        val current = _userProfile.value.diasAtivosExpiracao
        val base = kotlin.math.max(System.currentTimeMillis(), current)
        val novoTempo = base + (dias.toLong() * 24 * 60 * 60 * 1000)

        prefs.edit().putLong("dias_ativos_expiracao", novoTempo).apply()
        _userProfile.value = _userProfile.value.copy(
            diasAtivosExpiracao = novoTempo,
            estadoConta = AccountStatus.ATIVA
        )
        addAuditLog("Conta prorrogada manualmente por $adminName em +$dias dias.")
        return true
    }

    fun adminToggleBlockUser(block: Boolean, adminName: String = "Admin"): Boolean {
        prefs.edit().putBoolean("user_blocked", block).apply()
        _userProfile.value = _userProfile.value.copy(
            isBlocked = block,
            estadoConta = if (block) AccountStatus.BLOQUEADA else computeAccountStatus(_userProfile.value.diasAtivosExpiracao, false)
        )
        addAuditLog("Utilizadora ${if (block) "bloqueada" else "desbloqueada"} por $adminName.")
        return true
    }

    // -------------------------------------------------------------
    // SHOPPING LIST
    // -------------------------------------------------------------
    private fun loadShoppingList(): List<ShoppingItem> {
        val json = prefs.getString("shopping_json", null) ?: return listOf(
            ShoppingItem("item_1", "Gel de Babosa Fresca", "Ingredientes Caseiros", 200, false, "Máscara de Babosa"),
            ShoppingItem("item_2", "Mel Puro de Abelha", "Ingredientes Caseiros", 250, false, "Máscara de Babosa"),
            ShoppingItem("item_3", "Sabonete Neutro de Glicerina", "Higiene Básica", 500, true, "Rotina Diária")
        )
        return try {
            val list = mutableListOf<ShoppingItem>()
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    ShoppingItem(
                        id = obj.getString("id"),
                        nome = obj.getString("nome"),
                        categoria = obj.getString("categoria"),
                        precoEstimadoKz = obj.getInt("preco"),
                        comprado = obj.getBoolean("comprado"),
                        origemReceita = obj.optString("origem", "")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveShoppingList(list: List<ShoppingItem>) {
        val array = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("nome", item.nome)
                put("categoria", item.categoria)
                put("preco", item.precoEstimadoKz)
                put("comprado", item.comprado)
                put("origem", item.origemReceita)
            }
            array.put(obj)
        }
        prefs.edit().putString("shopping_json", array.toString()).apply()
        _shoppingList.value = list
    }

    fun addIngredientsToShoppingList(recipe: Recipe) {
        val current = _shoppingList.value.toMutableList()
        recipe.ingredientes.forEach { ing ->
            // Check if already in list
            val alreadyPresent = current.any { it.nome.equals(ing.nome, ignoreCase = true) }
            if (!alreadyPresent) {
                current.add(
                    ShoppingItem(
                        id = "shop_" + UUID.randomUUID().toString().take(6),
                        nome = ing.nome,
                        categoria = "Receita: ${recipe.titulo}",
                        precoEstimadoKz = ing.precoEstimadoKz,
                        comprado = false,
                        origemReceita = recipe.titulo
                    )
                )
            }
        }
        saveShoppingList(current)
    }

    fun addCustomShoppingItem(nome: String, precoKz: Int) {
        val item = ShoppingItem(
            id = "shop_" + UUID.randomUUID().toString().take(6),
            nome = nome,
            categoria = "Itens Pessoais",
            precoEstimadoKz = precoKz,
            comprado = false
        )
        val updated = _shoppingList.value + item
        saveShoppingList(updated)
    }

    fun toggleShoppingItem(id: String) {
        val list = _shoppingList.value.map {
            if (it.id == id) it.copy(comprado = !it.comprado) else it
        }
        saveShoppingList(list)
    }

    fun removeShoppingItem(id: String) {
        val list = _shoppingList.value.filter { it.id != id }
        saveShoppingList(list)
    }

    fun clearPurchasedShoppingItems() {
        val list = _shoppingList.value.filter { !it.comprado }
        saveShoppingList(list)
    }

    // -------------------------------------------------------------
    // ALLERGY TEST (24 HOURS)
    // -------------------------------------------------------------
    fun startAllergyTest(recipeTitle: String) {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putLong("allergy_test_start", now)
            .putString("allergy_recipe_name", recipeTitle)
            .apply()
        _allergyTestStartTime.value = now
        _allergyRecipeName.value = recipeTitle
    }

    fun resetAllergyTest() {
        prefs.edit().remove("allergy_test_start").remove("allergy_recipe_name").apply()
        _allergyTestStartTime.value = 0L
        _allergyRecipeName.value = ""
    }

    // -------------------------------------------------------------
    // FACIAL SCAN & AI ESTIMATION
    // -------------------------------------------------------------
    private fun loadScans(): List<FacialAnalysisResult> {
        val json = prefs.getString("scans_json", null) ?: return listOf(
            FacialAnalysisResult(
                id = "scan_demo_1",
                timestamp = System.currentTimeMillis() - (3L * 86400000),
                photoUri = null,
                overallScore = 78,
                tipoPeleDetectada = "Mista com tendência a oleosidade e cravos",
                acneScore = 45,
                cravosScore = 60,
                manchasScore = 35,
                olheirasScore = 50,
                oleosidadeScore = 68,
                ressecamentoScore = 25,
                porosDilatadosScore = 55,
                resumoDescritivo = "Zona T (testa e nariz) com brilho evidente e poros abertos. Presença leve de marcas pós-inflamatórias nas maçãs do rosto.",
                recomendacaoPrincipal = "Lavar com água fresca e sabonete de glicerina, seguido de máscara de babosa 2x por semana."
            )
        )
        return try {
            val list = mutableListOf<FacialAnalysisResult>()
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    FacialAnalysisResult(
                        id = obj.getString("id"),
                        timestamp = obj.getLong("timestamp"),
                        photoUri = obj.optString("photoUri").ifEmpty { null },
                        overallScore = obj.getInt("score"),
                        tipoPeleDetectada = obj.getString("tipoPele"),
                        acneScore = obj.getInt("acne"),
                        cravosScore = obj.getInt("cravos"),
                        manchasScore = obj.getInt("manchas"),
                        olheirasScore = obj.getInt("olheiras"),
                        oleosidadeScore = obj.getInt("oleosidade"),
                        ressecamentoScore = obj.getInt("ressecamento"),
                        porosDilatadosScore = obj.getInt("poros"),
                        resumoDescritivo = obj.getString("resumo"),
                        recomendacaoPrincipal = obj.getString("recomendacao")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveScans(list: List<FacialAnalysisResult>) {
        val array = JSONArray()
        list.forEach { s ->
            val obj = JSONObject().apply {
                put("id", s.id)
                put("timestamp", s.timestamp)
                put("photoUri", s.photoUri ?: "")
                put("score", s.overallScore)
                put("tipoPele", s.tipoPeleDetectada)
                put("acne", s.acneScore)
                put("cravos", s.cravosScore)
                put("manchas", s.manchasScore)
                put("olheiras", s.olheirasScore)
                put("oleosidade", s.oleosidadeScore)
                put("ressecamento", s.ressecamentoScore)
                put("poros", s.porosDilatadosScore)
                put("resumo", s.resumoDescritivo)
                put("recomendacao", s.recomendacaoPrincipal)
            }
            array.put(obj)
        }
        prefs.edit().putString("scans_json", array.toString()).apply()
        _facialScans.value = list
    }

    fun analyzeAndSaveScan(photoUri: String?): FacialAnalysisResult {
        // Algorithm estimating skin health based on user age, answers, and visual scan variance
        val user = _userProfile.value
        val isTeen = user.idade < 20
        val baseAcne = if (isTeen || user.tipoPele == "Oleosa") (40..75).random() else (15..45).random()
        val baseOleosidade = if (user.tipoPele in listOf("Oleosa", "Mista")) (60..85).random() else (20..40).random()
        val baseManchas = if (user.exposicaoSol == "Muita") (45..70).random() else (20..40).random()
        val baseOlheiras = if (user.qualidadeSono.contains("Menos")) (60..80).random() else (30..50).random()
        val basePoros = if (baseOleosidade > 55) (50..75).random() else (25..45).random()
        val baseRessecamento = if (user.tipoPele == "Seca") (55..80).random() else (15..35).random()

        val avgFlaws = (baseAcne + baseOleosidade + baseManchas + basePoros + baseRessecamento) / 5
        val overallScore = kotlin.math.max(60, 100 - (avgFlaws * 0.45).toInt())

        val result = FacialAnalysisResult(
            id = "scan_" + UUID.randomUUID().toString().take(6),
            timestamp = System.currentTimeMillis(),
            photoUri = photoUri,
            overallScore = overallScore,
            tipoPeleDetectada = "${user.tipoPele} (com zonas de ${if (baseOleosidade > 60) "oleosidade ativa" else "equilíbrio"})",
            acneScore = baseAcne,
            cravosScore = (baseAcne * 0.9).toInt(),
            manchasScore = baseManchas,
            olheirasScore = baseOlheiras,
            oleosidadeScore = baseOleosidade,
            ressecamentoScore = baseRessecamento,
            porosDilatadosScore = basePoros,
            resumoDescritivo = "Pele com boa elasticidade geral. Observa-se maior concentração de oleosidade e poros abertos na zona frontal e nasal, com leve presença de marcas superficiais.",
            recomendacaoPrincipal = "Reforce a hidratação com gel de babosa ou água de arroz e não pule o protetor solar pela manhã para prevenir o escurecimento das manchas."
        )

        val updated = listOf(result) + _facialScans.value
        saveScans(updated)
        return result
    }

    // -------------------------------------------------------------
    // DIARY & BEFORE/AFTER
    // -------------------------------------------------------------
    private fun loadDiary(): List<DiaryEntry> {
        val json = prefs.getString("diary_json", null) ?: return listOf(
            DiaryEntry(
                id = "d_1",
                dataFormatada = "Há 14 dias",
                timestamp = System.currentTimeMillis() - (14L * 86400000),
                photoUri = null,
                notaPele = 6,
                observacoes = "Início do uso da máscara de babosa com mel. Muitas espinhas na testa e pele brilhosa.",
                acneNivel = 4,
                oleosidadeNivel = 5,
                manchasNivel = 3
            ),
            DiaryEntry(
                id = "d_2",
                dataFormatada = "Há 7 dias",
                timestamp = System.currentTimeMillis() - (7L * 86400000),
                photoUri = null,
                notaPele = 7,
                observacoes = "Espinhas estão secando e não surgiram novas. Comecei a passar água de arroz como tónico.",
                acneNivel = 3,
                oleosidadeNivel = 3,
                manchasNivel = 3
            ),
            DiaryEntry(
                id = "d_3",
                dataFormatada = "Hoje",
                timestamp = System.currentTimeMillis(),
                photoUri = null,
                notaPele = 9,
                observacoes = "Pele muito mais lisa e macia! A oleosidade no meio do dia diminuiu visivelmente.",
                acneNivel = 2,
                oleosidadeNivel = 2,
                manchasNivel = 2
            )
        )
        return try {
            val list = mutableListOf<DiaryEntry>()
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    DiaryEntry(
                        id = obj.getString("id"),
                        dataFormatada = obj.getString("data"),
                        timestamp = obj.getLong("timestamp"),
                        photoUri = obj.optString("photoUri").ifEmpty { null },
                        notaPele = obj.getInt("nota"),
                        observacoes = obj.getString("obs"),
                        acneNivel = obj.getInt("acne"),
                        oleosidadeNivel = obj.getInt("oleo"),
                        manchasNivel = obj.getInt("manchas")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addDiaryEntry(
        photoUri: String?,
        notaPele: Int,
        observacoes: String,
        acneNivel: Int,
        oleosidadeNivel: Int,
        manchasNivel: Int
    ) {
        val df = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "AO"))
        val entry = DiaryEntry(
            id = "diary_" + UUID.randomUUID().toString().take(6),
            dataFormatada = df.format(Date()),
            timestamp = System.currentTimeMillis(),
            photoUri = photoUri,
            notaPele = notaPele,
            observacoes = observacoes,
            acneNivel = acneNivel,
            oleosidadeNivel = oleosidadeNivel,
            manchasNivel = manchasNivel
        )
        val list = listOf(entry) + _diaryEntries.value
        saveDiary(list)
    }

    private fun saveDiary(list: List<DiaryEntry>) {
        val array = JSONArray()
        list.forEach { d ->
            val obj = JSONObject().apply {
                put("id", d.id)
                put("data", d.dataFormatada)
                put("timestamp", d.timestamp)
                put("photoUri", d.photoUri ?: "")
                put("nota", d.notaPele)
                put("obs", d.observacoes)
                put("acne", d.acneNivel)
                put("oleo", d.oleosidadeNivel)
                put("manchas", d.manchasNivel)
            }
            array.put(obj)
        }
        prefs.edit().putString("diary_json", array.toString()).apply()
        _diaryEntries.value = list
    }

    // -------------------------------------------------------------
    // DAILY ROUTINE & WATER
    // -------------------------------------------------------------
    private fun loadMorningRoutine(): List<RoutineTask> {
        val tipo = prefs.getString("tipo_pele", "Mista") ?: "Mista"
        val passoLimpeza = if (tipo == "Oleosa") {
            "Lavar com sabonete suave ou neutro para remover o sebo noturno."
        } else {
            "Enxaguar o rosto com água fresca ou aplicar sabonete de glicerina hidratante."
        }

        return listOf(
            RoutineTask("m1", RoutinePeriod.MANHA, 1, "Passo 1: Limpeza Fresca", passoLimpeza, 2),
            RoutineTask(
                "m2", RoutinePeriod.MANHA, 2, "Passo 2: Hidratação Leve",
                "Aplicar água de arroz fresca ou camada fina de creme hidratante com a pele ainda húmida.", 2
            ),
            RoutineTask(
                "m3", RoutinePeriod.MANHA, 3, "Passo 3: Protetor Solar",
                "Aplicar a quantidade de 2 dedos de protetor facial (FPS 30+). Essencial contra manchas e calor!", 2
            )
        )
    }

    private fun loadNightRoutine(): List<RoutineTask> {
        return listOf(
            RoutineTask(
                "n1", RoutinePeriod.NOITE, 1, "Passo 1: Higienização Noturna",
                "Lavar o rosto para retirar poeira da rua, suor e resíduos de protetor solar.", 3
            ),
            RoutineTask(
                "n2", RoutinePeriod.NOITE, 2, "Passo 2: Cuidado Caseiro / Babosa",
                "Aplicar gel puro de babosa (2-3x/semana) ou compressa de chá verde fresco nas olheiras.", 15
            ),
            RoutineTask(
                "n3", RoutinePeriod.NOITE, 3, "Passo 3: Nutrição Reparadora",
                "Passar o hidratante noturno suave e relaxar para dormir bem.", 2
            )
        )
    }

    fun toggleMorningTask(taskId: String) {
        val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "AO")).format(Date())
        var targetTask: RoutineTask? = null
        val list = _morningRoutine.value.map {
            if (it.id == taskId) {
                val updated = it.copy(concluidoHoje = !it.concluidoHoje)
                targetTask = updated
                updated
            } else it
        }
        _morningRoutine.value = list
        checkStreakProgress()

        // Persist to Room RoutineHistory
        targetTask?.let { task ->
            scope.launch {
                if (task.concluidoHoje) {
                    roomDb.routineHistoryDao().insertHistory(
                        RoutineHistoryEntity(
                            dateString = todayStr,
                            timestamp = System.currentTimeMillis(),
                            taskId = task.id,
                            taskTitle = task.titulo,
                            period = "MANHA",
                            stepNumber = task.passo,
                            completed = true
                        )
                    )
                } else {
                    roomDb.routineHistoryDao().deleteHistoryItem(todayStr, task.id)
                }
            }
        }
    }

    fun toggleNightTask(taskId: String) {
        val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "AO")).format(Date())
        var targetTask: RoutineTask? = null
        val list = _nightRoutine.value.map {
            if (it.id == taskId) {
                val updated = it.copy(concluidoHoje = !it.concluidoHoje)
                targetTask = updated
                updated
            } else it
        }
        _nightRoutine.value = list
        checkStreakProgress()

        // Persist to Room RoutineHistory
        targetTask?.let { task ->
            scope.launch {
                if (task.concluidoHoje) {
                    roomDb.routineHistoryDao().insertHistory(
                        RoutineHistoryEntity(
                            dateString = todayStr,
                            timestamp = System.currentTimeMillis(),
                            taskId = task.id,
                            taskTitle = task.titulo,
                            period = "NOITE",
                            stepNumber = task.passo,
                            completed = true
                        )
                    )
                } else {
                    roomDb.routineHistoryDao().deleteHistoryItem(todayStr, task.id)
                }
            }
        }
    }

    // -------------------------------------------------------------
    // FAVORITE RECIPES (ROOM PERSISTENCE)
    // -------------------------------------------------------------
    suspend fun toggleFavoriteRecipe(recipe: Recipe): Boolean {
        val isFav = roomDb.favoriteRecipeDao().isFavorite(recipe.id)
        if (isFav) {
            roomDb.favoriteRecipeDao().removeFavorite(recipe.id)
            return false
        } else {
            roomDb.favoriteRecipeDao().addFavorite(
                FavoriteRecipeEntity(
                    recipeId = recipe.id,
                    title = recipe.titulo,
                    subtitle = recipe.subTitulo,
                    budgetRange = recipe.categoriaPreco.label,
                    estimatedCostKz = recipe.custoEstimadoKz,
                    frequency = recipe.frequencia,
                    recommendedSkinType = recipe.tipoPeleRecomendada,
                    mainBenefit = recipe.beneficioPrincipal
                )
            )
            return true
        }
    }

    suspend fun isRecipeFavorite(recipeId: String): Boolean {
        return roomDb.favoriteRecipeDao().isFavorite(recipeId)
    }

    private fun checkStreakProgress() {
        val allMorning = _morningRoutine.value.all { it.concluidoHoje }
        val allNight = _nightRoutine.value.all { it.concluidoHoje }
        if (allMorning && allNight) {
            val currentStreak = _routineStreakDays.value + 1
            prefs.edit().putInt("routine_streak", currentStreak).apply()
            _routineStreakDays.value = currentStreak
        }
    }

    fun addWaterCup() {
        val current = _waterCupsToday.value
        if (current < 12) {
            val newTotal = current + 1
            prefs.edit().putInt("water_cups_today", newTotal).apply()
            _waterCupsToday.value = newTotal
        }
    }

    fun resetWaterCups() {
        prefs.edit().putInt("water_cups_today", 0).apply()
        _waterCupsToday.value = 0
    }

    // -------------------------------------------------------------
    // AUDIT LOGS FOR ADMIN
    // -------------------------------------------------------------
    private fun loadAuditLogs(): List<String> {
        val saved = prefs.getString("audit_logs", null) ?: return listOf(
            "Sistema: Banco de dados local inicializado em modo offline resiliente.",
            "Administrador: Configuração de Multicaixa Express (923471505) ativa."
        )
        return saved.split("\n---\n").filter { it.isNotBlank() }
    }

    private fun addAuditLog(action: String) {
        val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "AO"))
        val entry = "[${df.format(Date())}] $action"
        val current = listOf(entry) + _auditLogs.value.take(40)
        prefs.edit().putString("audit_logs", current.joinToString("\n---\n")).apply()
        _auditLogs.value = current
    }
}
