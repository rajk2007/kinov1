package com.kinov1.plugins.data

/**
 * Minimal extractor link interface mirroring CloudStream's ExtractorLink
 * This allows runtime loading of CloudStream plugins without compile-time dependency
 */
data class KinoExtractorLink(
    val name: String,
    val url: String,
    val referer: String? = null,
    val quality: Int? = null,
    val headers: Map<String, String> = mapOf(),
    val type: String = "M3U8" // M3U8, MP4, etc.
)

/**
 * Minimal search response interface
 */
data class KinoSearchResponse(
    val name: String,
    val url: String,
    val apiName: String,
    val type: String? = null, // "movie", "tv", "anime"
    val poster: String? = null,
    val background: String? = null,
    val plot: String? = null
)

/**
 * Minimal load response interface
 */
data class KinoLoadResponse(
    val name: String,
    val url: String,
    val data: String? = null,
    val links: List<KinoExtractorLink> = listOf(),
    val subtitles: List<KinoSubtitle> = listOf()
)

data class KinoSubtitle(
    val url: String,
    val lang: String,
    val type: String = "vtt"
)
