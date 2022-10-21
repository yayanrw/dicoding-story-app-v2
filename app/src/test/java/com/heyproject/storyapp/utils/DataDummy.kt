package com.heyproject.storyapp.utils

import com.heyproject.storyapp.data.datasource.remote.dto.LoginResultDto
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.datasource.remote.response.LoginResponse
import com.heyproject.storyapp.domain.model.Story

/**
Written by Yayan Rahmat Wijaya on 10/21/2022 05:05
Github : https://github.com/yayanrw
 **/

object DataDummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResultDto = LoginResultDto(
            name = "Arif Faizin",
            userId = "user-yj5pc_LARC_AgK61",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )

        return LoginResponse(
            loginResult = loginResultDto,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): GeneralResponse {
        return GeneralResponse(error = false, message = "success")
    }

    fun generateDummyStoriesWithLocation(): List<Story> {
        val stories = ArrayList<Story>()
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
}