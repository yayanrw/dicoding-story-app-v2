package com.heyproject.storyapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.heyproject.storyapp.data.repository.UserRepository
import com.heyproject.storyapp.domain.model.User
import com.heyproject.storyapp.utils.DataDummy
import com.heyproject.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Written by Yayan Rahmat Wijaya on 10/22/2022 07:51
 * Github : https://github.com/yayanrw
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var sharedViewModel: SharedViewModel
    private val dummyLoggedInUser = DataDummy.generateDummyLoggedInUser()
    private val dummyLoggedOutUser = DataDummy.generateDummyLoggedOutUser()

    @Before
    fun setUp() {
        sharedViewModel = SharedViewModel(userRepository)
    }

    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Save user success and return right user livedata`() = runTest {
        val expectedResponse = MutableLiveData<User>()
        expectedResponse.value = dummyLoggedInUser

        sharedViewModel.saveUser(dummyLoggedInUser)

        val actualResponse = sharedViewModel.user.getOrAwaitValue()

        Mockito.verify(userRepository).saveUser(dummyLoggedInUser)

        Assert.assertNotNull(actualResponse.token)
        Assert.assertEquals(dummyLoggedInUser, actualResponse)
    }

    @Test
    fun `Save user success and return false user livedata`() = runTest {
        val expectedResponse = MutableLiveData<User>()
        expectedResponse.value = dummyLoggedInUser

        sharedViewModel.saveUser(dummyLoggedOutUser)

        val actualResponse = sharedViewModel.user.getOrAwaitValue()

        Mockito.verify(userRepository).saveUser(dummyLoggedOutUser)

        Assert.assertNotEquals(dummyLoggedInUser, actualResponse)
    }

    @Test
    fun `Save user success and is fetchUser() return right user`() {
        sharedViewModel.saveUser(dummyLoggedInUser)
        Assert.assertNotNull(sharedViewModel.user.value)
        Assert.assertEquals(dummyLoggedInUser, sharedViewModel.user.value)

        val expectedResponse = MutableLiveData<User>()
        expectedResponse.value = sharedViewModel.user.value

        sharedViewModel.fetchUser()
        val actualResponse = sharedViewModel.user.value

        Mockito.verify(userRepository).getUser()

        Assert.assertEquals(dummyLoggedInUser, actualResponse)
    }

    @Test
    fun `Logout success then user data store must be empty`() = runTest {
        val expectedResponse = MutableLiveData<User>()
        expectedResponse.value = dummyLoggedOutUser

        sharedViewModel.saveUser(dummyLoggedInUser)
        Assert.assertNotNull(sharedViewModel.user.value)
        Assert.assertEquals(dummyLoggedInUser, sharedViewModel.user.value)
        Assert.assertNotEquals(expectedResponse, sharedViewModel.user.value)

        sharedViewModel.logOut()
        Assert.assertEquals("", sharedViewModel.user.value?.token)
        Assert.assertNotEquals(dummyLoggedInUser, sharedViewModel.user.value)

        val actualResponse = sharedViewModel.user.value

        Mockito.verify(userRepository).logOut()

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse.value, actualResponse)
    }
}