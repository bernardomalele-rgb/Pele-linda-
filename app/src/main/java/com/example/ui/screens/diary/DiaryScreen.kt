package com.example.ui.screens.diary

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DiaryEntry
import com.example.ui.components.BigButton
import com.example.ui.components.PeleLindaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel

@Composable
fun DiaryScreen(viewModel: PeleLindaViewModel) {
    val entries by viewModel.diaryEntries.collectAsState()
    val routineHistory by viewModel.routineHistory.collectAsState()
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var splitSliderPosition by remember { mutableFloatStateOf(0.5f) }

    Scaffold(
        topBar = {
            PeleLindaTopBar(
                title = "Acompanhamento",
                subtitle = "Evolução da sua pele e fotos privadas"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddEntryDialog = true },
                containerColor = RosePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("add_diary_entry_fab")
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Adicionar foto ao diário")
            }
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
            // Split-Screen Before and After Comparison Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("before_after_split_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Comparação Antes / Depois",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            )
                            Surface(
                                color = MintContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Ecrã Dividido",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MintOnContainer
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive Split View
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(CreamSurfaceVariant)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Before Side
                                Box(
                                    modifier = Modifier
                                        .weight(splitSliderPosition)
                                        .fillMaxHeight()
                                        .background(Color(0xFFFFEBEE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("ANTES", fontWeight = FontWeight.ExtraBold, color = RosePrimary, fontSize = 16.sp)
                                        Text("Há 14 dias", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                        Text("Oleosidade: Alta", style = MaterialTheme.typography.bodySmall.copy(color = TextDark))
                                        Text("Espinhas ativas", style = MaterialTheme.typography.bodySmall.copy(color = TextDark))
                                    }
                                }

                                // Divider Line
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .fillMaxHeight()
                                        .background(RosePrimary)
                                )

                                // After Side
                                Box(
                                    modifier = Modifier
                                        .weight(1f - splitSliderPosition)
                                        .fillMaxHeight()
                                        .background(Color(0xFFE8F5E9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("DEPOIS", fontWeight = FontWeight.ExtraBold, color = MintSecondary, fontSize = 16.sp)
                                        Text("Hoje", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                                        Text("Pele macia e lisa", style = MaterialTheme.typography.bodySmall.copy(color = TextDark))
                                        Text("Poros reduzidos", style = MaterialTheme.typography.bodySmall.copy(color = TextDark))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Antes", style = MaterialTheme.typography.labelSmall)
                            Slider(
                                value = splitSliderPosition,
                                onValueChange = { splitSliderPosition = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = RosePrimary,
                                    activeTrackColor = RosePrimary,
                                    inactiveTrackColor = MintSecondary
                                )
                            )
                            Text("Depois", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Weekly Evolution Chart (Acne, Manchas, Oleosidade)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("evolution_chart_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Gráfico Semanal de Evolução",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )
                        Text(
                            text = "Acompanhamento da redução de acne, oleosidade e manchas",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Chart visualizer (composed bars for each recorded period)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val days = listOf("Semana 1", "Semana 2", "Semana 3", "Hoje")
                            val acneValues = listOf(0.85f, 0.65f, 0.45f, 0.25f)
                            val oleoValues = listOf(0.90f, 0.70f, 0.50f, 0.35f)
                            val manchasValues = listOf(0.70f, 0.60f, 0.45f, 0.30f)

                            days.forEachIndexed { i, day ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                                        verticalAlignment = Alignment.Bottom,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Acne Bar
                                        Box(
                                            modifier = Modifier
                                                .width(10.dp)
                                                .fillMaxHeight(acneValues[i])
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(RosePrimary)
                                        )
                                        // Oleosidade Bar
                                        Box(
                                            modifier = Modifier
                                                .width(10.dp)
                                                .fillMaxHeight(oleoValues[i])
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(HoneyTertiary)
                                        )
                                        // Manchas Bar
                                        Box(
                                            modifier = Modifier
                                                .width(10.dp)
                                                .fillMaxHeight(manchasValues[i])
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(MintSecondary)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextDark
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Chart Legend
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LegendItem(RosePrimary, "Acne / Borbulhas")
                            Spacer(modifier = Modifier.width(12.dp))
                            LegendItem(HoneyTertiary, "Oleosidade")
                            Spacer(modifier = Modifier.width(12.dp))
                            LegendItem(MintSecondary, "Manchas")
                        }
                    }
                }
            }

            // Routine History Section (Stored in Room Database)
            if (routineHistory.isNotEmpty()) {
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintSecondary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Histórico de Rotinas (Offline)",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )
                                    )
                                }
                                Surface(
                                    color = MintContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${routineHistory.size} registos",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MintOnContainer
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            routineHistory.take(5).forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.taskTitle,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = TextDark)
                                        )
                                        Text(
                                            text = "${item.period} • ${item.dateString}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "Concluído",
                                        tint = MintSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Diary Entries Header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Entradas no Diário",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                    Text(
                        text = "Fotos 100% privadas",
                        style = MaterialTheme.typography.bodySmall.copy(color = MintSecondary, fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Entries List
            items(entries) { entry ->
                DiaryEntryCard(entry = entry)
            }
        }
    }

    // Modal to add new diary entry
    if (showAddEntryDialog) {
        AddDiaryModal(
            onDismiss = { showAddEntryDialog = false },
            onSave = { photoUri, nota, obs, acne, oleo, manchas ->
                viewModel.addDiaryEntry(photoUri, nota, obs, acne, oleo, manchas)
                showAddEntryDialog = false
            }
        )
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 11.sp))
    }
}

@Composable
fun DiaryEntryCard(entry: DiaryEntry) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(OutlineSoft)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RoseContainer),
                contentAlignment = Alignment.Center
            ) {
                if (entry.photoUri != null) {
                    AsyncImage(
                        model = entry.photoUri,
                        contentDescription = "Foto privada do diário",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("🌸", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = entry.dataFormatada,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted)
                    )
                    Text(
                        text = "Nota: ${entry.notaPele}/10",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = RosePrimary)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.observacoes,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                )
            }
        }
    }
}

