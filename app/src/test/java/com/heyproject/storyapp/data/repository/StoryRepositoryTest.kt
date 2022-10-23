package com.heyproject.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heyproject.storyapp.data.FakeStoryDao
import com.heyproject.storyapp.data.FakeStoryDatabase
import com.heyproject.storyapp.data.FakeStoryService
import com.heyproject.storyapp.data.datasource.local.dao.StoryDao
import com.heyproject.storyapp.data.datasource.remote.api.StoryService
import com.heyproject.storyapp.util.Result
import com.heyproject.storyapp.utils.DataDummy
import com.heyproject.storyapp.utils.MainDispatcherRule
import com.heyproject.storyapp.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Written by Yayan Rahmat Wijaya on 10/22/2022 11:29
 * Github : https://github.com/yayanrw
 */

@ExperimentalCoroutinesApi
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var storyService: StoryService
    private lateinit var storyDao: StoryDao
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyDatabase: FakeStoryDatabase

    private val dummyToken = "token"
    private val dummyFalseToken = "false_token"
    private val dummyStories = DataDummy.generateDummyStoriesResponse()
    private val dummyEmptyStories = DataDummy.generateDummyEmptyStoriesResponse()
    private val dummyPostStoryResponse = DataDummy.generateDummyPostStoryResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyLatitude = DataDummy.generateDummyRequestBody()
    private val dummyLongitude = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyService = FakeStoryService()
        storyDao = FakeStoryDao()
        storyDatabase = FakeStoryDatabase()
        storyRepository = StoryRepository(storyDatabase, storyService)
    }

    @Test
    fun `Get stories should not null`() = runTest {
        val expectedResponse = dummyStories
        val actualResponse = storyRepository.getStories(dummyToken)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
//            Assert.assertEquals(dummyStories.listStory.size, (actualResponse.value))
        }
    }

    @Test
    fun `Get stories with location should not null`() = runTest {
        val expectedResponse = dummyStories
        val actualResponse = storyRepository.getAllStoriesWithLocation(dummyToken)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertNotNull(actualResponse.value?.data?.forEach {
                it.lon
            })
            Assert.assertNotNull(actualResponse.value?.data?.forEach {
                it.lat
            })
            Assert.assertEquals(expectedResponse.listStory.size, actualResponse.value?.data?.size)
        }
    }

    @Test
    fun `Get empty stories with empty result`() = runTest {
        val expectedResponse = dummyEmptyStories
        val actualResponse = storyRepository.getAllStoriesWithLocation(dummyFalseToken)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(expectedResponse.listStory.size, actualResponse.value?.data?.size)
        }
    }

    @Test
    fun `Upload story with success result`() = runTest {
        val expectedResponse = dummyPostStoryResponse

        val actualResponse = storyRepository.uploadStory(
            dummyToken, dummyMultipart, dummyDescription, dummyLatitude, dummyLongitude
        )

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(expectedResponse, (actualResponse.value as Result.Success).data)
        }
    }
}