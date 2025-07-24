package com.samprojects.sammary.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://youtube-summarizer2.p.rapidapi.com/"

    val api: SummarizerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SummarizerApi::class.java)
    }
}