package com.rajk2007.kino.cloudstream

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@Serializable
data class RepoManifest(
    val pluginLists: List<String> = emptyList(),
    val name: String = "",
    val description: String = ""
)

@Serializable
data class PluginInfo(
    val name: String = "",
    val internalName: String = "",
    val url: String = "",
    val version: Int = 1,
    val fileHash: String? = null,
    val tvTypes: List<String>? = null,
    val language: String? = "en",
    val status: Int = 1
)

class RepoParser {
    private val client = OkHttpClient()
    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    suspend fun fetchPluginsFromRepo(repoUrl: String): List<PluginInfo> = 
        withContext(Dispatchers.IO) {
            val allPlugins = mutableListOf<PluginInfo>()
            try {
                val repoBody = fetchUrl(repoUrl) ?: return@withContext emptyList()
                
                // Try parsing as direct plugin list first
                try {
                    val directList = json.decodeFromString<List<PluginInfo>>(repoBody)
                    allPlugins.addAll(directList.filter { it.url.isNotEmpty() })
                    return@withContext allPlugins
                } catch (e: Exception) { }
                
                // Try parsing as manifest with pluginLists
                try {
                    val manifest = json.decodeFromString<RepoManifest>(repoBody)
                    for (pluginsUrl in manifest.pluginLists) {
                        val pluginsBody = fetchUrl(pluginsUrl) ?: continue
                        try {
                            val plugins = json.decodeFromString<List<PluginInfo>>(pluginsBody)
                            allPlugins.addAll(plugins.filter { it.url.isNotEmpty() })
                        } catch (e: Exception) { e.printStackTrace() }
                    }
                } catch (e: Exception) { e.printStackTrace() }
                
            } catch (e: Exception) { e.printStackTrace() }
            allPlugins
        }

    private fun fetchUrl(url: String): String? {
        return try {
            val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0")
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) response.body?.string() else null
            }
        } catch (e: Exception) { null }
    }
}
