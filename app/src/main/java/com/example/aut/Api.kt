package com.example.aut

import com.example.aut.data_classes.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("/v2/Login/sign-in/registration")
    suspend fun registerUser(@Body userRegistration: RequestBody): Response<User>

    @POST("/v2/Login/sign-in")
    suspend fun authorizationUser(@Body userAuthorization: RequestBody): Response<User>
}