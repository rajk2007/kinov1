package com.kinov1.plugins

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Automatically installs default CloudStream repositories
 */
class DefaultRepoInstaller(private val context: Context) {
    
    private val pluginManager = KinoPluginManager(context)
    
    private val defaultPlugins = listOf(
        "https://raw.githubusercontent.com/recloudstream/cloudstream-extensions/master/mega/mega.cs3",
        "https://raw.githubusercontent.com/recloudstream/cloudstream-extensions/master/phisher/phisher.cs3",
        "https://raw.githubusercontent.com/recloudstream/cloudstream-extensions/master/saurabh/saurabh.cs3"
    )
    
    suspend fun installDefaults() = withContext(Dispatchers.IO) {
        defaultPlugins.forEach { url ->
            pluginManager.addPlugin(url)
        }
    }
    
    fun areDefaultsInstalled(): Boolean {
        val installedNames = pluginManager.getPluginNames().map { it.lowercase() }
        return installedNames.contains("mega") || 
               installedNames.contains("phisher") ||
               installedNames.contains("saurabh")
    }
}
