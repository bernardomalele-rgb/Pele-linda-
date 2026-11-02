package com.example.ui.screens.guide

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SkincareData
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.components.PeleLindaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel

@Composable
fun GuideScreen(viewModel: PeleLindaViewModel) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val shoppingItems by viewModel.shoppingList.collectAsState()

    val totalShoppingKz = remember(shoppingItems) {
        shoppingItems.filter { !it.comprado }.sumOf { it.precoEstimadoKz }
    }

    Scaffold(
        topBar = {
            PeleLindaTopBar(
                title = "Guia & Dicas",
                subtitle = "Educação facial, segurança e produtos económicos"
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
            // Category Selector Tabs
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedSection,
                    containerColor = Color.Transparent,
                    contentColor = RosePrimary,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                            color = RosePrimary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedSection == 0,
                        onClick = { selectedSection = 0 },
                        text = { Text("Lista Compras (${shoppingItems.size})", fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedSection == 1,
                        onClick = { selectedSection = 1 },
                        text = { Text("Pele por Fase", fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedSection == 2,
                        onClick = { selectedSection = 2 },
                        text = { Text("Alertas Perigo", fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedSection == 3,
                        onClick = { selectedSection = 3 },
                        text = { Text("Produtos Básicos", fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedSection == 4,
                        onClick = { selectedSection = 4 },
                        text = { Text("Mini-Guia & Mitos", fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            when (selectedSection) {
                0 -> {
                    // Shopping List Section
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Estimado em Kz",
                                            style = MaterialTheme.typography.labelMedium.copy(color = TextMuted)
                                        )
                                        Text(
                                            text = "~$totalShoppingKz Kz",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = RosePrimary
                                            )
                                        )
                                    }

                                    Button(
                                        onClick = { viewModel.repository.clearPurchasedShoppingItems() },
                                        colors = ButtonDefaults.buttonColors(containerColor = MintSecondary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Limpar Comprados", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    if (shoppingItems.isEmpty()) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CreamSurfaceVariant),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("🛒", fontSize = 32.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "A sua lista de compras está vazia",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Toque em 'Adicionar à Lista' em qualquer receita caseira ou produto para reunir os ingredientes aqui.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                    )
                                }
                            }
                        }
                    } else {
                        items(shoppingItems) { item ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (item.comprado) CreamSurfaceVariant else Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.repository.toggleShoppingItem(item.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = item.comprado,
                                        onCheckedChange = { viewModel.repository.toggleShoppingItem(item.id) },
                                        colors = CheckboxDefaults.colors(checkedColor = MintSecondary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.nome,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (item.comprado) TextMuted else TextDark,
                                                textDecoration = if (item.comprado) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                            )
                                        )
                                        if (item.origemReceita.isNotEmpty()) {
                                            Text(
                                                text = item.origemReceita,
                                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "~${item.precoEstimadoKz} Kz",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = RosePrimary
                                        )
                                    )
                                    IconButton(
                                        onClick = { viewModel.repository.removeShoppingItem(item.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remover", tint = TextMuted, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Skin by Phase (Adolescence, Pregnancy, Menstrual Cycle, Mature Skin)
                    items(SkincareData.pelePorFase) { phase ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Surface(
                                    color = RoseContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = phase.faseNome,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = RoseOnContainer
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = phase.subtitulo,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextDark
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = phase.caracteristicaPele,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Dicas Práticas Essenciais:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MintSecondary
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                phase.dicasPraticas.forEach { dica ->
                                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                        Text("✓ ", color = MintSecondary, fontWeight = FontWeight.Bold)
                                        Text(dica, style = MaterialTheme.typography.bodySmall, color = TextDark)
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Safety Alerts & When to See a Doctor
                    item {
                        Text(
                            text = "O que NUNCA Misturar ou Passar no Rosto:",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }

                    items(SkincareData.alertasDeSeguranca) { alert ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F2)),
                            shape = RoundedCornerShape(18.dp),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFCDD2))
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Dangerous, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = alert.tituloPerigo,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFB71C1C)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Por que faz mal: ${alert.porqueEPerigoso}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Alternativa segura: ${alert.oQueFazerEmVezDisso}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MintSecondary
                                    )
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Quando Procurar um Dermatologista:",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = RosePrimary
                            )
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(18.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SkincareData.quandoProcurarMedico.forEach { sinal ->
                                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Text("⚠️ ", fontSize = 14.sp)
                                        Text(sinal, style = MaterialTheme.typography.bodySmall, color = TextDark)
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // Simple & Affordable Basic Products (The app doesn't sell anything!)
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MintContainer),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MintSecondary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "O app Pele Linda não vende produtos. Recomendamos apenas opções simples de farmácia acessíveis a qualquer bolso em Angola.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MintOnContainer)
                                )
                            }
                        }
                    }

                    items(SkincareData.produtosSimplesAcessiveis) { prod ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(18.dp),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(OutlineSoft)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = prod.nome,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        color = RoseContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = prod.faixaPrecoKz,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = RoseOnContainer
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = prod.tipo,
                                    style = MaterialTheme.typography.labelSmall.copy(color = MintSecondary, fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Indicação: ${prod.indicadoPara}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Como usar: ${prod.comoUsar}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Onde encontrar: ${prod.ondeEncontrar}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = {
                                        viewModel.repository.addCustomShoppingItem(prod.nome, prod.precoEstimadoKz)
                                        viewModel.showSnackbar("${prod.nome} adicionado à Lista de Compras!")
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Adicionar à Lista de Compras (~${prod.precoEstimadoKz} Kz)")
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // Educational Mini-Guide & Myths
                    items(SkincareData.miniGuiaEducativo) { guide ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = guide.topico,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = RosePrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = guide.pergunta,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextDark
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = guide.explicacaoSimples,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextDark,
                                        lineHeight = 18.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = MintContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = guide.mitoOuVerdade,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = MintOnContainer
                                        ),
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                MedicalDisclaimerBanner()
            }
        }
    }
}
