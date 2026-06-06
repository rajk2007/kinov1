package com.rajk2007.kino.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/all/week")
    suspend fun getTrendingAll(): TmdbResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(): TmdbResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTv(): TmdbResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(): TmdbResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): TmdbResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): TmdbResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): TmdbResponse

    @GET("tv/popular")
    suspend fun getPopularTv(): TmdbResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTv(): TmdbResponse

    @GET("tv/airing_today")
    suspend fun getAiringTodayTv(): TmdbResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genres: String? = null,
        @Query("with_original_language") language: String? = null
    ): TmdbResponse

    @GET("discover/tv")
    suspend fun discoverTv(
        @Query("with_genres") genres: String? = null,
        @Query("with_original_language") language: String? = null
    ): TmdbResponse

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String
    ): TmdbResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") append: String = "credits,similar,videos"
    ): MovieDetailResponse

    @GET("tv/{id}")
    suspend fun getTvDetails(
        @Path("id") id: Int,
        @Query("append_to_response") append: String = "credits,similar,videos,seasons"
    ): TvDetailResponse

    companion object {
        const val API_KEY = "cf5a2b948bb3cbe03332dc70594b4ba7"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w1280"
    }
}

data class TmdbResponse(
    val results: List<TmdbMedia>
)

data class TmdbMedia(
    val id: Int,
    val title: String?,
    val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("media_type") val mediaType: String?
)

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    val runtime: Int?,
    val genres: List<Genre>,
    val credits: Credits?,
    val similar: TmdbResponse?,
    val videos: VideoResponse?
)

data class TvDetailResponse(
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("episode_run_time") val runtime: List<Int>?,
    val genres: List<Genre>,
    val credits: Credits?,
    val similar: TmdbResponse?,
    val videos: VideoResponse?,
    val seasons: List<Season>?
)

data class Genre(val id: Int, val name: String)
data class Credits(val cast: List<Cast>)
data class Cast(val id: Int, val name: String, @SerializedName("profile_path") val profilePath: String?)
data class VideoResponse(val results: List<Video>)
data class Video(val key: String, val site: String, val type: String)
data class Season(val id: Int, @SerializedName("season_number") val seasonNumber: Int, @SerializedName("episode_count") val episodeCount: Int)