@Composable
fun AddDiaryModal(
    onDismiss: () -> Unit,
    onSave: (String?, Int, String, Int, Int, Int) -> Unit
) {
    var nota by remember { mutableIntStateOf(8) }
    var observacoes by remember { mutableStateOf("") }
    var acne by remember { mutableIntStateOf(2) }
    var oleosidade by remember { mutableIntStateOf(2) }
    var manchas by remember { mutableIntStateOf(2) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        photoUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onSave(photoUri?.toString(), nota, observacoes.ifEmpty { "Pele bem cuidada hoje." }, acne, oleosidade, manchas)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
            ) {
                Text("Guardar no Diário")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextDark) }
        },
        title = {
            Text("Novo Registo de Pele", fontWeight = FontWeight.Bold, color = RosePrimary)
        },
        text = {
            Column(modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                Text(
                    text = "Como sente a sua pele hoje?",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintSecondary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (photoUri != null) "Foto Selecionada ✓" else "Anexar Foto Privada")
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Nota Geral da Pele: $nota / 10", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Slider(
                    value = nota.toFloat(),
                    onValueChange = { nota = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(thumbColor = RosePrimary, activeTrackColor = RosePrimary)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = observacoes,
                    onValueChange = { observacoes = it },
                    label = { Text("Anotações de hoje", color = TextDark) },
                    placeholder = { Text("Ex.: Menos cravos após a máscara de babosa...", color = TextMuted) },
                    textStyle = TextStyle(color = Color(0xFF1F1A1B), fontSize = 14.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1F1A1B),
                        unfocusedTextColor = Color(0xFF1F1A1B),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = RosePrimary,
                        unfocusedBorderColor = OutlineSoft
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    )
}
