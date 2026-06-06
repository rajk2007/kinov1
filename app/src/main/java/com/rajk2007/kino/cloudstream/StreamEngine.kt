package com.rajk2007.kino.cloudstream

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class StreamEngine {
    private val TAG = "KinoStreamEngine"

    suspend fun getStreamsForContent(
        providers: List<Any>,
        title: String,
        tmdbId: Int,
        type: String,
        season: Int? = null,
        episode: Int? = null
    ): List<ExtractorLink> = withContext(Dispatchers.IO) {

        val allLinks = mutableListOf<ExtractorLink>()

        val jobs = providers.map { provider ->
            async {
                withTimeoutOrNull(10000) {
                    try {
                        queryProvider(provider, title, tmdbId, type, season, episode)
                    } catch (e: Exception) {
                        Log.e(TAG, "Provider error", e)
                        emptyList()
                    }
                } ?: emptyList()
            }
        }

        jobs.awaitAll().forEach { links ->
            allLinks.addAll(links)
        }

        allLinks.sortWith(
            compareByDescending<ExtractorLink> {
                it.name.contains("Hindi", ignoreCase = true) ||
                it.url.contains("hindi", ignoreCase = true) ||
                it.name.contains("Dubbed", ignoreCase = true)
            }.thenByDescending { it.quality }
        )

        Log.d(TAG, "Found ${allLinks.size} streams for $title")
        allLinks
    }

    private suspend fun queryProvider(
        provider: Any,
        title: String,
        tmdbId: Int,
        type: String,
        season: Int?,
        episode: Int?
    ): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()

        try {
            val clazz = provider.javaClass

            val searchMethod = clazz.methods.firstOrNull {
                it.name == "search" && it.parameterCount >= 1
            } ?: return emptyList()

            @Suppress("UNCHECKED_CAST")
            val searchResults = searchMethod.invoke(provider, title) as? List<*>
                ?: return emptyList()

            if (searchResults.isEmpty()) return emptyList()

            val firstResult = searchResults.firstOrNull() ?: return emptyList()

            val urlField = firstResult.javaClass.declaredFields
                .firstOrNull { it.name == "url" }
            urlField?.isAccessible = true
            val url = urlField?.get(firstResult) as? String ?: return emptyList()

            val loadMethod = clazz.methods.firstOrNull {
                it.name == "load" && it.parameterCount >= 1
            } ?: return emptyList()

            val loadResponse = loadMethod.invoke(provider, url) ?: return emptyList()

            val dataField = loadResponse.javaClass.declaredFields
                .firstOrNull { it.name == "dataUrl" || it.name == "data" }
            dataField?.isAccessible = true
            val dataUrl = dataField?.get(loadResponse) as? String ?: url

            val loadLinksMethod = clazz.methods.firstOrNull {
                it.name == "loadLinks"
            } ?: return emptyList()

            val subtitleCallback: (Any) -> Unit = { }
            val linkCallback: (Any) -> Unit = { link ->
                try {
                    val linkClazz = link.javaClass
                    val linkUrl = linkClazz.declaredFields
                        .firstOrNull { it.name == "url" }
                        ?.also { it.isAccessible = true }
                        ?.get(link) as? String ?: return@linkCallback

                    val linkName = linkClazz.declaredFields
                        .firstOrNull { it.name == "name" || it.name == "source" }
                        ?.also { it.isAccessible = true }
                        ?.get(link) as? String ?: "Unknown"

                    val isM3u8 = linkUrl.contains(".m3u8") ||
                        (linkClazz.declaredFields
                            .firstOrNull { it.name == "isM3u8" }
                            ?.also { it.isAccessible = true }
                            ?.get(link) as? Boolean ?: false)

                    links.add(ExtractorLink(
                        name = linkName,
                        source = linkName,
                        url = linkUrl,
                        isM3u8 = isM3u8
                    ))
                } catch (e: Exception) { }
            }

            loadLinksMethod.invoke(
                provider, dataUrl, false, subtitleCallback, linkCallback
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error querying provider", e)
        }

        return links
    }
}
