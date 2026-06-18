package com.kinov1.plugins

import com.kinov1.plugins.data.KinoExtractorLink
import com.kinov1.plugins.data.KinoSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * High-level engine for searching and extracting streams
 */
class StreamEngine(private val pluginManager: KinoPluginManager) {
    
    /**
     * Search for a movie or TV show across all providers
     */
    suspend fun search(query: String): List<KinoSearchResponse> = withContext(Dispatchers.IO) {
        pluginManager.search(query)
    }
    
    /**
     * Get direct streaming links for a specific content URL
     */
    suspend fun getLinks(url: String): List<KinoExtractorLink> = withContext(Dispatchers.IO) {
        pluginManager.extractLinks(url)
    }
}
