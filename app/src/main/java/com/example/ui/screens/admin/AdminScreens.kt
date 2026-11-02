package com.example.ui.screens.admin

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(viewModel: PeleLindaViewModel) {
    val pin by viewModel.adminPinInput.collectAsState()
    val error by viewModel.adminAuthError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel de Administração", fontWeight = FontWeight.Bold, color = TextDark) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenDestination.MainTabs) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        },
        containerColor = CreamBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(RoseContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = RosePrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Autenticação Administrativa",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1A1B)
                )
            )

            Text(
                text = "Digite a senha numérica do painel para gerir os pagamentos.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMuted,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Professional Container Card with padding and elevation
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(1.dp, OutlineSoft.copy(alpha = 0.8f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_login_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Security Header Badge inside the Card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = RosePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ACESSO SEGURO",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = RosePrimary,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }

                    // High-Contrast Clear Display Card showing the typed PIN characters
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CreamBackground),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, if (error != null) Color(0xFFBA1A1A) else RosePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SENHA DIGITADA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (pin.isEmpty()) "____" else pin,
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (pin.isEmpty()) OutlineSoft else Color(0xFF1F1A1B),
                                    letterSpacing = 8.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.testTag("admin_pin_display")
                            )
                        }
                    }

                    // OutlinedTextField with explicit high contrast dark text
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = pin,
                        onValueChange = {
                            viewModel.adminPinInput.value = it.filter { char -> char.isDigit() }
                            viewModel.adminAuthError.value = null
                        },
                        label = { Text("Digitar com teclado", color = TextDark) },
                        placeholder = { Text("Ex: 2012", color = TextMuted) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            color = Color(0xFF1F1A1B),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF1F1A1B),
                            unfocusedTextColor = Color(0xFF1F1A1B),
                            focusedContainerColor = CreamBackground,
                            unfocusedContainerColor = CreamBackground,
                            focusedBorderColor = RosePrimary,
                            unfocusedBorderColor = OutlineSoft,
                            cursorColor = RosePrimary,
                            focusedLabelColor = RosePrimary,
                            unfocusedLabelColor = TextDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input")
                    )

                    // STRICT ERROR DISPLAY: ONLY "A senha está incorreta."
                    if (error != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Color(0xFFBA1A1A),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "A senha está incorreta.",
                                color = Color(0xFFBA1A1A),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.testTag("admin_pin_error_text")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // On-Screen Numeric Keypad for fast, failproof entry
                    AdminNumericKeypad(
                        onDigitClick = { digit ->
                            viewModel.adminAuthError.value = null
                            if (pin.length < 8) {
                                viewModel.adminPinInput.value = pin + digit
                            }
                        },
                        onDeleteClick = {
                            viewModel.adminAuthError.value = null
                            if (pin.isNotEmpty()) {
                                viewModel.adminPinInput.value = pin.dropLast(1)
                            }
                        },
                        onClearClick = {
                            viewModel.adminAuthError.value = null
                            viewModel.adminPinInput.value = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    BigButton(
                        text = "Entrar no Painel",
                        onClick = { viewModel.loginAdmin() },
                        enabled = pin.isNotBlank(),
                        icon = Icons.Default.LockOpen,
                        testTag = "admin_login_submit_button"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun AdminNumericKeypad(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("Limpar", "0", "⌫")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (row in keys) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (key in row) {
                    val isAction = key == "Limpar" || key == "⌫"
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                when (key) {
                                    "Limpar" -> onClearClick()
                                    "⌫" -> onDeleteClick()
                                    else -> onDigitClick(key)
                                }
                            }
                            .testTag("keypad_$key"),
                        color = if (isAction) RoseContainer else Color.White,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isAction) RosePrimary.copy(alpha = 0.3f) else OutlineSoft
                        ),
                        shadowElevation = 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = key,
                                style = TextStyle(
                                    fontSize = if (key.length > 1) 14.sp else 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAction) RosePrimary else Color(0xFF1F1A1B)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: PeleLindaViewModel) {
    val user by viewModel.userProfile.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<PaymentStatus?>(null) }
    var rejectionDialogPaymentId by remember { mutableStateOf<String?>(null) }
    var rejectionReasonInput by remember { mutableStateOf("") }
    var selectedProofZoomUri by remember { mutableStateOf<String?>(null) }

    val pendingCount = payments.count { it.estado == PaymentStatus.PENDENTE }
    val approvedCount = payments.count { it.estado == PaymentStatus.APROVADO }
    val monthlyRevenueKz = approvedCount * 3500

    val filteredPayments = remember(payments, searchQuery, statusFilter) {
        payments.filter { p ->
            val matchQuery = p.nomeUtilizadora.contains(searchQuery, ignoreCase = true) ||
                    p.telemovel.contains(searchQuery) || p.referencia.contains(searchQuery)
            val matchStatus = statusFilter == null || p.estado == statusFilter
            matchQuery && matchStatus
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel do Administrador", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.logoutAdmin() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Sair")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logoutAdmin() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Terminar Sessão")
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
            // Metrics Summary Grid (Pending, Active, Trial, Revenue)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Pendentes",
                        value = "$pendingCount",
                        color = HoneyTertiary,
                        containerColor = HoneyContainer,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Ativas (Pagas)",
                        value = "$approvedCount",
                        color = MintSecondary,
                        containerColor = MintContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Em Teste Grátis",
                        value = if (user.estadoConta == AccountStatus.EM_TESTE) "1" else "0",
                        color = RosePrimary,
                        containerColor = RoseContainer,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Receita do Mês",
                        value = "$monthlyRevenueKz Kz",
                        color = Color(0xFF2E7D32),
                        containerColor = Color(0xFFE8F5E9),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Tab Navigation inside Admin (Pagamentos, Gestão Utilizadoras, Auditoria)
            item {
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = Color.White,
                    contentColor = RosePrimary,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Pagamentos ($pendingCount)") }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("Utilizadora") }
                    )
                    Tab(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        text = { Text("Auditoria") }
                    )
                }
            }

            when (activeTab) {
                0 -> {
                    // Search & Filter
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Pesquisar por nome ou telemóvel...", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 15.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF1F1A1B),
                                unfocusedTextColor = Color(0xFF1F1A1B),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = RosePrimary,
                                unfocusedBorderColor = OutlineSoft
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = statusFilter == null,
                                onClick = { statusFilter = null },
                                label = { Text("Todos") }
                            )
                            FilterChip(
                                selected = statusFilter == PaymentStatus.PENDENTE,
                                onClick = { statusFilter = PaymentStatus.PENDENTE },
                                label = { Text("Pendentes") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = HoneyContainer)
                            )
                            FilterChip(
                                selected = statusFilter == PaymentStatus.APROVADO,
                                onClick = { statusFilter = PaymentStatus.APROVADO },
                                label = { Text("Aprovados") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MintContainer)
                            )
                            FilterChip(
                                selected = statusFilter == PaymentStatus.REJEITADO,
                                onClick = { statusFilter = PaymentStatus.REJEITADO },
                                label = { Text("Rejeitados") }
                            )
                        }
                    }

                    if (filteredPayments.isEmpty()) {
                        item {
                            Text(
                                text = "Nenhum pagamento corresponde ao filtro.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    } else {
                        items(filteredPayments) { payment ->
                            AdminPaymentItem(
                                payment = payment,
                                onApprove = { viewModel.adminApprove(payment.id) },
                                onReject = {
                                    rejectionDialogPaymentId = payment.id
                                    rejectionReasonInput = ""
                                },
                                onZoomProof = { selectedProofZoomUri = payment.comprovativoUri }
                            )
                        }
                    }
                }

                1 -> {
                    // User Management Section
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Gestão da Utilizadora Atual",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Nome: ${user.nome.ifEmpty { "Sem registo" }}", fontWeight = FontWeight.SemiBold)
                                Text("Telemóvel: ${user.telemovel.ifEmpty { "Nenhum" }}")
                                Text("Idade: ${user.idade} anos")
                                Text("Tipo de Pele: ${user.tipoPele}")
                                Text("Estado Atual: ${user.estadoConta.name}", color = RosePrimary, fontWeight = FontWeight.Bold)
                                Text("Dias restantes: ${viewModel.repository.getDaysRemaining()} dias")

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = OutlineSoft)
                                Spacer(modifier = Modifier.height(16.dp))

                                Text("Ações Manuais do Administrador:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { viewModel.adminExtendUserDays(7) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MintSecondary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+7 Dias")
                                    }

                                    Button(
                                        onClick = { viewModel.adminExtendUserDays(30) },
                                        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+30 Dias")
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = { viewModel.adminToggleBlock(!user.isBlocked) },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (user.isBlocked) MintSecondary else MaterialTheme.colorScheme.error
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        if (user.isBlocked) Icons.Default.LockOpen else Icons.Default.Block,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (user.isBlocked) "Desbloquear Utilizadora" else "Bloquear Utilizadora (Comprovativo Falso)")
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Audit Logs
                    items(auditLogs) { log ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = log,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = TextDark
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Rejection Dialog with Custom Reason
    if (rejectionDialogPaymentId != null) {
        AlertDialog(
            onDismissRequest = { rejectionDialogPaymentId = null },
            confirmButton = {
                Button(
                    onClick = {
                        val pid = rejectionDialogPaymentId!!
                        viewModel.adminReject(pid, rejectionReasonInput)
                        rejectionDialogPaymentId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirmar Rejeição")
                }
            },
            dismissButton = {
                TextButton(onClick = { rejectionDialogPaymentId = null }) { Text("Cancelar") }
            },
            title = { Text("Rejeitar Pagamento", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Indique o motivo para notificar a utilizadora:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rejectionReasonInput,
                        onValueChange = { rejectionReasonInput = it },
                        placeholder = { Text("Ex.: Talão ilegível, data antiga ou valor divergente.", color = TextMuted) },
                        textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF1F1A1B),
                            unfocusedTextColor = Color(0xFF1F1A1B),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.error,
                            unfocusedBorderColor = OutlineSoft
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        )
    }

    // Proof Zoom Dialog
    if (selectedProofZoomUri != null) {
        AlertDialog(
            onDismissRequest = { selectedProofZoomUri = null },
            confirmButton = {
                TextButton(onClick = { selectedProofZoomUri = null }) { Text("Fechar") }
            },
            title = { Text("Comprovativo Multicaixa", fontWeight = FontWeight.Bold) },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CreamSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = selectedProofZoomUri,
                        contentDescription = "Comprovativo em ecrã inteiro",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    color: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = color))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = color))
        }
    }
}

@Composable
fun AdminPaymentItem(
    payment: PaymentReceipt,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onZoomProof: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = payment.nomeUtilizadora,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                    Text(
                        text = "Telemóvel: ${payment.telemovel}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
                Surface(
                    color = when (payment.estado) {
                        PaymentStatus.APROVADO -> MintContainer
                        PaymentStatus.PENDENTE -> HoneyContainer
                        PaymentStatus.REJEITADO -> Color(0xFFFFEBEE)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = payment.estado.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (payment.estado) {
                                PaymentStatus.APROVADO -> MintOnContainer
                                PaymentStatus.PENDENTE -> HoneyOnContainer
                                PaymentStatus.REJEITADO -> Color(0xFFC62828)
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Método: ${payment.metodo} • Ref: ${payment.referencia}", style = MaterialTheme.typography.bodySmall)
            Text("Valor: ${payment.valorKz} Kz • Data: ${payment.dataPagamento}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))

            if (payment.comprovativoUri != null) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onZoomProof,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Foto do Comprovativo")
                }
            }

            if (payment.estado == PaymentStatus.PENDENTE) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = MintSecondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aprovar (+30d)")
                    }

                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rejeitar")
                    }
                }
            }
        }
    }
}
