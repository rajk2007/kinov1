package com.rajk2007.kino.provider

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class StreamLink(
    val url: String,
    val quality: String,
    val language: String,
    val provider: String,
    val isHindi: Boolean = false
)

object StreamEngine {

    private val client = OkHttpClient()
    
    // Hindi first priority
    suspend fun getStreams(
        title: String,
        year: Int,
        tmdbId: Int,
        type: String
    ): List<StreamLink> = withContext(Dispatchers.IO) {
        val streams = mutableListOf<StreamLink>()
        
        try {
            // Try VidSrc first (reliable free source)
            val vidsrcStreams = getVidSrcStreams(tmdbId, type)
            streams.addAll(vidsrcStreams)
        } catch (e: Exception) {
            Log.e("StreamEngine", "VidSrc failed", e)
        }
        
        try {
            // Try 2embed
            val embedStreams = get2EmbedStreams(tmdbId, type)
            streams.addAll(embedStreams)
        } catch (e: Exception) {
            Log.e("StreamEngine", "2Embed failed", e)
        }
        
        // Sort: Hindi first, then by quality
        streams.sortWith(compareByDescending<StreamLink> { it.isHindi }
            .thenByDescending { 
                when (it.quality) {
                    "1080p" -> 3
                    "720p" -> 2
                    "480p" -> 1
                    else -> 0
                }
            })
        
        streams
    }
    
    private fun getVidSrcStreams(tmdbId: Int, type: String): List<StreamLink> {
        val mediaType = if (type == "movie") "movie" else "tv"
        val url = "https://vidsrc.xyz/embed/$mediaType/$tmdbId"
        return listOf(
            StreamLink(
                url = url,
                quality = "Auto",
                language = "Multi",
                provider = "VidSrc",
                isHindi = false
            )
        )
    }
    
    private fun get2EmbedStreams(tmdbId: Int, type: String): List<StreamLink> {
        val mediaType = if (type == "movie") "movie" else "tv"
        val url = "https://www.2embed.cc/embed/$tmdbId"
        return listOf(
            StreamLink(
                url = url,
                quality = "Auto",
                language = "Multi",
                provider = "2Embed",
                isHindi = false
            )
        )
    }
}
