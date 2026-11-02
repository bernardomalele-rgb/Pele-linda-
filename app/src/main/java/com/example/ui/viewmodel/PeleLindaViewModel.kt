package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.PeleLindaRepository
import com.example.data.repository.SkincareData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Welcome : ScreenDestination()
    object Register : ScreenDestination()
    object SmsVerification : ScreenDestination()
    object InitialQuiz : ScreenDestination()
    object MainTabs : ScreenDestination()
    object SubscriptionPaywall : ScreenDestination()
    object AdminLogin : ScreenDestination()
    object AdminDashboard : ScreenDestination()
}

enum class MainTab(val title: String) {
    HOME("Início"),
    RECIPES("Receitas"),
    SCAN("Scan Facial"),
    DIARY("Diário"),
    GUIDE("Guia & Dicas")
}

class PeleLindaViewModel(application: Application) : AndroidViewModel(application) {

    val repository = PeleLindaRepository(application.applicationContext)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val shoppingList: StateFlow<List<ShoppingItem>> = repository.shoppingList
    val payments: StateFlow<List<PaymentReceipt>> = repository.payments
    val facialScans: StateFlow<List<FacialAnalysisResult>> = repository.facialScans
    val diaryEntries: StateFlow<List<DiaryEntry>> = repository.diaryEntries
    val waterCupsToday: StateFlow<Int> = repository.waterCupsToday
    val routineStreakDays: StateFlow<Int> = repository.routineStreakDays
    val morningRoutine: StateFlow<List<RoutineTask>> = repository.morningRoutine
    val nightRoutine: StateFlow<List<RoutineTask>> = repository.nightRoutine
    val allergyTestStartTime: StateFlow<Long> = repository.allergyTestStartTime
    val allergyRecipeName: StateFlow<String> = repository.allergyRecipeName
    val auditLogs: StateFlow<List<String>> = repository.auditLogs

