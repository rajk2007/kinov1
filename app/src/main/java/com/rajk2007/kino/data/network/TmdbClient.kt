package com.rajk2007.kino.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "cf5a2b948bb3cbe03332dc70594b4ba7"
    
    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}
