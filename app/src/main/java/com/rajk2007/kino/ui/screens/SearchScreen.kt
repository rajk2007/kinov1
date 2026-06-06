package com.rajk2007.kino.ui.screens

@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.rajk2007.kino.ui.theme.KinoColors

@Composable
fun SearchScreen(navController: NavController) {
    SearchScreenContent(navController)
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreenContent(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val trendingSearches = listOf("Jawan", "One Piece", "Oppenheimer", "Attack on Titan", "RRR", "The Boys")
    
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
                cursorColor = KinoColors.Red
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(30.dp))
        
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
                    modifier = Modifier.clickable { searchQuery = search }
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
        
        Spacer(modifier = Modifier.height(30.dp))
        
        Text(
            text = "Categories",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(15.dp))
        
        val categories = listOf(
            "Action" to Color(0xFFFF5252),
            "Comedy" to Color(0xFFFFD740),
            "Drama" to Color(0xFF448AFF),
            "Horror" to Color(0xFF7C4DFF),
            "Sci-Fi" to Color(0xFF64FFDA),
            "Anime" to Color(0xFFFF4081)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { (name, color) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color.copy(alpha = 0.2f))
                        .clickable { /* TODO */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        color = color,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

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
