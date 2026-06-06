package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rajk2007.kino.data.network.TmdbClient
import com.rajk2007.kino.data.network.TmdbMedia
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<TmdbMedia>>(emptyList()) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            delay(500) // Debounce
            scope.launch {
                try {
                    val results = TmdbClient.api.searchMulti(searchQuery).results
                    searchResults = results
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (searchQuery.isEmpty()) {
            searchResults = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black)
            .padding(20.dp)
            .padding(bottom = 60.dp)
    ) {
        Text(
            text = "Search",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Movies, TV Shows, Actors...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KinoColors.Red,
                unfocusedBorderColor = KinoColors.Surface,
                focusedContainerColor = KinoColors.Surface,
                unfocusedContainerColor = KinoColors.Surface,
                cursorColor = KinoColors.Red,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        if (searchResults.isEmpty() && searchQuery.isEmpty()) {
            TrendingSearchesSection { searchQuery = it }
        } else {
            SearchResultsGrid(searchResults, navController)
        }
    }
}

@Composable
fun TrendingSearchesSection(onSearchClick: (String) -> Unit) {
    val trendingSearches = listOf("Jawan", "One Piece", "Oppenheimer", "Attack on Titan", "RRR", "The Boys")
    
    Column {
        Text(
            text = "Trending Searches",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(15.dp))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            trendingSearches.forEach { search ->
                Surface(
                    color = KinoColors.Surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { onSearchClick(search) }
                ) {
                    Text(
                        text = search,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultsGrid(results: List<TmdbMedia>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(results) { item ->
            SearchMediaCard(item, navController)
        }
    }
}

@Composable
fun SearchMediaCard(item: TmdbMedia, navController: NavController) {
    val posterUrl = "https://image.tmdb.org/t/p/w342${item.posterPath}"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("details/${item.mediaType ?: "movie"}/${item.id.toString()}") }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
