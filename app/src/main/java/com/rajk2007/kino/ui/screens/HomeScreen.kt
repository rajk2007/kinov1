package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
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
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("Trending") }
    
    HomeScreenContent(navController, selectedCategory) { selectedCategory = it }
}

@Composable
fun HomeScreenContent(
    navController: NavController,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hello, Raj",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "What do you want to watch today?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            Row {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", color = Color.White)
                }
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", color = Color.White)
                }
            }
        }

        // Category Pills
        val categories = listOf("Trending", "Movies", "TV Shows", "Anime", "My List")
        LazyRow(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { category ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onCategorySelected(category) },
                    color = if (selectedCategory == category) KinoColors.Red else KinoColors.Surface,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = category,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        HeroSection()
        
        ContinueWatchingSection()
        
        ContentRow(title = "Trending Now", navController = navController)
        ContentRow(title = "Popular on Kino", navController = navController)
        ContentRow(title = "New Releases", navController = navController)
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original/r2J0VvYm0sX7h7uR3v9Z8v4X5V0.jpg",
            contentDescription = "Hero Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = "Oppenheimer",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "2023 • Drama/History • 3h 1m",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Watch Now")
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                FilledTonalIconButton(
                    onClick = { /* TODO */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("+", color = Color.White, fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun ContinueWatchingSection() {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = "Continue Watching",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(KinoColors.Surface)
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500/5i6SjbC970BTnZSTUv3B8fjPXvF.jpg",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.Gray.copy(alpha = 0.5f))
                            .align(Alignment.BottomStart)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight()
                                .background(KinoColors.Red)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContentRow(title: String, navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See All",
                color = KinoColors.Red,
                fontSize = 14.sp
            )
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(5) { index ->
                MediaCard(navController)
            }
        }
    }
}

@Composable
fun MediaCard(navController: NavController) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { navController.navigate("details/movie/1") }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(KinoColors.Surface)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/8Gxv0mYmUpepXUhwlm8YvE4Sxv1.jpg",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "8.5",
                    color = Color.Yellow,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "The Dark Knight",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}
