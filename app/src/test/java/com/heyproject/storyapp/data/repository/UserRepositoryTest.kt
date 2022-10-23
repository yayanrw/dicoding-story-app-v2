package com.heyproject.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heyproject.storyapp.data.FakeStoryDatabase
import com.heyproject.storyapp.data.FakeStoryService
import com.heyproject.storyapp.data.datasource.local.database.StoryDatabase
import com.heyproject.storyapp.data.datasource.local.datastore.UserDataStore
import com.heyproject.storyapp.data.datasource.remote.api.StoryService
import com.heyproject.storyapp.domain.model.toDomain
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
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Written by Yayan Rahmat Wijaya on 10/23/2022 07:06
 * Github : https://github.com/yayanrw
 */

@ExperimentalCoroutinesApi
class UserRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userDataStore: UserDataStore

    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyService: StoryService
    private lateinit var userRepository: UserRepository

    private val dummyEmail = "yayanraw@gmail.com"
    private val dummyFalseEmail = "falseemail@gmail.com"
    private val dummyName = "Yayan Rahmat Wijaya"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        storyDatabase = FakeStoryDatabase()
        storyService = FakeStoryService()
        userDataStore = mock(UserDataStore::class.java)
        userRepository = UserRepository(storyService, userDataStore)
    }

    @Test
    fun `Login successfully with right credentials`() = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponses(true)
        val actualResponse = userRepository.logIn(dummyEmail, dummyPassword)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue((actualResponse.value is Result.Success))
            Assert.assertEquals(
                expectedResponse.loginResult?.toDomain(),
                (actualResponse.value as Result.Success).data
            )
        }
    }

    @Test
    fun `Login unsuccessfully because wrong credentials`() = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponses(false)
        val actualResponse = userRepository.logIn(dummyFalseEmail, dummyPassword)

        actualResponse.observeForTesting {
            Assert.assertNull((actualResponse.value as Result.Error).data)
            Assert.assertTrue((actualResponse.value is Result.Error))
            Assert.assertEquals(
                expectedResponse.loginResult?.toDomain(),
                (actualResponse.value as Result.Error).data
            )
        }
    }

    @Test
    fun `Register successfully with unregistered email`() = runTest {
        val expectedResponse = DataDummy.generateDummyRegisterResponse(true)
        val actualResponse = userRepository.register(dummyName, dummyEmail, dummyPassword)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue((actualResponse.value is Result.Success))
            Assert.assertEquals(
                expectedResponse,
                (actualResponse.value as Result.Success).data
            )
        }
    }

    @Test
    fun `Register unsuccessfully with registered email`() = runTest {
        val expectedResponse = DataDummy.generateDummyRegisterResponse(false)
        val actualResponse = userRepository.register(dummyName, dummyFalseEmail, dummyPassword)

        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue((actualResponse.value is Result.Error))
            Assert.assertEquals(
                expectedResponse.message,
                (actualResponse.value as Result.Error).message
            )
        }
    }

    @Test
    fun `Save user datastore and check getUser() is filled and same value`() = runTest {
        userRepository.saveUser(DataDummy.generateDummyLoggedInUser())
        verify(userDataStore).saveUser(DataDummy.generateDummyLoggedInUser())

        userRepository.getUser()
        verify(userDataStore).getUser()
    }

    @Test
    fun `clearUserToken should be called only once when logout`() = runTest {
        userRepository.logOut()

        verify(userDataStore).logOut()
    }
}