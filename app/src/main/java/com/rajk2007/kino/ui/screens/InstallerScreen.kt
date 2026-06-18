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
import com.rajk2007.kino.plugin.PluginManager
import kotlinx.coroutines.launch

@Composable
fun InstallerScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var currentRepo by remember { mutableStateOf("") }
    var repoProgress by remember { mutableStateOf(mapOf<String, Float>()) }
    var repoDone by remember { mutableStateOf(mapOf<String, Boolean>()) }
    var allDone by remember { mutableStateOf(false) }
    var installing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            PluginManager.installDefaultRepos(context) { repoName, progress, done ->
                currentRepo = repoName
                repoProgress = repoProgress + (repoName to progress)
                repoDone = repoDone + (repoName to done)
                if (done && repoDone.size == PluginManager.DEFAULT_REPOS.size 
                    && repoDone.values.all { it }) {
                    allDone = true
                }
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

            PluginManager.DEFAULT_REPOS.forEach { repo ->
                val progress = repoProgress[repo.name] ?: 0f
                val done = repoDone[repo.name] ?: false
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = repo.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = repo.shortCode,
                                color = Color(0xFF8A8A8A),
                                fontSize = 11.sp
                            )
                        }
                        if (done) {
                            Text("✓", color = Color(0xFF00BFA5), fontSize = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFFE50914),
                        trackColor = Color(0xFF1A1A1A)
                    )
                }
            }

            if (allDone) {
                Spacer(modifier = Modifier.height(32.dp))
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
