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
        type: String = "movie",
        season: Int? = null,
        episode: Int? = null
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
                    is TvSeriesLoadResponse -> {
                        if (season != null && episode != null) {
                            loadResponse.episodes.find { it.season == season && it.episode == episode }?.data ?: loadResponse.episodes.firstOrNull()?.data
                        } else {
                            loadResponse.episodes.firstOrNull()?.data
                        } ?: first.url
                    }
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
                Log.e(TAG, "Provider ${provider.name} error", e)
            }
        }

        allLinks.sortedWith(
            compareByDescending<ExtractorLink> { CloudStreamUtils.isHindi(it) }
                .thenByDescending { it.quality }
        )
    }
}
