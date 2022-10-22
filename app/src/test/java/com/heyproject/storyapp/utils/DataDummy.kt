package com.heyproject.storyapp.utils

import com.heyproject.storyapp.data.datasource.remote.dto.LoginResultDto
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.datasource.remote.response.LoginResponse
import com.heyproject.storyapp.domain.model.Story
import com.heyproject.storyapp.domain.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
Written by Yayan Rahmat Wijaya on 10/21/2022 05:05
Github : https://github.com/yayanrw
 **/

object DataDummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResultDto = LoginResultDto(
            name = "Yayan Rahmat Wijaya",
            userId = "user-yj5pc_LARC_AgK61",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )

        return LoginResponse(
            loginResult = loginResultDto, error = false, message = "success"
        )
    }

    fun generateDummyGeneralResponse(): GeneralResponse {
        return GeneralResponse(error = false, message = "success")
    }

    fun generateDummyStoriesWithoutLocation(): List<Story> {
        val stories = arrayListOf<Story>()
        for (i in 1..10) {
            val story = Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Yayan",
                description = "Lorem Ipsum",
                lon = null,
                lat = null
            )
            stories.add(story)
        }
        return stories
    }

    fun generateDummyStoriesWithLocation(): List<Story> {
        val stories = arrayListOf<Story>()
        for (i in 1..10) {
            val story = Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Yayan",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
            stories.add(story)
        }
        return stories
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }

    fun generateDummyLoggedInUser(): User {
        return User(
            userId = "user-yj5pc_LARC_AgK61",
            name = "Yayan Rahmat Wijaya",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I",
            isLogin = true
        )
    }

    fun generateDummyLoggedOutUser(): User {
        return User(
            "", "", "", false
        )
    }
}