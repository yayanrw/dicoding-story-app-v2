package com.heyproject.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.heyproject.storyapp.data.StoryRemoteMediator
import com.heyproject.storyapp.data.datasource.local.database.StoryDatabase
import com.heyproject.storyapp.data.datasource.local.entity.StoryEntity
import com.heyproject.storyapp.data.datasource.remote.api.StoryService
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 10:01
Github : https://github.com/yayanrw
 **/

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val storyService: StoryService
) {
    fun getStories(token: String): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                storyService,
                generateBearerToken(token)
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData
    }

    suspend fun getAllStoriesWithLocation(token: String) = storyService.getStories(
        generateBearerToken(token),
        size = 10,
        location = 1
    )

    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): GeneralResponse {
        return storyService.postStory(
            generateBearerToken(token),
            file,
            description,
            lat,
            lon
        )
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }

    companion object {
        private var INSTANCE: StoryRepository? = null
        fun getInstance(storyService: StoryService, storyDatabase: StoryDatabase): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                StoryRepository(storyDatabase, storyService).also {
                    INSTANCE = it
                }
            }
        }
    }
}