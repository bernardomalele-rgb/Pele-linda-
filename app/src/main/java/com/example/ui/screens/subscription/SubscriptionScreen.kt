package com.example.ui.screens.subscription

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AccountStatus
import com.example.data.model.PaymentReceipt
import com.example.data.model.PaymentStatus
import com.example.ui.components.BigButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel
import com.example.ui.viewmodel.ScreenDestination
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    viewModel: PeleLindaViewModel,
    isBlockingPaywall: Boolean = false
) {
    val context = LocalContext.current
    val user by viewModel.userProfile.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val daysRemaining = viewModel.repository.getDaysRemaining()
    val submissionMessage by viewModel.paymentSubmissionMessage.collectAsState()

    var selectedProofUri by remember { mutableStateOf<Uri?>(null) }
    var selectedReceiptForView by remember { mutableStateOf<PaymentReceipt?>(null) }
    var paymentDate by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale("pt", "AO")).format(Date())) }
    var selectedMethod by remember { mutableStateOf("Multicaixa Express (923471505)") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedProofUri = uri
            viewModel.paymentProofUri.value = uri.toString()
        }
    }

    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    fun copyToClipboard(text: String, label: String) {
        val clip = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clip)
        viewModel.showSnackbar("$label copiado: $text")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isBlockingPaywall) "Assinatura Pele Linda" else "Mensalidade & Pagamento",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (!isBlockingPaywall) {
                        IconButton(onClick = { viewModel.navigateTo(ScreenDestination.MainTabs) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenDestination.AdminLogin) }) {
                        Icon(Icons.Outlined.AdminPanelSettings, contentDescription = "Admin")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        },
        containerColor = CreamBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Subscription Status Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = if (user.estadoConta == AccountStatus.ATIVA) MintContainer else RoseContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = when (user.estadoConta) {
                                    AccountStatus.ATIVA -> "✓ CONTA ATIVA"
                                    AccountStatus.EM_TESTE -> "TESTE GRATUITO EM CURSO"
                                    AccountStatus.EXPIRADA -> "ACESSO EXPIRADO"
                                    AccountStatus.BLOQUEADA -> "CONTA BLOQUEADA"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (user.estadoConta == AccountStatus.ATIVA) MintOnContainer else RoseOnContainer
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "3.500 Kz / mês",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = RosePrimary,
                                fontSize = 32.sp
                            )
                        )

                        Text(
                            text = if (user.estadoConta == AccountStatus.EM_TESTE) {
                                "Restam $daysRemaining dias do seu teste de 7 dias."
                            } else if (user.estadoConta == AccountStatus.ATIVA) {
                                "Válido por mais $daysRemaining dias com acesso irrestrito."
                            } else {
                                "O seu período de teste terminou. Renove para desbloquear o app."
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextDark,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        if (isBlockingPaywall) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🔒 Os seus dados, diário e histórico de fotos continuam salvos e seguros no seu telemóvel.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MintSecondary,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }

            // Instructions & Multicaixa Express Details
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(RosePrimary, MintSecondary))
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payment_instructions_card")
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Como Pagar a Mensalidade:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Method 1: Multicaixa Express (923471505)
                        Surface(
                            color = CreamSurfaceVariant,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text("Opção 1: Multicaixa Express", fontWeight = FontWeight.Bold, color = RosePrimary)
                                        Text("Transferência para o número:", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                        Text("923 471 505", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = TextDark))
                                    }

                                    Button(
                                        onClick = { copyToClipboard("923471505", "Número Multicaixa") },
                                        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("copy_phone_button")
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Copiar", fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = OutlineSoft)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text("Valor exato da mensalidade:", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                        Text("3.500 Kz", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MintSecondary))
                                    }

                                    OutlinedButton(
                                        onClick = { copyToClipboard("3500", "Valor da mensalidade") },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("copy_amount_button")
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Copiar Valor", fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Method 2: Referência
                        Surface(
                            color = CreamSurfaceVariant,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Opção 2: Pagamento por Referência", fontWeight = FontWeight.Bold, color = TextDark)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Entidade: 00923 | Referência: 923 471 505", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            }
                        }
                    }
                }
            }

            // Upload Proof Form
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Enviar Comprovativo de Pagamento",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Carregue a foto ou print do talão do Multicaixa Express para validação.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MintSecondary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("pick_payment_proof_button")
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (selectedProofUri != null) "Comprovativo Carregado ✓" else "Selecionar Foto do Comprovativo",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (selectedProofUri != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, MintSecondary, RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = selectedProofUri,
                                    contentDescription = "Comprovativo selecionado",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = paymentDate,
                            onValueChange = { paymentDate = it },
                            label = { Text("Data do Pagamento", color = TextDark) },
                            placeholder = { Text("Ex.: 02/11/2026", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = RosePrimary) },
                            singleLine = true,
                            textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF1F1A1B),
                                unfocusedTextColor = Color(0xFF1F1A1B),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = RosePrimary,
                                unfocusedBorderColor = OutlineSoft
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (submissionMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = submissionMessage ?: "",
                                color = if (submissionMessage!!.contains("sucesso") || submissionMessage!!.contains("enviado")) MintSecondary else MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        BigButton(
                            text = "Submeter Comprovativo (3.500 Kz)",
                            onClick = {
                                viewModel.paymentDateInput.value = paymentDate
                                viewModel.paymentMethod.value = selectedMethod
                                viewModel.submitPaymentProof()
                            },
                            enabled = selectedProofUri != null,
                            icon = Icons.Default.Send,
                            testTag = "submit_payment_button"
                        )
                    }
                }
            }

            // Payment Receipts History
            item {
                Text(
                    text = "Histórico de Pagamentos & Recibos",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
            }

            if (payments.isEmpty()) {
                item {
                    Text(
                        text = "Nenhum comprovativo enviado até ao momento.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            } else {
                items(payments) { receipt ->
                    PaymentReceiptCard(
                        receipt = receipt,
                        onClick = { selectedReceiptForView = receipt }
                    )
                }
            }
        }
    }

    // Modal to view Printable Receipt in PDF / Receipt view
    if (selectedReceiptForView != null) {
        val r = selectedReceiptForView!!
        AlertDialog(
            onDismissRequest = { selectedReceiptForView = null },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.showSnackbar("Recibo guardado como PDF com sucesso!")
                        selectedReceiptForView = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Descarregar Recibo PDF")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedReceiptForView = null }) { Text("Fechar", color = TextDark) }
            },
            title = {
                Text("Recibo Oficial de Pagamento", fontWeight = FontWeight.Bold, color = RosePrimary)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                    Text("PELE LINDA - CUIDADOS COM A PELE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Código do Recibo: ${r.reciboCodigo.ifEmpty { r.id }}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    Text("Titular: ${r.nomeUtilizadora}", style = MaterialTheme.typography.bodyMedium)
                    Text("Telemóvel: ${r.telemovel}", style = MaterialTheme.typography.bodyMedium)
                    Text("Data: ${r.dataPagamento}", style = MaterialTheme.typography.bodyMedium)
                    Text("Método: ${r.metodo}", style = MaterialTheme.typography.bodyMedium)
                    Text("Valor Pago: 3.500 Kz", fontWeight = FontWeight.Bold, color = MintSecondary, style = MaterialTheme.typography.bodyLarge)
                    Text("Estado: ${r.estado.name}", fontWeight = FontWeight.Bold)

                    if (r.estado == PaymentStatus.REJEITADO && r.motivoRejeicao.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Motivo da rejeição: ${r.motivoRejeicao}", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Validade: 30 dias de acesso contínuo a todas as ferramentas do app.",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }
            }
        )
    }
}

