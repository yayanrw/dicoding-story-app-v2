package com.heyproject.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.heyproject.storyapp.data.repository.StoryRepository
import com.heyproject.storyapp.domain.model.Story
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
 * Written by Yayan Rahmat Wijaya on 10/21/2022 16:20
 * Github : https://github.com/yayanrw
 */

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStoriesWithLocation = DataDummy.generateDummyStoriesWithLocation()

    private val dummyToken = "token"

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `Get stories successfully with location`() {
        val expectedResponse = MutableLiveData<Result<List<Story>>>()
        expectedResponse.value = Result.Success(dummyStoriesWithLocation)

        `when`(storyRepository.getAllStoriesWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse =
            mapsViewModel.fetchAllStoryWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getAllStoriesWithLocation(dummyToken)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(
            dummyStoriesWithLocation,
            (actualResponse as Result.Success).data
        )
        Assert.assertNotNull(
            actualResponse.data?.forEach {
                it.lon
            }
        )
        Assert.assertNotNull(
            actualResponse.data?.forEach {
                it.lat
            }
        )
    }

    @Test
    fun `Get stories with failed result`() {
        val expectedResponse = MutableLiveData<Result<List<Story>>>()
        expectedResponse.value = Result.Error("Error")

        `when`(storyRepository.getAllStoriesWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse =
            mapsViewModel.fetchAllStoryWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getAllStoriesWithLocation(dummyToken)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}