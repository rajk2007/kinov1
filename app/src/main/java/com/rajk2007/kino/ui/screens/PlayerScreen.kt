package com.rajk2007.kino.ui.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.rajk2007.kino.provider.StreamEngine

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PlayerScreen(
    navController: NavController,
    type: String,
    id: String
) {
    val tmdbId = id.toIntOrNull() ?: 0
    var streamUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        isLoading = true
        try {
            val streams = StreamEngine.getStreams(
                title = "",
                year = 2024,
                tmdbId = tmdbId,
                type = type
            )
            if (streams.isNotEmpty()) {
                streamUrl = streams.first().url
            } else {
                // Fallback to vidsrc
                val mediaType = if (type == "movie") "movie" else "tv"
                streamUrl = "https://vidsrc.xyz/embed/$mediaType/$tmdbId"
            }
        } catch (e: Exception) {
            val mediaType = if (type == "movie") "movie" else "tv"
            streamUrl = "https://vidsrc.xyz/embed/$mediaType/$tmdbId"
        }
        isLoading = false
    }

    BackHandler {
        navController.popBackStack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFFE50914)
            )
        } else if (streamUrl.isNotEmpty()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            mediaPlaybackRequiresUserGesture = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            userAgentString = "Mozilla/5.0 (Linux; Android 12; Mobile) AppleWebKit/537.36 Chrome/120.0.0.0 Mobile Safari/537.36"
                        }
                        webChromeClient = WebChromeClient()
                        webViewClient = WebViewClient()
                        loadUrl(streamUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Back button always visible
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp)
                .background(
                    Color(0x99000000),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}
