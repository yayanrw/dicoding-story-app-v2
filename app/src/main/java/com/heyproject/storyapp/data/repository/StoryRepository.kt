package com.heyproject.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.heyproject.storyapp.data.local.database.StoryDatabase
import com.heyproject.storyapp.data.local.entity.StoryEntity
import com.heyproject.storyapp.data.remote.StoryRemoteMediator
import com.heyproject.storyapp.data.remote.api.StoryService
import com.heyproject.storyapp.data.remote.response.GeneralResponse
import com.heyproject.storyapp.data.remote.response.StoriesResponse
import com.heyproject.storyapp.util.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
Written by Yayan Rahmat Wijaya on 10/10/2022 10:01
Github : https://github.com/yayanrw
 **/

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val storyService: StoryService
) {
    fun getAllStories(token: String): Flow<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                storyService,
                generateBearerToken(token)
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).flow
    }

    fun getAllStoriesWithLocation(token: String): Flow<Result<StoriesResponse>> = flow {
        wrapEspressoIdlingResource {
            try {
                val bearerToken = generateBearerToken(token)
                val response = storyService.getStories(bearerToken, size = 30, location = 1)
                emit(Result.success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.failure(e))
            }
        }
    }

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Result<GeneralResponse>> = flow {
        try {
            val bearerToken = generateBearerToken(token)
            val response = storyService.insertStory(bearerToken, file, description, lat, lon)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }
}