package com.zurche.topreddit.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zurche.topreddit.home.data.Resource
import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.TopStoriesResponse
import com.zurche.topreddit.home.data.remote.service.TopStoryData
import com.zurche.topreddit.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var topPostRepository: TopPostRepository

    @Mock
    private lateinit var topPostsObserver: Observer<Resource<List<ChildrenData>>>

    @Test
    fun `Given top post fetch is successful when retrieving top posts then success with list is observed`() {
        val (mockTopPostResponse, mockResponse) = mockEmptyResponse()
        every { mockResponse.body() } returns mockTopPostResponse

        testCoroutineRule.runBlockingTest {

            doReturn(mockResponse)
                    .`when`(topPostRepository)
                    .getTopPosts()

            val viewModel = PostListViewModel(topPostRepository)
            viewModel.getTopPosts().observeForever(topPostsObserver)

            verify(topPostRepository).getTopPosts()
            verify(topPostsObserver).onChanged(Resource.success(emptyList()))
            viewModel.getTopPosts().removeObserver(topPostsObserver)
        }
    }

    @Test
    fun `Given top post fetch fails when retrieving top posts then error with null and error message is observed`() {
        val (mockTopPostResponse, mockResponse) = mockEmptyResponse()
        every { mockResponse.body() } returns mockTopPostResponse
        every { mockResponse.isSuccessful } returns false

        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"

            doThrow(RuntimeException(errorMessage))
                    .`when`(topPostRepository)
                    .getTopPosts()

            val viewModel = PostListViewModel(topPostRepository)
            viewModel.getTopPosts().observeForever(topPostsObserver)

            verify(topPostRepository).getTopPosts()
            verify(topPostsObserver).onChanged(Resource.error(null, "Error Message For You"))
            viewModel.getTopPosts().removeObserver(topPostsObserver)
        }
    }

    @Test
    fun `Given top post fetch request times out when trying to get data from backend then cannot load error is observed`() {
        val (mockTopPostResponse, mockResponse) = mockEmptyResponse()
        every { mockResponse.body() } returns mockTopPostResponse
        every { mockResponse.isSuccessful } returns false

        testCoroutineRule.runBlockingTest {
            val timeoutMessage = "Timeout Exception"

            given(topPostRepository.getTopPosts()).willAnswer {
                throw TimeoutException(timeoutMessage)
            }

            val viewModel = PostListViewModel(topPostRepository)
            viewModel.getTopPosts().observeForever(topPostsObserver)

            verify(topPostRepository).getTopPosts()
            verify(topPostsObserver).onChanged(Resource.cannotLoad(timeoutMessage))
            viewModel.getTopPosts().removeObserver(topPostsObserver)
        }
    }

    @Test
    fun `Given top post fetch request cannot reach URL when trying to get data from backend then cannot load error is observed`() {
        val (mockTopPostResponse, mockResponse) = mockEmptyResponse()
        every { mockResponse.body() } returns mockTopPostResponse
        every { mockResponse.isSuccessful } returns false

        testCoroutineRule.runBlockingTest {
            val timeoutMessage = "Unknown Host Exception"

            given(topPostRepository.getTopPosts()).willAnswer {
                throw UnknownHostException(timeoutMessage)
            }

            val viewModel = PostListViewModel(topPostRepository)
            viewModel.getTopPosts().observeForever(topPostsObserver)

            verify(topPostRepository).getTopPosts()
            verify(topPostsObserver).onChanged(Resource.cannotLoad(timeoutMessage))
            viewModel.getTopPosts().removeObserver(topPostsObserver)
        }
    }

    private fun mockEmptyResponse(): Pair<TopStoriesResponse, Response<TopStoriesResponse>> {
        val mockTopStory = mockk<TopStoryData>()
        every { mockTopStory.children } returns emptyList()
        val mockTopPostResponse = mockk<TopStoriesResponse>()
        every { mockTopPostResponse.data } returns mockTopStory
        val mockResponse = mockk<Response<TopStoriesResponse>>()
        return Pair(mockTopPostResponse, mockResponse)
    }

}