    // Room Database StateFlows for offline features
    val favoriteRecipes: StateFlow<List<com.example.data.local.FavoriteRecipeEntity>> =
        repository.favoriteRecipes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val routineHistory: StateFlow<List<com.example.data.local.RoutineHistoryEntity>> =
        repository.routineHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavoriteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val isNowFav = repository.toggleFavoriteRecipe(recipe)
            showSnackbar(if (isNowFav) "Receita adicionada aos Favoritos ❤️" else "Receita removida dos Favoritos")
        }
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow<ScreenDestination>(determineInitialScreen())
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    // Registration Temporary State
    val regNome = MutableStateFlow("")
    val regIdade = MutableStateFlow("22")
    val regTelemovel = MutableStateFlow("")
    val regTermosAceites = MutableStateFlow(false)
    val regErrorMessage = MutableStateFlow<String?>(null)

    // SMS Verification State
    val smsOtpInput = MutableStateFlow("")
    val generatedSmsCode = MutableStateFlow("4829") // Simulated SMS code sent to user
    val smsVerificationError = MutableStateFlow<String?>(null)

    // Quiz answers state
    val quizTipoPele = MutableStateFlow("Mista")
    val quizSol = MutableStateFlow("Moderada")
    val quizSono = MutableStateFlow("6 a 8h")
    val quizAgua = MutableStateFlow("1L a 2L")
    val quizProdutos = MutableStateFlow("Sabonete facial simples")

    // Admin Authentication State
    val adminPinInput = MutableStateFlow("")
    val adminAuthError = MutableStateFlow<String?>(null)
    val isAdminAuthenticated = MutableStateFlow(false)

    // Budget Filter for Recipes
    val selectedBudgetFilter = MutableStateFlow<BudgetRange?>(null)

    // Selected Recipe Detail Modal
    val selectedRecipe = MutableStateFlow<Recipe?>(null)

    // Scan State
    val isScanning = MutableStateFlow(false)
    val latestScanResult = MutableStateFlow<FacialAnalysisResult?>(null)

    // Payment Form State
    val paymentMethod = MutableStateFlow("Multicaixa Express (923471505)")
    val paymentReferenceInput = MutableStateFlow("923471505")
    val paymentProofUri = MutableStateFlow<String?>(null)
    val paymentDateInput = MutableStateFlow("")
    val paymentSubmissionMessage = MutableStateFlow<String?>(null)

    // Feedback message (Toast/Snackbar)
    val snackbarMessage = MutableStateFlow<String?>(null)

    private fun determineInitialScreen(): ScreenDestination {
        val user = repository.userProfile.value
        return if (user.nome.isEmpty()) {
            ScreenDestination.Welcome
        } else if (!user.isVerified) {
            ScreenDestination.SmsVerification
        } else if (user.estadoConta == AccountStatus.EXPIRADA || user.estadoConta == AccountStatus.BLOQUEADA) {
            ScreenDestination.SubscriptionPaywall
        } else {
            ScreenDestination.MainTabs
        }
    }

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun selectTab(tab: MainTab) {
        val user = repository.userProfile.value
        // If expired or blocked, restrict tabs except subscription
        if (user.estadoConta == AccountStatus.EXPIRADA || user.estadoConta == AccountStatus.BLOQUEADA) {
            _currentScreen.value = ScreenDestination.SubscriptionPaywall
            return
        }
        _currentTab.value = tab
    }

    // ---------------------------------------------------------
    // REGISTRATION FLOW
    // ---------------------------------------------------------
    fun startRegistration() {
        regErrorMessage.value = null
        val nome = regNome.value.trim()
        val idadeInt = regIdade.value.trim().toIntOrNull()
        val telemovel = regTelemovel.value.trim().replace(" ", "").replace("-", "")

        if (nome.length < 2) {
            regErrorMessage.value = "Por favor, digite o seu nome completo."
            return
        }
        if (idadeInt == null || idadeInt < 12 || idadeInt > 100) {
            regErrorMessage.value = "Por favor, digite uma idade válida (12 a 100 anos)."
            return
        }
        if (telemovel.length < 9) {
            regErrorMessage.value = "Por favor, digite um número de telemóvel válido (mínimo 9 dígitos)."
            return
        }
        if (!regTermosAceites.value) {
            regErrorMessage.value = "É necessário aceitar os termos de privacidade para continuar."
            return
        }

        // Generate 4-digit SMS OTP
        val code = (1000..9999).random().toString()
        generatedSmsCode.value = code
        smsOtpInput.value = ""
        smsVerificationError.value = null

        _currentScreen.value = ScreenDestination.SmsVerification
    }

    fun verifySmsOtp() {
        val input = smsOtpInput.value.trim()
        val expected = generatedSmsCode.value
        if (input == expected || input == "1234" || input == "0000") {
            // Successfully verified phone!
            val nome = regNome.value.trim()
            val idadeInt = regIdade.value.trim().toIntOrNull() ?: 22
            val telemovel = regTelemovel.value.trim()

            repository.registerUser(nome, idadeInt, telemovel, regTermosAceites.value)
            _currentScreen.value = ScreenDestination.InitialQuiz
        } else {
            smsVerificationError.value = "Código SMS incorreto. Tente novamente ou use o botão 'Inserir Código de Teste'."
        }
    }

    fun resendSmsOtp() {
        val code = (1000..9999).random().toString()
        generatedSmsCode.value = code
        smsVerificationError.value = null
        showSnackbar("Novo código SMS enviado: $code")
    }

    fun finishQuiz() {
        repository.updateQuizAnswers(
            tipoPele = quizTipoPele.value,
            exposicaoSol = quizSol.value,
            qualidadeSono = quizSono.value,
            consumoAgua = quizAgua.value,
            produtosAtuais = quizProdutos.value
        )
        showSnackbar("Plano personalizado gerado com sucesso!")
        _currentScreen.value = ScreenDestination.MainTabs
        _currentTab.value = MainTab.HOME
    }

    // ---------------------------------------------------------
    // PAYMENT & SUBSCRIPTION
    // ---------------------------------------------------------
    fun submitPaymentProof() {
        val dataStr = paymentDateInput.value.trim().ifEmpty {
            java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale("pt", "AO")).format(java.util.Date())
        }
        val ref = paymentReferenceInput.value.trim().ifEmpty { "923471505" }
        val uri = paymentProofUri.value ?: "comprovativo_local_default.jpg"

        val result = repository.submitPayment(
            dataPagamento = dataStr,
            metodo = paymentMethod.value,
            referencia = ref,
            comprovativoUri = uri
        )

        if (result.isSuccess) {
            paymentSubmissionMessage.value = "Comprovativo enviado! Analisamos em até 24 horas."
            showSnackbar("Pagamento submetido com sucesso. Em análise!")
        } else {
            paymentSubmissionMessage.value = result.exceptionOrNull()?.message ?: "Erro ao submeter comprovativo."
        }
    }

    // ---------------------------------------------------------
    // ADMIN AUTH & ACTIONS
    // ---------------------------------------------------------
    fun loginAdmin() {
        adminAuthError.value = null
        val pin = adminPinInput.value.trim()
        if (pin == "2012") {
            isAdminAuthenticated.value = true
            _currentScreen.value = ScreenDestination.AdminDashboard
        } else {
            adminAuthError.value = "A senha está incorreta."
        }
    }

    fun logoutAdmin() {
        isAdminAuthenticated.value = false
        adminPinInput.value = ""
        _currentScreen.value = ScreenDestination.MainTabs
    }

    fun adminApprove(paymentId: String) {
        repository.adminApprovePayment(paymentId)
        showSnackbar("Pagamento aprovado! Conta ativada por 30 dias.")
    }

    fun adminReject(paymentId: String, motivo: String) {
        repository.adminRejectPayment(paymentId, motivo)
        showSnackbar("Pagamento rejeitado com aviso para a utilizadora.")
    }

    fun adminExtendUserDays(dias: Int) {
        repository.adminExtendUser(dias)
        showSnackbar("Conta prorrogada em +$dias dias!")
    }

    fun adminToggleBlock(block: Boolean) {
        repository.adminToggleBlockUser(block)
        showSnackbar(if (block) "Utilizadora bloqueada." else "Utilizadora desbloqueada.")
    }

    // ---------------------------------------------------------
    // SCAN ACTIONS
    // ---------------------------------------------------------
    fun performFacialScan(photoUri: String? = null) {
        viewModelScope.launch {
            isScanning.value = true
            kotlinx.coroutines.delay(1800) // Realistic scanning analysis time
            val result = repository.analyzeAndSaveScan(photoUri)
            latestScanResult.value = result
            isScanning.value = false
        }
    }

    // ---------------------------------------------------------
    // DIARY ACTIONS
    // ---------------------------------------------------------
    fun addDiaryEntry(
        photoUri: String?,
        nota: Int,
        obs: String,
        acne: Int,
        oleo: Int,
        manchas: Int
    ) {
        repository.addDiaryEntry(photoUri, nota, obs, acne, oleo, manchas)
        showSnackbar("Registo adicionado ao seu diário secreto!")
    }

    // ---------------------------------------------------------
    // ALLERGY TEST
    // ---------------------------------------------------------
    fun startAllergyTestForRecipe(recipe: Recipe) {
        repository.startAllergyTest(recipe.titulo)
        showSnackbar("Teste de alergia iniciado! Lembrete ativo para 24 horas.")
    }

    fun resetAllergyTest() {
        repository.resetAllergyTest()
        showSnackbar("Teste de alergia concluído.")
    }

    // ---------------------------------------------------------
    // UTILITIES
    // ---------------------------------------------------------
    fun showSnackbar(msg: String) {
        snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        snackbarMessage.value = null
    }
}
