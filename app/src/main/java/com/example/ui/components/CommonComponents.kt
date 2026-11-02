package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountStatus
import com.example.ui.theme.*

@Composable
fun BigButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    isSecondary: Boolean = false,
    testTag: String = "big_button"
) {
    val containerColor = if (isSecondary) MintSecondary else RosePrimary
    val contentColor = Color.White

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFE2D6DA),
            disabledContentColor = Color(0xFF9E8F94)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun PeleLindaTopBar(
    title: String,
    subtitle: String? = null,
    onAdminClick: (() -> Unit)? = null,
    onPaywallClick: (() -> Unit)? = null,
    statusText: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CreamBackground,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = RosePrimary,
                        fontSize = 22.sp
                    )
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (statusText != null && onPaywallClick != null) {
                    Surface(
                        color = RoseContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clickable { onPaywallClick() }
                            .testTag("status_badge_topbar")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = RosePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = statusText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoseOnContainer
                                )
                            )
                        }
                    }
                }

                if (onAdminClick != null) {
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("admin_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AdminPanelSettings,
                            contentDescription = "Painel de Administração",
                            tint = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MedicalDisclaimerBanner(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CreamSurfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(OutlineSoft)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("medical_disclaimer_card")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(RoseContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = RosePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Aviso Médico Importante",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = RosePrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Este resultado é uma estimativa e não substitui um dermatologista. Em casos graves de dor, infecção ou feridas, procure um médico.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextDark,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ScoreProgressBar(
    label: String,
    score: Int, // 0 a 100
    levelText: String,
    color: Color = RosePrimary,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
            )
            Text(
                text = "$levelText ($score%)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = CreamSurfaceVariant
        )
    }
}
