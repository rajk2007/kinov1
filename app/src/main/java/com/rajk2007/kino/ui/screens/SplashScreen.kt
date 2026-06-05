package com.rajk2007.kino.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        alpha.animateTo(1f, animationSpec = tween(1000))
        delay(1200) // Total 3.2s roughly with animations
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "KINO",
                color = KinoColors.Red,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "by Raj Karmakar",
                color = KinoColors.Muted,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "CINEMA. REDEFINED.",
                color = KinoColors.Text,
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )
        }
    }
}
