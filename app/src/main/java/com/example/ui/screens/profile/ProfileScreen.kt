package com.example.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountStatus
import com.example.ui.components.BigButton
import com.example.ui.components.PeleLindaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun ProfileScreen(viewModel: PeleLindaViewModel) {
    val context = LocalContext.current
    val user by viewModel.userProfile.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showFaqDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PeleLindaTopBar(
                title = "Perfil & Definições",
                subtitle = "Dados da conta, suporte e privacidade"
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
            // Profile Card
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
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .background(RoseContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = RosePrimary,
                                modifier = Modifier.size(42.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = user.nome.ifEmpty { "Utilizadora Pele Linda" },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )

                        Text(
                            text = "${user.idade} anos • ${user.telemovel}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = MintContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Pele ${user.tipoPele} • ${if (user.estadoConta == AccountStatus.ATIVA) "Assinante Ativa" else "Teste Grátis"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintOnContainer
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = { showEditProfileDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Editar Dados Pessoais")
                        }
                    }
                }
            }

            // Quick Actions Group
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        // Invite friends via WhatsApp
                        SettingsRow(
                            icon = Icons.Default.Share,
                            title = "Convidar Amigas para o App",
                            subtitle = "Partilhe rotinas e receitas económicas por WhatsApp",
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Olá! Estou a usar o app 'Pele Linda' para cuidar da pele gastando pouco dinheiro com receitas caseiras de babosa, mel e produtos simples. Experimenta os 7 dias grátis!"
                                    )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Partilhar Pele Linda")
                                context.startActivity(shareIntent)
                            }
                        )

                        HorizontalDivider(color = OutlineSoft.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                        // WhatsApp Support (+244 923471505)
                        SettingsRow(
                            icon = Icons.Default.Chat,
                            title = "Falar por WhatsApp (Suporte)",
                            subtitle = "Dúvidas sobre pagamentos e ajuda (+244 923471505)",
                            onClick = {
                                try {
                                    val waIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/244923471505?text=Ola!%20Preciso%20de%20ajuda%20no%20app%20Pele%20Linda.")
                                    )
                                    context.startActivity(waIntent)
                                } catch (e: Exception) {
                                    viewModel.showSnackbar("Contacto de Suporte WhatsApp: 923471505")
                                }
                            }
                        )

                        HorizontalDivider(color = OutlineSoft.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                        // FAQ
                        SettingsRow(
                            icon = Icons.Default.HelpOutline,
                            title = "Perguntas Frequentes (FAQ)",
                            subtitle = "Como funciona o teste, cancelamento e receitas",
                            onClick = { showFaqDialog = true }
                        )

                        HorizontalDivider(color = OutlineSoft.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                        // Subscription screen shortcut
                        SettingsRow(
                            icon = Icons.Default.CardMembership,
                            title = "Gerir Mensalidade (3.500 Kz)",
                            subtitle = "Ver comprovativos, Multicaixa Express e recibos",
                            onClick = { viewModel.navigateTo(ScreenDestination.SubscriptionPaywall) }
                        )
                    }
                }
            }

            // Legal & Security
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        SettingsRow(
                            icon = Icons.Default.Policy,
                            title = "Termos de Uso & Política de Privacidade",
                            subtitle = "Proteção de dados locais e termos da mensalidade",
                            onClick = { showTermsDialog = true }
                        )

                        HorizontalDivider(color = OutlineSoft.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsRow(
                            icon = Icons.Default.AdminPanelSettings,
                            title = "Painel de Administração",
                            subtitle = "Acesso exclusivo com código de segurança",
                            onClick = { viewModel.navigateTo(ScreenDestination.AdminLogin) }
                        )

                        HorizontalDivider(color = OutlineSoft.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsRow(
                            icon = Icons.Default.DeleteForever,
                            title = "Apagar Minha Conta",
                            subtitle = "Remove todos os dados e fotos do telemóvel",
                            isDestructive = true,
                            onClick = { showDeleteAccountDialog = true }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Pele Linda v1.0 • Desenvolvido para mulheres em Angola",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var editNome by remember { mutableStateOf(user.nome) }
        var editIdade by remember { mutableStateOf(user.idade.toString()) }
        var editTelemovel by remember { mutableStateOf(user.telemovel) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val idadeInt = editIdade.toIntOrNull() ?: user.idade
                        viewModel.repository.updateProfile(editNome, idadeInt, editTelemovel)
                        viewModel.showSnackbar("Perfil atualizado!")
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Guardar Alterações")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) { Text("Cancelar") }
            },
            title = { Text("Editar Perfil", fontWeight = FontWeight.Bold, color = RosePrimary) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNome,
                        onValueChange = { editNome = it },
                        label = { Text("Nome", color = TextDark) },
                        singleLine = true,
                        textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 16.sp),
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
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editIdade,
                        onValueChange = { editIdade = it },
                        label = { Text("Idade", color = TextDark) },
                        singleLine = true,
                        textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 16.sp),
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
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editTelemovel,
                        onValueChange = { editTelemovel = it },
                        label = { Text("Telemóvel", color = TextDark) },
                        singleLine = true,
                        textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 16.sp),
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
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.repository.deleteAccount()
                        viewModel.showSnackbar("Conta e dados apagados com sucesso.")
                        showDeleteAccountDialog = false
                        viewModel.navigateTo(ScreenDestination.Welcome)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sim, Apagar Tudo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) { Text("Cancelar") }
            },
            title = { Text("Apagar Conta e Dados?", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
            text = {
                Text("Esta ação é irreversível. Todas as fotos do diário, histórico de scan facial, listas e registos serão eliminados deste telemóvel.")
            }
        )
    }

    // FAQ Dialog
    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            confirmButton = {
                TextButton(onClick = { showFaqDialog = false }) { Text("Fechar") }
            },
            title = { Text("Perguntas Frequentes", fontWeight = FontWeight.Bold, color = RosePrimary) },
            text = {
                Column(modifier = Modifier.heightIn(max = 380.dp).verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                    FaqItem("Como funciona o teste grátis?", "Tem 7 dias de acesso completo sem pagar nada. Ao final dos 7 dias, o app solicita o pagamento da mensalidade de 3.500 Kz para continuar.")
                    FaqItem("Onde pago a mensalidade?", "Por Multicaixa Express para o número 923471505 ou por referência bancária. Depois basta enviar o print do comprovativo no app.")
                    FaqItem("As minhas fotos estão seguras?", "Sim! As fotos do rosto e do diário ficam guardadas apenas na memória interna do seu telemóvel e nunca são partilhadas publicamente.")
                    FaqItem("O app vende produtos?", "Não. O Pele Linda apenas recomenda ingredientes caseiros baratos e produtos neutros de farmácia acessíveis a qualquer bolso em Angola.")
                }
            }
        )
    }

    // Terms & Privacy Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) { Text("Fechar") }
            },
            title = { Text("Termos e Privacidade", fontWeight = FontWeight.Bold, color = RosePrimary) },
            text = {
                Column(modifier = Modifier.heightIn(max = 380.dp).verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                    Text(
                        text = "1. Aceitação dos Termos\nAo utilizar o aplicativo Pele Linda, concorda com a recolha do seu nome e número de telemóvel estritamente para gestão da conta e pagamento da mensalidade de 3.500 Kz.\n\n2. Natureza Informativa e Estimativa\nOs resultados do scan facial e as receitas caseiras constituem estimativas educativas e de autocuidado. Não constituem aconselhamento médico ou prescrição dermatológica.\n\n3. Armazenamento Local Privado\nAs imagens faciais e notas de diário permanecem no armazenamento local privado do seu dispositivo.\n\n4. Renovação e Cancelamento\nA assinatura é renovada mensalmente mediante envio do comprovativo de pagamento. Não existem fidelizações forçadas.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, color = TextDark)
                    )
                }
            }
        )
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    if (isDestructive) Color(0xFFFFEBEE) else RoseContainer,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) Color(0xFFD32F2F) else RosePrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDestructive) Color(0xFFD32F2F) else TextDark
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text("• $question", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RosePrimary)
        Text(answer, fontSize = 12.sp, color = TextDark, lineHeight = 16.sp)
    }
}
