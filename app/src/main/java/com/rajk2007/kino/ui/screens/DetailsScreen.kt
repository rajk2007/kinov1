package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rajk2007.kino.ui.navigation.Screen
import com.rajk2007.kino.ui.theme.KinoColors

@Composable
fun DetailsScreen(navController: NavController, type: String, id: Int) {
    DetailsScreenContent(navController, type, id)
}

@Composable
fun DetailsScreenContent(navController: NavController, type: String, id: Int) {
    val scrollState = rememberScrollState()
    
    Box(modifier = Modifier.fillMaxSize().background(KinoColors.Black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Backdrop Image
            Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original/r2J0VvYm0sX7h7uR3v9Z8v4X5V0.jpg",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, KinoColors.Black),
                                startY = 300f
                            )
                        )
                )
            }
            
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Oppenheimer",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("8.9 (1.2M Reviews)", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("2023", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("3h 1m", color = Color.Gray, fontSize = 14.sp)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Red),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Watch Now", fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    FilledTonalIconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = KinoColors.Surface
                        )
                    ) {
                        Text("+", color = Color.White, fontSize = 24.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(30.dp))
                
                Text(
                    text = "Storyline",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb. A psychological thriller that follows the life of the man who changed the world forever.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                
                Spacer(modifier = Modifier.height(30.dp))
                
                Text(
                    text = "Cast",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(15.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    repeat(4) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(70.dp).clip(CircleShape).background(KinoColors.Surface))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Actor Name", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        
        // Back Button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(20.dp)
                .statusBarsPadding()
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
    }
}

@Composable
fun DetailActionItem(icon: Any, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Implementation
    }
}
