package com.rajk2007.kino.plugin

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONArray
import java.io.File

data class KinoRepo(
    val name: String,
    val url: String,
    val shortCode: String
)

data class KinoPlugin(
    val name: String,
    val url: String,
    val version: Int,
    val repoName: String,
    val status: String = "available"
)

object PluginManager {
    
    private val client = OkHttpClient()
    
    val DEFAULT_REPOS = listOf(
        KinoRepo(
            name = "Mega Repository",
            url = "https://raw.githubusercontent.com/self-similarity/MegaRepo/builds/repo.json",
            shortCode = "megarepo"
        ),
        KinoRepo(
            name = "CloudStream Providers",
            url = "https://raw.githubusercontent.com/recloudstream/extensions/master/repo.json",
            shortCode = "cspr"
        ),
        KinoRepo(
            name = "Phisher Repo",
            url = "https://raw.githubusercontent.com/phisher98/cloudstream-extensions-phisher/refs/heads/builds/repo.json",
            shortCode = "phisherrepo"
        ),
        KinoRepo(
            name = "Megix Repo",
            url = "https://raw.githubusercontent.com/SaurabhKaperwan/CSX/builds/CS.json",
            shortCode = "csx"
        )
    )
    
    private var installedPlugins = mutableListOf<KinoPlugin>()
    private var installedRepos = mutableListOf<KinoRepo>()
    
    suspend fun installDefaultRepos(
        context: Context,
        onProgress: (repoName: String, progress: Float, done: Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        DEFAULT_REPOS.forEachIndexed { index, repo ->
            try {
                onProgress(repo.name, 0f, false)
                val plugins = fetchRepoPlugins(repo)
                installedPlugins.addAll(plugins)
                installedRepos.add(repo)
                onProgress(repo.name, 1f, true)
                Log.d("KinoPlugin", "Installed repo: ${repo.name} with ${plugins.size} plugins")
            } catch (e: Exception) {
                Log.e("KinoPlugin", "Failed to install repo: ${repo.name}", e)
                onProgress(repo.name, 1f, true)
            }
            kotlinx.coroutines.delay(600)
        }
        // Save to preferences
        saveInstalledState(context)
    }
    
    private suspend fun fetchRepoPlugins(repo: KinoRepo): List<KinoPlugin> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(repo.url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext emptyList()
                parseRepoJson(body, repo.name)
            } catch (e: Exception) {
                Log.e("KinoPlugin", "Error fetching ${repo.url}", e)
                emptyList()
            }
        }
    }
    
    private fun parseRepoJson(json: String, repoName: String): List<KinoPlugin> {
        val plugins = mutableListOf<KinoPlugin>()
        try {
            val obj = JSONObject(json)
            val pluginsArray: JSONArray = when {
                obj.has("plugins") -> obj.getJSONArray("plugins")
                obj.has("extensions") -> obj.getJSONArray("extensions")
                else -> return emptyList()
            }
            for (i in 0 until pluginsArray.length()) {
                val p = pluginsArray.getJSONObject(i)
                plugins.add(
                    KinoPlugin(
                        name = p.optString("name", "Unknown"),
                        url = p.optString("url", ""),
                        version = p.optInt("version", 1),
                        repoName = repoName
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("KinoPlugin", "Error parsing repo JSON", e)
        }
        return plugins
    }
    
    private fun saveInstalledState(context: Context) {
        val prefs = context.getSharedPreferences("kino_plugins", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("repos_installed", true).apply()
    }
    
    fun isInstalled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("kino_plugins", Context.MODE_PRIVATE)
        return prefs.getBoolean("repos_installed", false)
    }
    
    fun getInstalledPlugins() = installedPlugins.toList()
    fun getInstalledRepos() = installedRepos.toList()
}
