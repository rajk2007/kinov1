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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PlayerScreen(
    navController: NavController,
    type: String,
    id: String
) {
    var sourceIndex by remember { mutableStateOf(0) }
    val sources = remember(id, type) {
        val mediaType = if (type == "movie") "movie" else "tv"
        val tmdbId = id.toIntOrNull() ?: 0
        listOf(
            "https://vidsrc.to/embed/$mediaType/$tmdbId",
            "https://vidsrc.xyz/embed/$mediaType/$tmdbId",
            "https://www.2embed.cc/embed/$tmdbId",
            "https://multiembed.mov/?video_id=$tmdbId&tmdb=1"
        )
    }
    
    var streamUrl by remember { mutableStateOf(sources[0]) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(id, sourceIndex) {
        isLoading = true
        streamUrl = sources[sourceIndex]
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
                            allowFileAccess = true
                            allowContentAccess = true
                            databaseEnabled = true
                            cacheMode = WebSettings.LOAD_DEFAULT
                            userAgentString = "Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                            }
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                            }
                        }
                        loadUrl(streamUrl)
                    }
                },
                update = { webView ->
                    if (webView.url != streamUrl) {
                        webView.loadUrl(streamUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Back button top left
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 16.dp)
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

        // Source switcher bottom right
        TextButton(
            onClick = {
                sourceIndex = (sourceIndex + 1) % sources.size
                streamUrl = sources[sourceIndex]
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .background(Color(0x99000000), RoundedCornerShape(8.dp))
        ) {
            Text(
                "Try Another Source",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}
