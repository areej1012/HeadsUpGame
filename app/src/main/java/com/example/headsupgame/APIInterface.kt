package com.example.headsupgame

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface APIInterface {

    @GET("celebrities/")
    fun getAllCelebrities() : Call<ArrayList<Celebrity>>
}