package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rajk2007.kino.data.network.TmdbClient
import com.rajk2007.kino.data.network.TmdbMedia
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("Trending") }
    val scope = rememberCoroutineScope()
    
    var trendingMovies by remember { mutableStateOf<List<TmdbMedia>>(emptyList()) }
    var popularMovies by remember { mutableStateOf<List<TmdbMedia>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<TmdbMedia>>(emptyList()) }
    var heroMovie by remember { mutableStateOf<TmdbMedia?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val trending = TmdbClient.api.getTrendingAll().results
                trendingMovies = trending
                heroMovie = trending.firstOrNull()
                popularMovies = TmdbClient.api.getPopularMovies().results
                topRatedMovies = TmdbClient.api.getTopRatedMovies().results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    HomeScreenContent(
        navController = navController,
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it },
        trendingMovies = trendingMovies,
        popularMovies = popularMovies,
        topRatedMovies = topRatedMovies,
        heroMovie = heroMovie
    )
}

@Composable
fun HomeScreenContent(
    navController: NavController,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    trendingMovies: List<TmdbMedia>,
    popularMovies: List<TmdbMedia>,
    topRatedMovies: List<TmdbMedia>,
    heroMovie: TmdbMedia?
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
                IconButton(onClick = { navController.navigate("search") }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                }
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
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

        heroMovie?.let { HeroSection(it, navController) }
        
        ContinueWatchingSection()
        
        ContentRow(title = "Trending Now", items = trendingMovies, navController = navController)
        ContentRow(title = "Popular on Kino", items = popularMovies, navController = navController)
        ContentRow(title = "Top Rated", items = topRatedMovies, navController = navController)
    }
}

@Composable
fun HeroSection(item: TmdbMedia, navController: NavController) {
    val backdropUrl = "https://image.tmdb.org/t/p/w1280${item.backdropPath}"
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { navController.navigate("details/${item.mediaType ?: "movie"}/${item.id.toString()}") }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(backdropUrl)
                .crossfade(true)
                .build(),
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
                text = item.title ?: item.name ?: "",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${item.releaseDate ?: item.firstAirDate ?: ""} • Rating: ${item.voteAverage}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { navController.navigate("player/${item.mediaType ?: "movie"}/${item.id.toString()}") },
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
            items(0) { // Placeholder for now
                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(KinoColors.Surface)
                )
            }
        }
    }
}

@Composable
fun ContentRow(title: String, items: List<TmdbMedia>, navController: NavController) {
    if (items.isEmpty()) return

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
            items(items) { item ->
                MediaCard(item, navController)
            }
        }
    }
}

@Composable
fun MediaCard(item: TmdbMedia, navController: NavController) {
    val posterUrl = "https://image.tmdb.org/t/p/w342${item.posterPath}"

    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { navController.navigate("details/${item.mediaType ?: "movie"}/${item.id.toString()}") }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(KinoColors.Surface)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title ?: item.name,
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
                    text = String.format("%.1f", item.voteAverage),
                    color = Color.Yellow,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = item.title ?: item.name ?: "",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}
