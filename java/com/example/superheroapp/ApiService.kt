package com.example.superheroapp

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("all.json")
    suspend fun getAllHeroes(): Response<List<Hero>>

    @GET("id/{id}.json")
    suspend fun getHeroDetail(@Path("id") id: Int): Response<Hero>
}

object RetrofitClient {
    private const val BASE_URL = "https://akabab.github.io/superhero-api/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}