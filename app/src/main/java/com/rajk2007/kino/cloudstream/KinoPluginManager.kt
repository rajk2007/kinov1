package com.rajk2007.kino.cloudstream

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.ExtractorLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class KinoRepo(
    val name: String,
    val url: String,
    val shortCode: String
)

object KinoPluginManager {

    private val TAG = "KinoPluginManager"

    val DEFAULT_REPOS = listOf(
        KinoRepo(
            "Mega Repository",
            "https://raw.githubusercontent.com/self-similarity/MegaRepo/builds/repo.json",
            "megarepo"
        ),
        KinoRepo(
            "CloudStream Providers",
            "https://raw.githubusercontent.com/recloudstream/extensions/master/repo.json",
            "cspr"
        ),
        KinoRepo(
            "Phisher Repo",
            "https://raw.githubusercontent.com/phisher98/cloudstream-extensions-phisher/refs/heads/builds/repo.json",
            "phisherrepo"
        ),
        KinoRepo(
            "Megix Repo",
            "https://raw.githubusercontent.com/SaurabhKaperwan/CSX/builds/CS.json",
            "csx"
        )
    )

    private lateinit var downloader: PluginDownloader
    private lateinit var loader: PluginLoader
    private val repoParser = RepoParser()
    private val streamEngine = StreamEngine()
    private var loadedProviders = listOf<MainAPI>()

    fun initialize(context: Context) {
        if (!::downloader.isInitialized) {
            downloader = PluginDownloader(context)
        }
        if (!::loader.isInitialized) {
            loader = PluginLoader(context)
        }
    }

    fun isInitialized(context: Context): Boolean {
        val prefs = context.getSharedPreferences("kino_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("plugins_installed", false)
    }

    suspend fun installAllRepos(
        context: Context,
        onRepoProgress: (repoName: String, done: Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        initialize(context)
        var totalInstalled = 0

        for (repo in DEFAULT_REPOS) {
            try {
                Log.d(TAG, "Installing repo: ${repo.name}")
                onRepoProgress(repo.name, false)

                val plugins = repoParser.fetchPluginsFromRepo(repo.url)
                Log.d(TAG, "Found ${plugins.size} plugins in ${repo.name}")

                val toDownload = plugins.take(20)

                for (plugin in toDownload) {
                    try {
                        val file = downloader.downloadPlugin(
                            plugin.url,
                            plugin.internalName.ifEmpty { plugin.name }
                        )
                        if (file != null) {
                            totalInstalled++
                            Log.d(TAG, "Downloaded: ${plugin.name}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to download ${plugin.name}", e)
                    }
                }

                onRepoProgress(repo.name, true)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to install repo: ${repo.name}", e)
                onRepoProgress(repo.name, true)
            }
        }

        val pluginFiles = downloader.getDownloadedPlugins()
        loadedProviders = loader.loadAllPlugins(pluginFiles)
        Log.d(TAG, "Loaded ${loadedProviders.size} providers from $totalInstalled plugins")

        val prefs = context.getSharedPreferences("kino_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("plugins_installed", true).apply()
    }

    suspend fun loadInstalledPlugins(context: Context) {
        initialize(context)
        val pluginFiles = downloader.getDownloadedPlugins()
        loadedProviders = loader.loadAllPlugins(pluginFiles)
        Log.d(TAG, "Loaded ${loadedProviders.size} providers")
    }

    suspend fun getStreams(
        title: String,
        tmdbId: Int,
        type: String,
        season: Int? = null,
        episode: Int? = null
    ): List<ExtractorLink> {
        if (loadedProviders.isEmpty()) return emptyList()
        return streamEngine.getStreamsForContent(
            loadedProviders, title, tmdbId, type, season, episode
        )
    }

    fun getProviderCount() = loadedProviders.size
}
