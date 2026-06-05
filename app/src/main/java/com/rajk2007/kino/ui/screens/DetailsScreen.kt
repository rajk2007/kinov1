package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Backdrop with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w1280/rvMmQj3nMZbJmoW6QrzU0nq9Xky.jpg",
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
                                startY = 100f
                            )
                        )
                )
            }

            // Info Section
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-60).dp),
                verticalAlignment = Alignment.Bottom
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w342/rhGx6E3qRNMgj3i5su2oukNHwIQ.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Jawan",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "2023 • 7.2 • Action • 2h 49m",
                        color = KinoColors.Muted,
                        fontSize = 14.sp
                    )
                }
            }

            // Actions
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp)
            ) {
                Button(
                    onClick = { navController.navigate(Screen.Player.createRoute(type, id)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Watch Now", fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DetailActionItem(Icons.Default.Add, "Watchlist")
                    DetailActionItem(Icons.Default.Download, "Download")
                    DetailActionItem(Icons.Default.Share, "Share")
                }

                Text(
                    text = "A high-octane action thriller which outlines the emotional journey of a man who is set to rectify the wrongs in the society.",
                    color = KinoColors.Text,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Text(
                    text = "Cast",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(5) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w185/7mS70G9N6z4q7r7Z5Z7Z5Z7Z5Z7.jpg",
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = "Actor Name",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Back Button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
    }
}

@Composable
fun DetailActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        Text(text = label, color = KinoColors.Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
    }
}
