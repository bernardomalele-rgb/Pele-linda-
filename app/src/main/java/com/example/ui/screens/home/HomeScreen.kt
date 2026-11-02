package com.example.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountStatus
import com.example.data.model.RoutinePeriod
import com.example.data.model.RoutineTask
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.components.PeleLindaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.PeleLindaViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun HomeScreen(viewModel: PeleLindaViewModel) {
    val user by viewModel.userProfile.collectAsState()
    val waterCups by viewModel.waterCupsToday.collectAsState()
    val streakDays by viewModel.routineStreakDays.collectAsState()
    val morningTasks by viewModel.morningRoutine.collectAsState()
    val nightTasks by viewModel.nightRoutine.collectAsState()
    val daysRemaining = viewModel.repository.getDaysRemaining()

    Scaffold(
        topBar = {
            val statusLabel = when (user.estadoConta) {
                AccountStatus.EM_TESTE -> {
                    if (daysRemaining <= 1) "Último dia de teste!"
                    else "$daysRemaining dias de teste"
                }
                AccountStatus.ATIVA -> "Assinatura Ativa"
                AccountStatus.EXPIRADA -> "Teste Expirado"
                AccountStatus.BLOQUEADA -> "Bloqueada"
            }

            PeleLindaTopBar(
                title = "Pele Linda",
                subtitle = "Olá, ${user.nome.ifEmpty { "Amiga" }}!",
                statusText = statusLabel,
                onPaywallClick = { viewModel.navigateTo(ScreenDestination.SubscriptionPaywall) },
                onAdminClick = { viewModel.navigateTo(ScreenDestination.AdminLogin) }
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
            // Free Trial Countdown Alert (especially on day 6 and last day)
            if (user.estadoConta == AccountStatus.EM_TESTE) {
                item {
                    val isUrgent = daysRemaining <= 2
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUrgent) RoseContainer else MintContainer
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(ScreenDestination.SubscriptionPaywall) }
                            .testTag("trial_countdown_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isUrgent) Icons.Default.Warning else Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (isUrgent) RosePrimary else MintSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (daysRemaining <= 1) "Atenção: Último dia de teste gratuito!" else "Tens $daysRemaining dias grátis para testar tudo",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUrgent) RoseOnContainer else MintOnContainer
                                    )
                                )
                                Text(
                                    text = "Mensalidade de apenas 3.500 Kz para continuar sem interrupções.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isUrgent) RoseOnContainer.copy(alpha = 0.8f) else MintOnContainer.copy(alpha = 0.8f)
                                    )
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = if (isUrgent) RosePrimary else MintSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Streak & Motivational Message
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    Brush.linearGradient(listOf(RosePrimary, HoneyTertiary)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔥", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "$streakDays dias seguidos de rotina!",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            )
                            Text(
                                text = "A consistência diária é o segredo de uma pele radiante e saudável.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    }
                }
            }

            // UV Index & Sun Protection Alert
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CreamSurfaceVariant),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("uv_index_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = HoneyTertiary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Índice UV Diário: 8 (Muito Alto)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                )
                            }
                            Surface(
                                color = HoneyContainer,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Pico: 10h - 16h",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = HoneyOnContainer
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sol forte hoje! Aplique protetor solar FPS 30+ ou use chapéu/sombrinha ao sair na rua para evitar manchas e envelhecimento precoce.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }
            }

            // Water Tracker (Copos de Água)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("water_tracker_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = Color(0xFF29B6F6),
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Hidratação de Hoje",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                )
                            }
                            Text(
                                text = "$waterCups / 8 copos (${waterCups * 250} ml)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0288D1)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual cups indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (i in 1..8) {
                                val filled = i <= waterCups
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            if (filled) Color(0xFFE1F5FE) else CreamSurfaceVariant,
                                            CircleShape
                                        )
                                        .border(
                                            1.dp,
                                            if (filled) Color(0xFF29B6F6) else OutlineSoft,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = null,
                                        tint = if (filled) Color(0xFF0288D1) else Color(0xFFB0BEC5),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { viewModel.repository.addWaterCup() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("add_water_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+1 Copo de Água", fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.repository.resetWaterCups() },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text("Reiniciar", color = TextMuted)
                            }
                        }
                    }
                }
            }

            // Morning Skincare Routine
            item {
                Text(
                    text = "☀️ Rotina da Manhã",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
            }

            items(morningTasks) { task ->
                RoutineTaskItem(
                    task = task,
                    onToggle = { viewModel.repository.toggleMorningTask(task.id) }
                )
            }

            // Night Skincare Routine
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🌙 Rotina da Noite",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
            }

            items(nightTasks) { task ->
                RoutineTaskItem(
                    task = task,
                    onToggle = { viewModel.repository.toggleNightTask(task.id) }
                )
            }

            // Daily Skincare Tip Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MintContainer),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Dica de Ouro de Hoje",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintOnContainer
                                )
                            )
                            Text(
                                text = "Troque a fronha do seu travesseiro a cada 3 dias. O tecido acumula óleo capilar e bactérias durante a noite que provocam espinhas na lateral do rosto.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MintOnContainer.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            }

            // Medical Disclaimer
            item {
                MedicalDisclaimerBanner()
            }
        }
    }
}

@Composable
fun RoutineTaskItem(
    task: RoutineTask,
    onToggle: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (task.concluidoHoje) MintContainer.copy(alpha = 0.6f) else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (task.concluidoHoje) MintSecondary else OutlineSoft
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("routine_task_${task.id}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.concluidoHoje,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MintSecondary,
                    uncheckedColor = OutlineSoft
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.titulo,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (task.concluidoHoje) MintOnContainer else TextDark
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = task.descricao,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (task.concluidoHoje) MintOnContainer.copy(alpha = 0.8f) else TextMuted
                    )
                )
            }
            Surface(
                color = if (task.concluidoHoje) MintSecondary.copy(alpha = 0.2f) else CreamSurfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${task.duracaoMin} min",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (task.concluidoHoje) MintSecondary else TextMuted
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
