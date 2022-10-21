package com.heyproject.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.heyproject.storyapp.data.datasource.remote.response.GeneralResponse
import com.heyproject.storyapp.data.repository.UserRepository
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
 * Written by Yayan Rahmat Wijaya on 10/21/2022 15:15
 * Github : https://github.com/yayanrw
 */

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()

    private val dummyName = "name"
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userRepository)
    }

    @Test
    fun `Register success with success result`() {
        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Success(dummyRegisterResponse)

        `when`(userRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        val actualResponse =
            registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(userRepository).register(dummyName, dummyEmail, dummyPassword)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(dummyRegisterResponse, (actualResponse as Result.Success).data)
    }
}