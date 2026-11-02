package com.example.ui.screens.scan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.FacialAnalysisResult
import com.example.ui.components.BigButton
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.components.PeleLindaTopBar
import com.example.ui.components.ScoreProgressBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel

@Composable
fun FacialScanScreen(viewModel: PeleLindaViewModel) {
    val isScanning by viewModel.isScanning.collectAsState()
    val latestResult by viewModel.latestScanResult.collectAsState()
    val previousScans by viewModel.facialScans.collectAsState()

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            capturedImageUri = uri
            viewModel.performFacialScan(uri.toString())
        }
    }

    Scaffold(
        topBar = {
            PeleLindaTopBar(
                title = "Scan Facial",
                subtitle = "Análise inteligente de pele e diagnóstico"
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
            // Scanner Viewfinder Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(listOf(RosePrimary, MintSecondary))
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("facial_scan_camera_card")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(CircleShape)
                                .background(CreamSurfaceVariant)
                                .border(3.dp, RosePrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (capturedImageUri != null) {
                                AsyncImage(
                                    model = capturedImageUri,
                                    contentDescription = "Foto capturada do rosto",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Face,
                                        contentDescription = null,
                                        tint = RosePrimary,
                                        modifier = Modifier.size(72.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Posicione o seu rosto",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                    )
                                }
                            }

                            if (isScanning) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(170.dp),
                                    color = MintSecondary,
                                    strokeWidth = 4.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isScanning) "A analisar acne, manchas e oleosidade..." else "Tire uma selfie com boa iluminação",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Lave o rosto e fique de frente para a luz do dia para uma estimativa mais precisa.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !isScanning,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("open_camera_picker_button")
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Carregar Foto")
                            }

                            OutlinedButton(
                                onClick = {
                                    capturedImageUri = null
                                    viewModel.performFacialScan(null)
                                },
                                shape = RoundedCornerShape(16.dp),
                                enabled = !isScanning,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("simulate_instant_scan_button")
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Scan Rápido")
                            }
                        }
                    }
                }
            }

            // Fixed Medical Disclaimer
            item {
                MedicalDisclaimerBanner()
            }

            // Analysis Result Presentation
            val activeResult = latestResult ?: previousScans.firstOrNull()
            if (activeResult != null) {
                item {
                    ScanDetailCard(result = activeResult)
                }
            }
        }
    }
}

@Composable
fun ScanDetailCard(result: FacialAnalysisResult) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("scan_results_detail_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Resultado da Análise Facial",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                    Text(
                        text = result.tipoPeleDetectada,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = RosePrimary
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(RoseContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${result.overallScore}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = RosePrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = result.resumoDescritivo,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextDark,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Scores for each condition requested by user:
            // Acne, Borbulhas/Cravos, Manchas, Olheiras, Oleosidade, Ressecamento e Poros Dilatados
            ScoreProgressBar("Acne & Borbulhas Ativas", result.acneScore, if (result.acneScore > 50) "Moderada" else "Baixa", RosePrimary)
            Spacer(modifier = Modifier.height(10.dp))

            ScoreProgressBar("Cravos (Pontos Negros)", result.cravosScore, if (result.cravosScore > 50) "Zona T" else "Poucos", HoneyTertiary)
            Spacer(modifier = Modifier.height(10.dp))

            ScoreProgressBar("Manchas & Hiperpigmentação", result.manchasScore, if (result.manchasScore > 50) "Marcas pós-acne" else "Leves", RosePrimary)
            Spacer(modifier = Modifier.height(10.dp))

            ScoreProgressBar("Olheiras & Inchaço", result.olheirasScore, if (result.olheirasScore > 50) "Cansaço aparente" else "Suaves", Color(0xFF8E24AA))
            Spacer(modifier = Modifier.height(10.dp))

            ScoreProgressBar("Oleosidade Cutânea", result.oleosidadeScore, if (result.oleosidadeScore > 50) "Alta na testa/nariz" else "Controlada", MintSecondary)
            Spacer(modifier = Modifier.height(10.dp))

            ScoreProgressBar("Poros Dilatados", result.porosDilatadosScore, if (result.porosDilatadosScore > 50) "Abertos" else "Refinados", Color(0xFF3F51B5))
            Spacer(modifier = Modifier.height(10.dp))

            ScoreProgressBar("Ressecamento / Descamação", result.ressecamentoScore, if (result.ressecamentoScore > 50) "Pele repuxando" else "Boa barreira", Color(0xFF00897B))

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MintContainer),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Recommend, contentDescription = null, tint = MintSecondary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Recomendação Personalizada: ${result.recomendacaoPrincipal}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MintOnContainer,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
