package com.example.oompaloompa.data.network

import com.example.oompaloompa.model.ListOompaLoompa
import com.example.oompaloompa.model.OompaLoompaModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OompaLoompaApi {
    companion object {
        val instance = Retrofit.Builder()
            .baseUrl("https://2q2woep105.execute-api.eu-west-1.amazonaws.com/napptilus/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build()
            ).build().create(OompaLoompaApi::class.java)
    }

    @GET("oompa-loompas")
    suspend fun getCharacters(@Query("page") page: String): ListOompaLoompa

    @GET("oompa-loompas/{id}")
    suspend fun getCharacterByID(@Path("id") id: String): OompaLoompaModel
}