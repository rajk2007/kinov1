package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rajk2007.kino.ui.theme.KinoColors

@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black)
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(KinoColors.Red, KinoColors.Purple, KinoColors.Black)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(KinoColors.Elevated),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "RK", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "Raj Karmakar",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = KinoColors.Gold, modifier = Modifier.size(16.dp))
                    Text(text = "Premium Member", color = KinoColors.Gold, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            ProfileSectionTitle("Playback")
            ProfileSettingRow("Default Quality", "Auto")
            ProfileSettingRow("Default Audio", "Hindi")

            ProfileSectionTitle("Theme")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeTile("Cinematic Red", KinoColors.Red, true)
                ThemeTile("Midnight Blue", Color(0xFF1A237E), false)
            }

            ProfileSectionTitle("Content Sources")
            ProfileSettingRow("Mega Repository", "megarepo")
            ProfileSettingRow("CloudStream Providers", "cspr")

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Elevated),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Clear Cache", color = Color.White)
            }
            
            Text(
                text = "KINO v1.0.0 by Raj Karmakar",
                color = KinoColors.Muted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 24.dp).align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        color = KinoColors.Muted,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
    )
}

@Composable
fun ProfileSettingRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White)
        Text(text = value, color = KinoColors.Red)
    }
}

@Composable
fun ThemeTile(label: String, color: Color, isSelected: Boolean) {
    Surface(
        modifier = Modifier.width(120.dp).height(60.dp),
        color = KinoColors.Elevated,
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(2.dp, KinoColors.Red) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, color = Color.White, fontSize = 12.sp)
        }
    }
}
