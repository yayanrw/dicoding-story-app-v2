package com.heyproject.storyapp.ui.story_add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.util.Result
import com.heyproject.storyapp.utils.DataDummy
import com.heyproject.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Written by Yayan Rahmat Wijaya on 10/22/2022 06:28
 * Github : https://github.com/yayanrw
 */

@RunWith(MockitoJUnitRunner::class)
class StoryAddViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyAddViewModel: StoryAddViewModel

    private val dummyToken = "token"
    private val dummyStoryAddResponse = DataDummy.generateDummyGeneralResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyLatitude = DataDummy.generateDummyRequestBody()
    private val dummyLongitude = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyAddViewModel = StoryAddViewModel(storyRepository)
    }

    @Test
    fun `Upload file success with share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Success(dummyStoryAddResponse)

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponse)

        val actualResponse =
            storyAddViewModel.uploadImage(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(
            dummyStoryAddResponse,
            (actualResponse as Result.Success).data
        )
    }

    @Test
    fun `Upload file failed with share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                dummyLatitude,
                dummyLongitude
            )
        ).thenReturn(expectedResponse)

        val actualResponse = storyAddViewModel.uploadImage(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            dummyLatitude,
            dummyLongitude
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `Upload file success without share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Success(dummyStoryAddResponse)

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        val actualResponse =
            storyAddViewModel.uploadImage(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            null,
            null
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(
            dummyStoryAddResponse,
            (actualResponse as Result.Success).data
        )
    }

    @Test
    fun `Upload file failed without share location`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(
            storyRepository.uploadStory(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        val actualResponse = storyAddViewModel.uploadImage(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            null,
            null
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(
            dummyToken,
            dummyMultipart,
            dummyDescription,
            null,
            null
        )

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}