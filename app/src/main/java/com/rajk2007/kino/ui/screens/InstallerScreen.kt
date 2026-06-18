package com.rajk2007.kino.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kinov1.plugins.DefaultRepoInstaller
import kotlinx.coroutines.launch

@Composable
fun InstallerScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repoInstaller = remember { DefaultRepoInstaller(context) }
    
    var allDone by remember { mutableStateOf(false) }
    var installing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!installing) {
            installing = true
            scope.launch {
                repoInstaller.installDefaults()
                allDone = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080808))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "KINO",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE50914)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Setting Up Your Experience",
                fontSize = 16.sp,
                color = Color(0xFF8A8A8A)
            )
            Spacer(modifier = Modifier.height(48.dp))

            if (!allDone) {
                CircularProgressIndicator(color = Color(0xFFE50914))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Installing default plugins...",
                    color = Color.White,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = "✓ All sources ready. Welcome to Kino.",
                    color = Color(0xFF00BFA5),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("installer") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE50914)
                    )
                ) {
                    Text(
                        text = "Start Watching",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
