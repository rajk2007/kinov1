package com.rajk2007.kino.cloudstream

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class PluginDownloader(private val context: Context) {
    private val client = OkHttpClient()

    val pluginsDir: File
        get() = File(context.filesDir, "kino_plugins").apply { mkdirs() }

    suspend fun downloadPlugin(
        url: String,
        internalName: String
    ): File? = withContext(Dispatchers.IO) {
        try {
            val file = File(pluginsDir, "$internalName.cs3")
            
            // Skip if already downloaded same version
            if (file.exists() && file.length() > 1000) {
                return@withContext file
            }
            
            val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0")
                .build()
                
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body ?: return@withContext null
                FileOutputStream(file).use { fos ->
                    body.byteStream().copyTo(fos)
                }
            }
            
            if (file.length() > 1000) file else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getDownloadedPlugins(): List<File> {
        return pluginsDir.listFiles()
            ?.filter { it.extension == "cs3" && it.length() > 1000 }
            ?: emptyList()
    }

    fun clearPlugins() {
        pluginsDir.listFiles()?.forEach { it.delete() }
    }
}
