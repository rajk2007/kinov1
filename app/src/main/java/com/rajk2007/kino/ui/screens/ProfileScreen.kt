package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rajk2007.kino.ui.theme.KinoColors

@Composable
fun ProfileScreen(navController: NavController) {
    ProfileScreenContent(navController)
}

@Composable
fun ProfileScreenContent(navController: NavController) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Background)
            .verticalScroll(scrollState)
            .padding(bottom = 100.dp)
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(KinoColors.Primary, KinoColors.Background)
                        )
                    )
            )
            
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = KinoColors.Surface
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(20.dp),
                        tint = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(15.dp))
                
                Text(
                    text = "Raj Kushwaha",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Surface(
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Premium Member",
                        color = KinoColors.Primary,
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            ProfileSectionTitle("Playback Settings")
            ProfileSettingRow(Icons.Default.Settings, "Video Quality", "Auto (4K)")
            ProfileSettingRow(Icons.Default.Info, "Subtitles", "English")
            
            Spacer(modifier = Modifier.height(30.dp))
            
            ProfileSectionTitle("Appearance")
            ProfileSettingRow(Icons.Default.Build, "Theme", "Dark Mode")
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ThemeTile("Midnight", KinoColors.Background, true)
                ThemeTile("Ocean", Color(0xFF0D1B2A), false)
                ThemeTile("Forest", Color(0xFF1B2E1B), false)
            }
            
            Spacer(modifier = Modifier.height(30.dp))
            
            ProfileSectionTitle("Content & Privacy")
            ProfileSettingRow(Icons.Default.Lock, "Parental Controls", "Off")
            ProfileSettingRow(Icons.Default.Share, "Connected Devices", "3 Devices")
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Clear Cache (1.2 GB)", color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "Kino v1.0.4 (Production)",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 15.dp)
    )
}

@Composable
fun ProfileSettingRow(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp)
        }
        Text(text = value, color = KinoColors.Primary, fontSize = 14.sp)
    }
}

@Composable
fun ThemeTile(name: String, color: Color, selected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color)
                .then(
                    if (selected) Modifier.background(Color.White.copy(alpha = 0.1f)) else Modifier
                )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = name, color = if (selected) Color.White else Color.Gray, fontSize = 12.sp)
    }
}
