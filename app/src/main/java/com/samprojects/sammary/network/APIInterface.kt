package com.samprojects.sammary.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SummarizerApi {
    @GET("summary_translate")
    fun getSummary(
        @Query("id") videoId: String,
        @Query("language") language: String = "en",
        @Header("x-rapidapi-key") apiKey: String = "API_KEY",
        @Header("x-rapidapi-host") host: String = "youtube-summarizer2.p.rapidapi.com"
    ): Call<ResponseBody>
}