@Composable
fun PaymentReceiptCard(
    receipt: PaymentReceipt,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(OutlineSoft)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        when (receipt.estado) {
                            PaymentStatus.APROVADO -> MintContainer
                            PaymentStatus.PENDENTE -> HoneyContainer
                            PaymentStatus.REJEITADO -> Color(0xFFFFEBEE)
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (receipt.estado) {
                        PaymentStatus.APROVADO -> Icons.Default.Check
                        PaymentStatus.PENDENTE -> Icons.Default.AccessTime
                        PaymentStatus.REJEITADO -> Icons.Default.Close
                    },
                    contentDescription = null,
                    tint = when (receipt.estado) {
                        PaymentStatus.APROVADO -> MintSecondary
                        PaymentStatus.PENDENTE -> HoneyTertiary
                        PaymentStatus.REJEITADO -> Color(0xFFD32F2F)
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${receipt.valorKz} Kz - ${receipt.dataPagamento}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextDark)
                )
                Text(
                    text = when (receipt.estado) {
                        PaymentStatus.APROVADO -> "Aprovado ✓ Recibo ${receipt.reciboCodigo}"
                        PaymentStatus.PENDENTE -> "Em análise (até 24 horas)"
                        PaymentStatus.REJEITADO -> "Rejeitado: ${receipt.motivoRejeicao}"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = when (receipt.estado) {
                            PaymentStatus.APROVADO -> MintSecondary
                            PaymentStatus.PENDENTE -> HoneyTertiary
                            PaymentStatus.REJEITADO -> Color(0xFFD32F2F)
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
        }
    }
}
