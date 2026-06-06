package com.rajk2007.kino.cloudstream

enum class TvType {
    Movie, TvSeries, Anime, OVA, AnimeMovie, Cartoon, Documentary, Live
}

data class ExtractorLink(
    val name: String,
    val source: String,
    val url: String,
    val referer: String = "",
    val quality: Int = 0,
    val isM3u8: Boolean = false,
    val headers: Map<String, String> = emptyMap()
)

data class SubtitleFile(
    val lang: String,
    val url: String
)

data class SearchResponse(
    val name: String,
    val url: String,
    val apiName: String,
    val type: TvType,
    val posterUrl: String? = null,
    val id: Int? = null
)

interface LoadResponse {
    val name: String
    val url: String
    val apiName: String
    val type: TvType
    val posterUrl: String?
}

data class MovieLoadResponse(
    override val name: String,
    override val url: String,
    override val apiName: String,
    override val type: TvType = TvType.Movie,
    override val posterUrl: String?,
    val dataUrl: String
) : LoadResponse

data class EpisodeMetadata(
    val name: String?,
    val season: Int,
    val episode: Int,
    val data: String
)

data class TvSeriesLoadResponse(
    override val name: String,
    override val url: String,
    override val apiName: String,
    override val type: TvType = TvType.TvSeries,
    override val posterUrl: String?,
    val episodes: List<EpisodeMetadata>
) : LoadResponse

abstract class MainAPI {
    abstract val name: String
    abstract val mainUrl: String
    open val lang: String = "en"
    open val supportedTypes: Set<TvType> = setOf(TvType.Movie, TvType.TvSeries)

    open suspend fun search(query: String): List<SearchResponse> = emptyList()
    open suspend fun load(url: String): LoadResponse? = null
    open suspend fun loadLinks(
        data: String,
        isCdn: Boolean = false,
        subtitleCallback: (SubtitleFile) -> Unit = {},
        callback: (ExtractorLink) -> Unit
    ): Boolean = false
}
