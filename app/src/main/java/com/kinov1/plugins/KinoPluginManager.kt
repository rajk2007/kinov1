package com.kinov1.plugins

import android.content.Context
import com.kinov1.plugins.data.KinoExtractorLink
import com.kinov1.plugins.data.KinoLoadResponse
import com.kinov1.plugins.data.KinoSearchResponse
import java.io.File

/**
 * Manages all plugins and provides unified search and load operations
 */
class KinoPluginManager(private val context: Context) {
    
    private val loader = PluginLoader(context)
    private val plugins = mutableListOf<KinoPlugin>()
    
    /**
     * Initialize with default plugins
     */
    fun initialize() {
        plugins.clear()
        plugins.addAll(loader.getInstalledPlugins())
    }
    
    /**
     * Add a plugin from URL
     */
    suspend fun addPlugin(url: String): Boolean {
        return try {
            val success = loader.downloadAndInstall(url)
            if (success) {
                val plugin = loader.loadPlugin(File(context.filesDir, "plugins/${url.substringAfterLast("/")}"))
                plugin?.let { plugins.add(it) }
            }
            success
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Search across all plugins
     */
    fun search(query: String): List<KinoSearchResponse> {
        return plugins.flatMap { plugin ->
            try {
                plugin.search(query)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    /**
     * Load content from a specific plugin
     */
    fun load(url: String, apiName: String): KinoLoadResponse? {
        val plugin = plugins.find { it.name == apiName }
        return plugin?.load(url)
    }
    
    /**
     * Extract video links from a URL
     */
    fun extractLinks(url: String): List<KinoExtractorLink> {
        return plugins.flatMap { plugin ->
            try {
                plugin.extractLinks(url)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    /**
     * Get all plugin names
     */
    fun getPluginNames(): List<String> = plugins.map { it.name }
    
    /**
     * Remove a plugin
     */
    fun removePlugin(apiName: String): Boolean {
        return plugins.removeIf { it.name == apiName }
    }
}
