package com.example.calorietracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


data class APIResponse(
    val items: List<FoodItem>
)

data class FoodItem(
    val name: String,
    val calories: Float
)

interface NutritionApiService {
    @Headers("X-Api-Key: nHZBE1YtjgKz16iaCrsDFg==3FuJCood6ACiaDzF")
    @GET("nutrition")
    suspend fun getNutritionInfo(@Query("query") query: String): APIResponse
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NutritionApiService by lazy {
        retrofit.create(NutritionApiService::class.java)
    }
}







