package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rajk2007.kino.ui.theme.KinoColors

@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val trendingSearches = listOf("Jawan", "One Piece", "Oppenheimer", "Attack on Titan", "RRR", "The Boys")
    val categories = listOf("Action", "Romance", "Anime", "Thriller", "Comedy", "Sci-Fi")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KinoColors.Black)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            placeholder = { Text("Movies, shows, anime...", color = KinoColors.Muted) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = KinoColors.Elevated,
                unfocusedContainerColor = KinoColors.Elevated,
                focusedBorderColor = KinoColors.Red,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = KinoColors.Red,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        Text(
            text = "Trending Searches",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            trendingSearches.forEach { search ->
                Surface(
                    onClick = { searchQuery = search },
                    color = KinoColors.Elevated,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = search,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Text(
            text = "Browse Categories",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val chunkedCategories = categories.chunked(2)
            items(chunkedCategories) { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { category ->
                        Surface(
                            modifier = Modifier.weight(1f).height(60.dp),
                            color = KinoColors.Elevated,
                            shape = RoundedCornerShape(12.dp),
                            onClick = { }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = category, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
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
