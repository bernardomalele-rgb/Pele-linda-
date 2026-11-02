package com.example.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BigButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun WelcomeScreen(viewModel: PeleLindaViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_skincare),
                    contentDescription = "Mulher com pele linda e saudável",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, CreamBackground.copy(alpha = 0.9f), CreamBackground)
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = RoseContainer,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "✨ Cuidado Natural Acessível",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RosePrimary
                        ),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "Pele Linda",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = RosePrimary,
                        fontSize = 36.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Aprenda a cuidar da sua pele gastando pouco dinheiro, com soluções caseiras, receitas naturais e produtos simples que funcionam.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextDark,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Price & Free Trial Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(RosePrimary, MintSecondary))
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pricing_banner_card")
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = MintSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "7 DIAS GRÁTIS PARA TESTAR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintSecondary
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Apenas 3.500 Kz / mês",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = RosePrimary,
                                fontSize = 24.sp
                            )
                        )
                        Text(
                            text = "Acesso completo a scan facial, receitas caseiras, diário com fotos e dicas diárias. Cancele quando quiser.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                BigButton(
                    text = "Começar 7 Dias Grátis",
                    onClick = { viewModel.navigateTo(ScreenDestination.Register) },
                    icon = Icons.Default.AutoAwesome,
                    testTag = "welcome_register_button"
                )

                Spacer(modifier = Modifier.height(14.dp))

                TextButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.AdminLogin) },
                    modifier = Modifier.testTag("admin_access_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AdminPanelSettings,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Acesso de Administração",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextMuted)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: PeleLindaViewModel) {
    val nome by viewModel.regNome.collectAsState()
    val idade by viewModel.regIdade.collectAsState()
    val telemovel by viewModel.regTelemovel.collectAsState()
    val termosAceites by viewModel.regTermosAceites.collectAsState()
    val errorMsg by viewModel.regErrorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Criar Conta Gratuita",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenDestination.Welcome) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = RoseContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = RosePrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Mensalidade: 3.500 Kz (Primeiros 7 dias grátis)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoseOnContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Conte-nos sobre si",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )
            Text(
                text = "O seu nome e idade ajudam a personalizar a sua rotina de pele económica.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { viewModel.regNome.value = it },
                label = { Text("Nome completo ou como prefere ser chamada", color = TextDark) },
                placeholder = { Text("Ex.: Maria Teresa", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RosePrimary) },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(0xFF1F1A1B),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF1F1A1B),
                    unfocusedTextColor = Color(0xFF1F1A1B),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = RosePrimary,
                    unfocusedBorderColor = OutlineSoft,
                    cursorColor = RosePrimary,
                    focusedLabelColor = RosePrimary,
                    unfocusedLabelColor = TextDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_nome_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Idade
            OutlinedTextField(
                value = idade,
                onValueChange = { if (it.length <= 3) viewModel.regIdade.value = it },
                label = { Text("A sua idade (anos)", color = TextDark) },
                placeholder = { Text("Ex.: 24", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null, tint = RosePrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(0xFF1F1A1B),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF1F1A1B),
                    unfocusedTextColor = Color(0xFF1F1A1B),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = RosePrimary,
                    unfocusedBorderColor = OutlineSoft,
                    cursorColor = RosePrimary,
                    focusedLabelColor = RosePrimary,
                    unfocusedLabelColor = TextDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_idade_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Telemóvel
            OutlinedTextField(
                value = telemovel,
                onValueChange = { if (it.length <= 13) viewModel.regTelemovel.value = it },
                label = { Text("Número de Telemóvel (Angola)", color = TextDark) },
                placeholder = { Text("Ex.: 923 456 789", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = RosePrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(0xFF1F1A1B),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF1F1A1B),
                    unfocusedTextColor = Color(0xFF1F1A1B),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = RosePrimary,
                    unfocusedBorderColor = OutlineSoft,
                    cursorColor = RosePrimary,
                    focusedLabelColor = RosePrimary,
                    unfocusedLabelColor = TextDark
                ),
                supportingText = {
                    Text("Enviaremos um código SMS para validação e segurança da sua conta.", color = TextMuted)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_telemovel_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox Termos de Privacidade Obrigatória
            Card(
                colors = CardDefaults.cardColors(containerColor = CreamSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(OutlineSoft)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.regTermosAceites.value = !termosAceites }
                    .testTag("privacy_checkbox_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = termosAceites,
                        onCheckedChange = { viewModel.regTermosAceites.value = it },
                        colors = CheckboxDefaults.colors(checkedColor = RosePrimary),
                        modifier = Modifier.testTag("privacy_checkbox")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aceito que o meu nome e número sejam usados para gerir a minha conta e os pagamentos da mensalidade.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextDark,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            if (errorMsg != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMsg ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BigButton(
                text = "Criar Conta & Verificar SMS",
                onClick = { viewModel.startRegistration() },
                enabled = termosAceites && nome.isNotBlank() && telemovel.isNotBlank(),
                icon = Icons.Default.ArrowForward,
                testTag = "reg_submit_button"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsVerificationScreen(viewModel: PeleLindaViewModel) {
    val otpInput by viewModel.smsOtpInput.collectAsState()
    val generatedCode by viewModel.generatedSmsCode.collectAsState()
    val error by viewModel.smsVerificationError.collectAsState()
    val telemovel by viewModel.regTelemovel.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verificação por SMS", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(ScreenDestination.Register) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(RoseContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Sms,
                    contentDescription = null,
                    tint = RosePrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Código de Confirmação",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enviamos um SMS com código de verificação para o número $telemovel para ativar os seus 7 dias gratuitos.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Simulated SMS Delivery Simulation Helper Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = MintContainer),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.smsOtpInput.value = generatedCode }
                    .testTag("sms_simulation_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MarkChatRead,
                        contentDescription = null,
                        tint = MintSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "SMS Recebido da Pele Linda:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MintOnContainer)
                        )
                        Text(
                            text = "O seu código é $generatedCode (Toque aqui para preencher)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MintOnContainer
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = otpInput,
                onValueChange = { if (it.length <= 6) viewModel.smsOtpInput.value = it },
                label = { Text("Código de 4 dígitos", color = TextDark) },
                placeholder = { Text("Ex.: $generatedCode", color = TextMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp,
                    color = RosePrimary
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = RosePrimary,
                    unfocusedTextColor = RosePrimary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = RosePrimary,
                    unfocusedBorderColor = OutlineSoft,
                    cursorColor = RosePrimary,
                    focusedLabelColor = RosePrimary,
                    unfocusedLabelColor = TextDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sms_otp_input")
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BigButton(
                text = "Confirmar Telemóvel",
                onClick = { viewModel.verifySmsOtp() },
                enabled = otpInput.isNotBlank(),
                icon = Icons.Default.CheckCircle,
                testTag = "verify_sms_button"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { viewModel.resendSmsOtp() },
                modifier = Modifier.testTag("resend_sms_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = RosePrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reenviar novo código SMS", color = RosePrimary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialQuizScreen(viewModel: PeleLindaViewModel) {
    var step by remember { mutableIntStateOf(1) }

    val tipoPele by viewModel.quizTipoPele.collectAsState()
    val exposicaoSol by viewModel.quizSol.collectAsState()
    val sono by viewModel.quizSono.collectAsState()
    val agua by viewModel.quizAgua.collectAsState()
    val produtos by viewModel.quizProdutos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Personalizar Rotina ($step de 5)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LinearProgressIndicator(
                progress = { step / 5f },
                color = RosePrimary,
                trackColor = RoseContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                1 -> {
                    QuizOptionGroup(
                        title = "1. Qual é o seu tipo de pele predominante?",
                        subtitle = "Se não tem a certeza, pense em como o seu rosto fica no meio da tarde.",
                        options = listOf(
                            "Oleosa" to "Muito brilho, poros abertos e cravos frequentes",
                            "Mista" to "Oleosa na zona T (testa/nariz) e normal ou seca nas bochechas",
                            "Seca" to "Sensação de repuxar, opaca e com leve descamação",
                            "Sensível" to "Fica vermelha com facilidade e arde com produtos novos",
                            "Normal" to "Equilibrada, sem excesso de brilho nem ressecamento"
                        ),
                        selected = tipoPele,
                        onSelect = { viewModel.quizTipoPele.value = it }
                    )
                }
                2 -> {
                    QuizOptionGroup(
                        title = "2. Quanta exposição solar tem durante o dia?",
                        subtitle = "O sol em Angola é forte e influencia manchas e oleosidade.",
                        options = listOf(
                            "Pouca" to "Fico quase todo o dia em locais fechados",
                            "Moderada" to "Ando na rua nos trajetos de trabalho/estudos",
                            "Muita" to "Trabalho ou passo muitas horas exposta ao sol direto"
                        ),
                        selected = exposicaoSol,
                        onSelect = { viewModel.quizSol.value = it }
                    )
                }
                3 -> {
                    QuizOptionGroup(
                        title = "3. Como é a sua rotina de sono?",
                        subtitle = "A pele repara o colágeno e reduz olheiras durante o sono profundo.",
                        options = listOf(
                            "Menos de 6h" to "Durmo pouco e acordo com sensação de cansaço",
                            "6 a 8h" to "Sono regular na maioria das noites",
                            "Mais de 8h" to "Descanso bastante e acordo com a pele renovada"
                        ),
                        selected = sono,
                        onSelect = { viewModel.quizSono.value = it }
                    )
                }
                4 -> {
                    QuizOptionGroup(
                        title = "4. Quantos litros de água bebe por dia?",
                        subtitle = "Hidratação de dentro para fora é a base para uma pele luminosa.",
                        options = listOf(
                            "Menos de 1L" to "Bebo pouca água, esqueço com frequência",
                            "1L a 2L" to "Bebo quantidade moderada ao longo do dia",
                            "Mais de 2L" to "Bebo muita água e ando sempre com garrafa"
                        ),
                        selected = agua,
                        onSelect = { viewModel.quizAgua.value = it }
                    )
                }
                5 -> {
                    QuizOptionGroup(
                        title = "5. Que produtos usa atualmente no rosto?",
                        subtitle = "Vamos adaptar receitas económicas ao que já tem.",
                        options = listOf(
                            "Nenhum / Apenas água e sabonete comum" to "Cuidado básico sem produtos específicos",
                            "Hidratante facial simples" to "Uso creme hidratante de vez em quando",
                            "Protetor solar diário" to "Já tenho o hábito de aplicar protetor solar",
                            "Tratamento para acne / manchas" to "Uso produtos específicos indicados ou caseiros"
                        ),
                        selected = produtos,
                        onSelect = { viewModel.quizProdutos.value = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { step-- },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Text("Voltar", color = TextDark)
                    }
                }

                Button(
                    onClick = {
                        if (step < 5) {
                            step++
                        } else {
                            viewModel.finishQuiz()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(2f)
                        .height(52.dp)
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        if (step < 5) "Próxima Pergunta" else "Ver Meu Plano Personalizado",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuizOptionGroup(
    title: String,
    subtitle: String,
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDark,
                fontSize = 20.sp
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
        )
        Spacer(modifier = Modifier.height(20.dp))

        options.forEach { (label, desc) ->
            val isSelected = selected.contains(label) || selected == label
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) RoseContainer else Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isSelected) RosePrimary else OutlineSoft
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onSelect(label) }
                    .testTag("quiz_option_${label.take(8)}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelect(label) },
                        colors = RadioButtonDefaults.colors(selectedColor = RosePrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) RoseOnContainer else TextDark
                            )
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSelected) RoseOnContainer.copy(alpha = 0.8f) else TextMuted
                            )
                        )
                    }
                }
            }
        }
    }
}
