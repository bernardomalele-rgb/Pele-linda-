package com.example.data.model

data class UserProfile(
    val id: String = "user_default",
    val nome: String = "",
    val idade: Int = 22,
    val telemovel: String = "",
    val isVerified: Boolean = false,
    val termosAceites: Boolean = false,
    val tipoPele: String = "Mista", // Oleosa, Seca, Mista, Sensível, Normal
    val exposicaoSol: String = "Moderada", // Pouca, Moderada, Muita
    val qualidadeSono: String = "6 a 8h", // Menos de 6h, 6 a 8h, Mais de 8h
    val consumoAgua: String = "1L a 2L", // Menos de 1L, 1L a 2L, Mais de 2L
    val produtosAtuais: String = "Sabonete facial simples",
    val dataRegisto: Long = System.currentTimeMillis(),
    val testeGratisUsado: Boolean = true,
    val diasAtivosExpiracao: Long = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000), // 7 dias de teste
    val estadoConta: AccountStatus = AccountStatus.EM_TESTE,
    val isBlocked: Boolean = false,
    val emailNotificado: Boolean = false
)

enum class AccountStatus {
    EM_TESTE,
    ATIVA,
    EXPIRADA,
    BLOQUEADA
}

data class PaymentReceipt(
    val id: String,
    val nomeUtilizadora: String,
    val telemovel: String,
    val dataPagamento: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metodo: String = "Multicaixa Express (923471505)",
    val referencia: String = "923471505",
    val valorKz: Int = 3500,
    val comprovativoUri: String? = null,
    val estado: PaymentStatus = PaymentStatus.PENDENTE,
    val motivoRejeicao: String = "",
    val dataAprovacao: Long? = null,
    val reciboCodigo: String = ""
)

enum class PaymentStatus {
    PENDENTE,
    APROVADO,
    REJEITADO
}

data class Recipe(
    val id: String,
    val titulo: String,
    val subTitulo: String,
    val categoriaPreco: BudgetRange,
    val custoEstimadoKz: Int,
    val tempoPreparo: String,
    val frequencia: String,
    val tipoPeleRecomendada: String,
    val beneficioPrincipal: String,
    val ingredientes: List<IngredientItem>,
    val passoAPasso: List<String>,
    val avisosSeguranca: String,
    val requerTesteAlergia: Boolean = true
)

enum class BudgetRange(val label: String, val maxKz: Int) {
    ATE_500("Até 500 Kz", 500),
    ATE_1500("Até 1.500 Kz", 1500),
    ATE_3000("Até 3.000 Kz", 3000)
}

data class IngredientItem(
    val nome: String,
    val quantidade: String,
    val precoEstimadoKz: Int
)

data class ShoppingItem(
    val id: String,
    val nome: String,
    val categoria: String,
    val precoEstimadoKz: Int,
    val comprado: Boolean = false,
    val origemReceita: String = ""
)

data class RoutineTask(
    val id: String,
    val periodo: RoutinePeriod,
    val passo: Int,
    val titulo: String,
    val descricao: String,
    val duracaoMin: Int = 3,
    val concluidoHoje: Boolean = false
)

enum class RoutinePeriod {
    MANHA,
    NOITE
}

data class FacialAnalysisResult(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val photoUri: String? = null,
    val overallScore: Int = 82, // 0 a 100
    val tipoPeleDetectada: String = "Mista com tendência a oleosidade",
    val acneScore: Int = 65, // 0 = limpo, 100 = intenso
    val cravosScore: Int = 50,
    val manchasScore: Int = 40,
    val olheirasScore: Int = 55,
    val oleosidadeScore: Int = 70,
    val ressecamentoScore: Int = 30,
    val porosDilatadosScore: Int = 60,
    val resumoDescritivo: String,
    val recomendacaoPrincipal: String,
    val avisoMedico: String = "Este resultado é uma estimativa inteligente e não substitui uma consulta com dermatologista."
)

data class DiaryEntry(
    val id: String,
    val dataFormatada: String,
    val timestamp: Long = System.currentTimeMillis(),
    val photoUri: String? = null,
    val notaPele: Int = 8, // 1 a 10
    val observacoes: String = "",
    val acneNivel: Int = 2, // 1 a 5
    val oleosidadeNivel: Int = 3, // 1 a 5
    val manchasNivel: Int = 2 // 1 a 5
)

data class Achievement(
    val id: String,
    val titulo: String,
    val descricao: String,
    val icone: String,
    val desbloqueado: Boolean = false,
    val progresso: Int = 0,
    val total: Int = 7
)
