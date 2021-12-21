package com.example.andersen_homework_6

import retrofit2.http.GET
import retrofit2.http.Headers

interface Api {
    @Headers("app-id:61c04349893c832f86eaaa49")
    @GET("data/v1/user?limit=50")
    suspend fun getUsers(): GeneralResponse
}