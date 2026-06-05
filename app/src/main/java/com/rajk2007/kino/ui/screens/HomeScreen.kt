package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import com.rajk2007.kino.data.network.TmdbApi
import com.rajk2007.kino.data.network.TmdbMedia
import com.rajk2007.kino.ui.navigation.Screen
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("Trending") }
    val categories = listOf("Trending", "Movies", "Series", "Anime", "Hindi", "English", "Tamil", "Telugu", "Korean", "Japanese")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "KINO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(
                    onClick = { navController.navigate(Screen.Search.route) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(KinoColors.Elevated, CircleShape)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.background(KinoColors.Elevated, CircleShape)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                }
            }
        }

        // Category Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory == category
                Surface(
                    onClick = { selectedCategory = category },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) KinoColors.Red else KinoColors.Elevated,
                    modifier = Modifier.height(36.dp)
                ) {
                    Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                        Text(text = category, color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }

        // Content
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { HeroSection(navController) }
            
            if (selectedCategory == "Trending") {
                item { ContinueWatchingSection(navController) }
                item { ContentRow(navController, "Trending Now") }
                item { ContentRow(navController, "Trending Movies") }
                item { ContentRow(navController, "Trending TV") }
            } else {
                item { ContentRow(navController, "$selectedCategory Popular") }
                item { ContentRow(navController, "$selectedCategory Top Rated") }
            }
        }
    }
}

@Composable
fun HeroSection(navController: NavController) {
    // Placeholder Hero Content
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
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
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Surface(
                color = KinoColors.Red,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "SERIES",
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Text(
                text = "Cape Fear",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "2026 • 5.3 • Drama • Crime",
                color = KinoColors.Muted,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Row(modifier = Modifier.padding(top = 12.dp)) {
                Button(
                    onClick = { navController.navigate(Screen.Player.createRoute("tv", 277439)) },
                    colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Red),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Watch Now")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Watchlist", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ContinueWatchingSection(navController: NavController) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = "Continue Watching",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val items = listOf(
                Triple("Jawan", 0.65f, "https://image.tmdb.org/t/p/w500/aTovumsNlDjof7YVoU5nW2RHaYn.jpg"),
                Triple("One Piece", 0.30f, "https://image.tmdb.org/t/p/w500/wPN9TvXjMu3JZpf9ZuJEW80Bsbu.jpg"),
                Triple("The Boys", 0.80f, "https://image.tmdb.org/t/p/w500/mGVrXeIjyecj6TKmwPVpHlscEmw.jpg")
            )
            items(items) { (title, progress, img) ->
                Column(modifier = Modifier.width(160.dp)) {
                    Box(modifier = Modifier.height(100.dp).clip(RoundedCornerShape(8.dp))) {
                        AsyncImage(model = img, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(4.dp).align(Alignment.BottomCenter),
                            color = KinoColors.Red,
                            trackColor = Color.DarkGray
                        )
                    }
                    Text(text = title, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                    Text(text = "Resume", color = KinoColors.Muted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ContentRow(navController: NavController, title: String) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(8) {
                MediaCard(navController)
            }
        }
    }
}

@Composable
fun MediaCard(navController: NavController) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(KinoColors.Card)
            .clickable { navController.navigate(Screen.Details.createRoute("movie", 1083381)) }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w342/rhGx6E3qRNMgj3i5su2oukNHwIQ.jpg",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Rating Badge
        Surface(
            color = KinoColors.Gold,
            shape = RoundedCornerShape(bottomStart = 8.dp),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = "6.8",
                color = Color.Black,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        // HD Badge
        Surface(
            color = Color.Black.copy(alpha = 0.5f),
            shape = RoundedCornerShape(bottomEnd = 8.dp),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(
                text = "HD",
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
