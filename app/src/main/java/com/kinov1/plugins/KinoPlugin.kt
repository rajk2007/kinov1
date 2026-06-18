package com.kinov1.plugins

import com.kinov1.plugins.data.KinoExtractorLink
import com.kinov1.plugins.data.KinoLoadResponse
import com.kinov1.plugins.data.KinoSearchResponse

/**
 * Minimal plugin interface that CloudStream plugins can implement
 * This uses reflection to work with actual CloudStream Plugin class at runtime
 */
interface KinoPlugin {
    val name: String
    val enabled: Boolean
    
    fun search(query: String): List<KinoSearchResponse>
    fun load(url: String): KinoLoadResponse
    fun extractLinks(url: String): List<KinoExtractorLink>
}

/**
 * CloudStream plugin adapter using reflection
 */
class CloudStreamPluginAdapter(private val plugin: Any) : KinoPlugin {
    override val name: String = plugin::class.simpleName ?: "Unknown"
    override val enabled: Boolean = true
    
    override fun search(query: String): List<KinoSearchResponse> {
        return try {
            // Use reflection to call plugin's search method
            val method = plugin::class.java.getMethod("search", String::class.java)
            val result = method.invoke(plugin, query)
            
            // Convert CloudStream SearchResponse to KinoSearchResponse
            (result as? List<*>)?.mapNotNull { item ->
                item?.let { convertSearchResponse(it) }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override fun load(url: String): KinoLoadResponse {
        return try {
            val method = plugin::class.java.getMethod("load", String::class.java)
            val result = method.invoke(plugin, url)
            convertLoadResponse(result)
        } catch (e: Exception) {
            KinoLoadResponse(name = "Unknown", url = url)
        }
    }
    
    override fun extractLinks(url: String): List<KinoExtractorLink> {
        return try {
            val method = plugin::class.java.getMethod("extractLinks", String::class.java)
            val result = method.invoke(plugin, url)
            
            (result as? List<*>)?.mapNotNull { item ->
                item?.let { convertExtractorLink(it) }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun convertSearchResponse(response: Any): KinoSearchResponse {
        // Use reflection to extract properties from CloudStream SearchResponse
        val name = response::class.java.getMethod("getName").invoke(response) as? String ?: ""
        val url = response::class.java.getMethod("getUrl").invoke(response) as? String ?: ""
        val apiName = response::class.java.getMethod("getApiName").invoke(response) as? String ?: ""
        
        return KinoSearchResponse(
            name = name,
            url = url,
            apiName = apiName,
            type = try { response::class.java.getMethod("getType").invoke(response) as? String } catch (e: Exception) { null },
            poster = try { response::class.java.getMethod("getPoster").invoke(response) as? String } catch (e: Exception) { null },
            background = try { response::class.java.getMethod("getBackground").invoke(response) as? String } catch (e: Exception) { null },
            plot = try { response::class.java.getMethod("getPlot").invoke(response) as? String } catch (e: Exception) { null }
        )
    }
    
    private fun convertLoadResponse(response: Any): KinoLoadResponse {
        val name = response::class.java.getMethod("getName").invoke(response) as? String ?: ""
        val url = response::class.java.getMethod("getUrl").invoke(response) as? String ?: ""
        
        val links = try {
            val method = response::class.java.getMethod("getExtractorLinks")
            val result = method.invoke(response) as? List<*>
            result?.mapNotNull { convertExtractorLink(it!!) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        
        return KinoLoadResponse(
            name = name,
            url = url,
            links = links
        )
    }
    
    private fun convertExtractorLink(link: Any): KinoExtractorLink {
        val name = link::class.java.getMethod("getName").invoke(link) as? String ?: ""
        val url = link::class.java.getMethod("getUrl").invoke(link) as? String ?: ""
        val referer = try { link::class.java.getMethod("getReferer").invoke(link) as? String } catch (e: Exception) { null }
        val quality = try { (link::class.java.getMethod("getQuality").invoke(link) as? Int) } catch (e: Exception) { null }
        
        return KinoExtractorLink(
            name = name,
            url = url,
            referer = referer,
            quality = quality
        )
    }
}
