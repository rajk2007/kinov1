package com.rajk2007.kino.cloudstream

import android.content.Context
import android.util.Log
import dalvik.system.PathClassLoader
import java.io.File

class PluginLoader(private val context: Context) {

    private val TAG = "KinoPluginLoader"
    private val loadedProviders = mutableListOf<Any>()

    fun loadPlugin(pluginFile: File): List<Any> {
        val providers = mutableListOf<Any>()
        try {
            pluginFile.setReadOnly()
            
            val classLoader = PathClassLoader(
                pluginFile.absolutePath,
                context.classLoader
            )

            try {
                val manifestStream = classLoader.getResourceAsStream("manifest.json")
                if (manifestStream != null) {
                    val manifest = manifestStream.bufferedReader().readText()
                    Log.d(TAG, "Plugin manifest: ")
                }
            } catch (e: Exception) { }

            val entryPoints = listOf(
                "com.lagradost.cloudstream3.plugins.Plugin",
                "com.lagradost.cloudstream3.MainAPI"
            )

            for (entry in entryPoints) {
                try {
                    val clazz = classLoader.loadClass(entry)
                    val instance = try {
                        clazz.getDeclaredConstructor(Context::class.java)
                            .newInstance(context)
                    } catch (e: Exception) {
                        clazz.getDeclaredConstructor().newInstance()
                    }
                    
                    try {
                        val loadMethod = clazz.getMethod("load", Context::class.java)
                        loadMethod.invoke(instance, context)
                    } catch (e: Exception) { }

                    try {
                        val providersMethod = clazz.getMethod("getProviders")
                        val result = providersMethod.invoke(instance)
                        if (result is List<*>) {
                            providers.addAll(result.filterNotNull())
                        }
                    } catch (e: Exception) { }

                    providers.add(instance)
                    break
                } catch (e: ClassNotFoundException) {
                    continue
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading $entry", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load plugin: ${pluginFile.name}", e)
        }
        return providers
    }

    fun loadAllPlugins(pluginFiles: List<File>): List<Any> {
        loadedProviders.clear()
        pluginFiles.forEach { file ->
            val providers = loadPlugin(file)
            loadedProviders.addAll(providers)
            Log.d(TAG, "Loaded ${providers.size} providers from ${file.name}")
        }
        return loadedProviders.toList()
    }

    fun getLoadedProviders() = loadedProviders.toList()
}
