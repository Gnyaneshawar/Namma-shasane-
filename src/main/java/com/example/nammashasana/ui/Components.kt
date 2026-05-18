package com.example.nammashasana.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammashasana.ui.theme.Parchment
import com.example.nammashasana.ui.theme.ParchmentDark
import com.example.nammashasana.ui.theme.StoneDark

@Composable
fun ParchmentCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)) // Sharper corners for a more "cut" look
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Parchment, ParchmentDark),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .border(1.dp, Color(0xFF4E342E).copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(24.dp)
            .drawWithContent {
                drawContent()
                // Simple vignette effect for aged look
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color(0x1A000000)),
                        center = center,
                        radius = size.maxDimension / 1.5f
                    )
                )
            },
        content = content
    )
}

@Composable
fun ShasanaTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E2723), // Deep brown
            letterSpacing = 0.5.sp
        ),
        modifier = modifier
    )
}

@Composable
fun ShasanaBody(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = FontFamily.Serif,
            color = Color.Black.copy(alpha = 0.85f),
            lineHeight = 26.sp
        ),
        modifier = modifier
    )
}
