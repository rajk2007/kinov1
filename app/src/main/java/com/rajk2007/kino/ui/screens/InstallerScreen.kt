package com.rajk2007.kino.ui.screens

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
import com.rajk2007.kino.cloudstream.KinoPluginManager
import kotlinx.coroutines.launch

@Composable
fun InstallerScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var repoStates by remember {
        mutableStateOf(
            KinoPluginManager.DEFAULT_REPOS.associate { it.name to false }
        )
    }
    var allDone by remember { mutableStateOf(false) }
    var providerCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        scope.launch {
            KinoPluginManager.installAllRepos(context) { repoName, done ->
                repoStates = repoStates + (repoName to done)
                if (repoStates.values.all { it }) {
                    providerCount = KinoPluginManager.getProviderCount()
                    allDone = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080808))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "KINO",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE50914)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "by Raj Karmakar",
                fontSize = 14.sp,
                color = Color(0xFF8A8A8A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CINEMA. REDEFINED.",
                fontSize = 11.sp,
                letterSpacing = 3.sp,
                color = Color(0xFF444444)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Setting Up Content Sources",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Installing plugins for 200+ streaming sources...",
                fontSize = 13.sp,
                color = Color(0xFF8A8A8A)
            )
            Spacer(modifier = Modifier.height(32.dp))

            KinoPluginManager.DEFAULT_REPOS.forEach { repo ->
                val done = repoStates[repo.name] ?: false
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = repo.name,
                            color = if (done) Color.White else Color(0xFF8A8A8A),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = repo.shortCode,
                            color = Color(0xFF444444),
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { if (done) 1f else 0.6f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = if (done) Color(0xFF00BFA5) else Color(0xFFE50914),
                            trackColor = Color(0xFF1A1A1A)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    if (done) {
                        Text("✓", color = Color(0xFF00BFA5), fontSize = 20.sp)
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color(0xFFE50914),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

            if (allDone) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "✓ All sources ready! $providerCount providers loaded.",
                    color = Color(0xFF00BFA5),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
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
                        "Start Watching",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
