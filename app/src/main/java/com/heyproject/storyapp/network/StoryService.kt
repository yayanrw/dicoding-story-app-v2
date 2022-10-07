package com.heyproject.storyapp.network

import com.heyproject.storyapp.network.response.GeneralResponse
import com.heyproject.storyapp.network.response.LoginResponse
import com.heyproject.storyapp.network.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GeneralResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun insertStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") auth: String
    ): GeneralResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int,
        @Header("Authorization") auth: String
    ) : StoriesResponse
}