package com.rajk2007.kino.cloudstream

import android.content.Context
import android.util.Log
import dalvik.system.PathClassLoader
import com.lagradost.cloudstream3.plugins.Plugin
import com.lagradost.cloudstream3.MainAPI
import java.io.File

class PluginLoader(private val context: Context) {
    private val TAG = "KinoPluginLoader"
    private val loadedProviders = mutableListOf<MainAPI>()

    fun loadPlugin(pluginFile: File): List<MainAPI> {
        try {
            pluginFile.setReadOnly()
            val classLoader = PathClassLoader(pluginFile.absolutePath, context.classLoader)

            // Load Plugin wrapper (standard in CloudStream plugins)
            val pluginClass = classLoader.loadClass("com.lagradost.cloudstream3.plugins.Plugin")
            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as Plugin

            // Call init if exists
            try {
                val initMethod = pluginClass.getMethod("init", Context::class.java)
                initMethod.invoke(pluginInstance, context)
            } catch (_: Exception) {}

            val providers = pluginInstance.getProviders()
            loadedProviders.addAll(providers)

            Log.d(TAG, "Loaded ${providers.size} providers from ${pluginFile.name}")
            return providers
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load ${pluginFile.name}", e)
            return emptyList()
        }
    }

    fun loadAllPlugins(pluginFiles: List<File>): List<MainAPI> {
        loadedProviders.clear()
        pluginFiles.forEach { loadPlugin(it) }
        return loadedProviders.toList()
    }

    fun getLoadedProviders() = loadedProviders.toList()
}
