package com.rajk2007.kino.cloudstream

import android.util.Log
import com.lagradost.cloudstream3.*
import kotlinx.coroutines.*

class StreamEngine {
    private val TAG = "KinoStreamEngine"

    suspend fun getStreamsForContent(
        providers: List<MainAPI>,
        title: String,
        tmdbId: Int = 0,
        type: String = "movie"
    ): List<ExtractorLink> = withContext(Dispatchers.IO) {
        val allLinks = mutableListOf<ExtractorLink>()

        providers.forEach { provider ->
            try {
                val searchResults = provider.search(title)
                if (searchResults.isEmpty()) return@forEach

                val first = searchResults.first()
                val loadResponse = provider.load(first.url) ?: return@forEach

                val data = when (loadResponse) {
                    is MovieLoadResponse -> loadResponse.dataUrl
                    is TvSeriesLoadResponse -> loadResponse.episodes.firstOrNull()?.data ?: ""
                    else -> first.url
                }

                val links = mutableListOf<ExtractorLink>()
                provider.loadLinks(
                    data = data,
                    isCasting = false,
                    subtitleCallback = {},
                    callback = { links.add(it) }
                )
                allLinks.addAll(links)
            } catch (e: Exception) {
                Log.e(TAG, "Error with provider ${provider.name}", e)
            }
        }

        // Hindi/Dub priority
        allLinks.sortedWith(
            compareByDescending<ExtractorLink> { 
                it.name.contains("Hindi", ignoreCase = true) || 
                it.name.contains("Dub", ignoreCase = true) 
            }.thenByDescending { it.quality }
        )
    }
}
