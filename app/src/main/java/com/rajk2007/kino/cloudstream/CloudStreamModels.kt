package com.rajk2007.kino.cloudstream

import com.lagradost.cloudstream3.*

// Official library aliases
typealias KinoMainAPI = MainAPI
typealias KinoExtractorLink = ExtractorLink
typealias KinoSubtitleFile = SubtitleFile
typealias KinoSearchResponse = SearchResponse
typealias KinoLoadResponse = LoadResponse
typealias KinoMovieLoadResponse = MovieLoadResponse
typealias KinoTvSeriesLoadResponse = TvSeriesLoadResponse
typealias KinoTvType = TvType
typealias KinoEpisode = Episode

object CloudStreamUtils {
    fun isHindi(link: ExtractorLink): Boolean {
        return link.name.contains("Hindi", ignoreCase = true) ||
               link.name.contains("Dub", ignoreCase = true)
    }
}
