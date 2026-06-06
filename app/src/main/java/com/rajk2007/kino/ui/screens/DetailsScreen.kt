package com.rajk2007.kino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rajk2007.kino.data.network.TmdbClient
import com.rajk2007.kino.data.network.MovieDetailResponse
import com.rajk2007.kino.data.network.TvDetailResponse
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(navController: NavController, type: String, id: Int) {
    val scope = rememberCoroutineScope()
    var movieDetails by remember { mutableStateOf<MovieDetailResponse?>(null) }
    var tvDetails by remember { mutableStateOf<TvDetailResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        scope.launch {
            try {
                if (type == "movie") {
                    movieDetails = TmdbClient.api.getMovieDetails(id)
                } else {
                    tvDetails = TmdbClient.api.getTvDetails(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(KinoColors.Black), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = KinoColors.Red)
        }
    } else {
        val title = movieDetails?.title ?: tvDetails?.name ?: ""
        val backdropPath = movieDetails?.backdropPath ?: tvDetails?.backdropPath
        val posterPath = movieDetails?.posterPath ?: tvDetails?.posterPath
        val overview = movieDetails?.overview ?: tvDetails?.overview ?: ""
        val rating = movieDetails?.voteAverage ?: tvDetails?.voteAverage ?: 0.0
        val releaseDate = movieDetails?.releaseDate ?: tvDetails?.firstAirDate ?: ""
        val runtime = movieDetails?.runtime?.let { "${it}m" } ?: tvDetails?.runtime?.firstOrNull()?.let { "${it}m" } ?: ""
        val genres = movieDetails?.genres?.joinToString(", ") { it.name } ?: tvDetails?.genres?.joinToString(", ") { it.name } ?: ""
        val cast = movieDetails?.credits?.cast ?: tvDetails?.credits?.cast ?: emptyList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KinoColors.Black)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/w1280$backdropPath")
                        .crossfade(true)
                        .build(),
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
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
                    Text(text = " ${String.format("%.1f", rating)}  •  $releaseDate  •  $runtime", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = genres, color = KinoColors.Red, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        navController.navigate("player/$type/${id.toString()}")
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KinoColors.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Watch Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Storyline", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = overview, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(24.dp))
                if (cast.isNotEmpty()) {
                    Text(text = "Cast", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(cast.take(10)) { person ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://image.tmdb.org/t/p/w185${person.profilePath}")
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = person.name, color = Color.White, fontSize = 12.sp, maxLines = 2, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}
