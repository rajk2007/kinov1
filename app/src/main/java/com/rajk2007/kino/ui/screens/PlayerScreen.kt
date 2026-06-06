package com.rajk2007.kino.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rajk2007.kino.data.network.TmdbClient
import com.rajk2007.kino.ui.theme.KinoColors
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(navController: NavController, type: String, id: Int) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("Loading...") }

    LaunchedEffect(id) {
        scope.launch {
            try {
                if (type == "movie") {
                    val details = TmdbClient.api.getMovieDetails(id)
                    title = details.title
                } else {
                    val details = TmdbClient.api.getTvDetails(id)
                    title = details.name
                }
                
                // Automatically launch YouTube search for the trailer
                val intent = Intent(Intent.ACTION_VIEW, 
                    Uri.parse("https://www.youtube.com/results?search_query=${title}+trailer"))
                context.startActivity(intent)
                
                // Go back after launching
                navController.popBackStack()
            } catch (e: Exception) {
                e.printStackTrace()
                title = "Error loading trailer"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = KinoColors.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Opening trailer for $title...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
        
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
    }
}
