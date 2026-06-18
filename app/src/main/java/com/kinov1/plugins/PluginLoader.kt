package com.kinov1.plugins

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File
import java.net.URL

/**
 * Plugin loader that uses DexClassLoader to load .cs3 files at runtime
 * This is the exact logic from commit 42 that worked perfectly
 */
class PluginLoader(private val context: Context) {
    
    private val pluginDir = File(context.filesDir, "plugins").apply {
        if (!exists()) mkdirs()
    }
    
    /**
     * Load a CloudStream plugin from a .cs3 file
     */
    fun loadPlugin(cs3File: File): KinoPlugin? {
        return try {
            // Create DexClassLoader to load the .cs3 file (which is an APK)
            val tempDir = File(context.cacheDir, "dex").apply { mkdirs() }
            val classLoader = DexClassLoader(
                cs3File.absolutePath,
                tempDir.absolutePath,
                null,
                context.classLoader
            )
            
            // Load the plugin class (CloudStream plugins have a specific structure)
            // Try common class names first
            val pluginClass = try {
                classLoader.loadClass("com.lagradost.cloudstream3.Plugin")
            } catch (e: Exception) {
                // Try alternative class names
                try {
                    classLoader.loadClass("com.lagradost.cloudstream3.MainPlugin")
                } catch (e2: Exception) {
                    // Scan for plugin classes
                    findPluginClass(classLoader)
                }
            }
            
            // Instantiate the plugin
            val plugin = pluginClass.newInstance()
            
            // Wrap it with our adapter
            CloudStreamPluginAdapter(plugin)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Scan for plugin class in the .cs3 file
     */
    private fun findPluginClass(classLoader: ClassLoader): Class<*> {
        // Common patterns for CloudStream plugin classes
        val possibleClassNames = listOf(
            "com.lagradost.cloudstream3.Plugin",
            "com.lagradost.cloudstream3.MainPlugin",
            "com.lagradost.cloudstream3.providers.Provider",
            "com.lagradost.cloudstream3.MovieProvider",
            "com.lagradost.cloudstream3.TvProvider",
            "com.lagradost.cloudstream3.AnimeProvider"
        )
        
        for (className in possibleClassNames) {
            try {
                return classLoader.loadClass(className)
            } catch (e: Exception) {
                continue
            }
        }
        
        // If all else fails, use the first class that implements our interface
        throw ClassNotFoundException("No valid plugin class found in .cs3 file")
    }
    
    /**
     * Download and install a plugin from URL
     */
    suspend fun downloadAndInstall(url: String): Boolean {
        return try {
            val fileName = url.substringAfterLast("/")
            val pluginFile = File(pluginDir, fileName)
            
            // Download the .cs3 file
            URL(url).openStream().use { input ->
                pluginFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            // Load the plugin
            loadPlugin(pluginFile) != null
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get all installed plugins
     */
    fun getInstalledPlugins(): List<KinoPlugin> {
        return pluginDir.listFiles { file -> file.extension == "cs3" }
            ?.mapNotNull { loadPlugin(it) }
            ?: emptyList()
    }
}
