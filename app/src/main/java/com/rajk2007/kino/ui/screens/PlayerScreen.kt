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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.rajk2007.kino.cloudstream.KinoPluginManager

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PlayerScreen(
    navController: NavController,
    type: String,
    id: String,
    title: String = ""
) {
    val context = LocalContext.current
    val tmdbId = id.toIntOrNull() ?: 0

    var isLoading by remember { mutableStateOf(true) }
    var streamUrl by remember { mutableStateOf("") }
    var useExoPlayer by remember { mutableStateOf(false) }
    var sourceIndex by remember { mutableStateOf(0) }
    var statusText by remember { mutableStateOf("Finding best stream...") }

    val webSources = remember(id, type) {
        val mt = if (type == "movie") "movie" else "tv"
        listOf(
            "https://vidsrc.to/embed/$mt/$tmdbId",
            "https://autoembed.co/embed/tmdb/$mt-$tmdbId",
            "https://embed.su/embed/$mt/$tmdbId",
            "https://player.smashy.stream/$mt/$tmdbId",
            "https://multiembed.mov/?video_id=$tmdbId&tmdb=1"
        )
    }

    LaunchedEffect(id) {
        isLoading = true
        val count = KinoPluginManager.getProviderCount()
        statusText = if (count > 0) "Searching $count providers..." else "Loading stream..."

        try {
            if (count > 0) {
                val streams = KinoPluginManager.getStreams(
                    title = title,
                    tmdbId = tmdbId,
                    type = type
                )
                if (streams.isNotEmpty()) {
                    streamUrl = streams.first().url
                    useExoPlayer = streams.first().isM3u8 ||
                        streams.first().url.contains(".m3u8") ||
                        streams.first().url.contains(".mp4")
                    statusText = "Playing via ${streams.first().name}"
                    isLoading = false
                    return@LaunchedEffect
                }
            }
        } catch (e: Exception) { }

        streamUrl = webSources[0]
        useExoPlayer = false
        statusText = "Loading stream..."
        isLoading = false
    }

    BackHandler { navController.popBackStack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFE50914),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = statusText,
                    color = Color(0xFF8A8A8A),
                    fontSize = 13.sp
                )
            }
        } else if (streamUrl.isNotEmpty()) {
            if (useExoPlayer) {
                val player = remember {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(streamUrl))
                        prepare()
                        playWhenReady = true
                    }
                }
                DisposableEffect(Unit) {
                    onDispose { player.release() }
                }
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
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
                                databaseEnabled = true
                                userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.6099.230 Mobile Safari/537.36"
                            }
                            webChromeClient = WebChromeClient()
                            webViewClient = WebViewClient()
                            loadUrl(streamUrl)
                        }
                    },
                    update = { wv ->
                        if (wv.url != streamUrl) wv.loadUrl(streamUrl)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 16.dp)
                .size(48.dp)
                .background(Color(0x99000000), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        if (!isLoading && !useExoPlayer) {
            Button(
                onClick = {
                    sourceIndex = (sourceIndex + 1) % webSources.size
                    streamUrl = webSources[sourceIndex]
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xCC000000)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Try Source ${sourceIndex + 1}/${webSources.size}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        if (!isLoading) {
            Text(
                text = if (useExoPlayer) "● LIVE" else "WEB",
                color = if (useExoPlayer) Color(0xFF00BFA5) else Color(0xFF8A8A8A),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 56.dp, end = 16.dp)
                    .background(Color(0x99000000), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
