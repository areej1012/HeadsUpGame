package com.example.headsupgame

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    private var retrofit : Retrofit? = null

    fun celebrity() : Retrofit?{
        retrofit = Retrofit.Builder()
            .baseUrl("https://dojo-recipes.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

}