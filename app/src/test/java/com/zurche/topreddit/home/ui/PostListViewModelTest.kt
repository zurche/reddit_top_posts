package com.zurche.topreddit.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zurche.topreddit.home.data.Resource
import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.TopStoriesResponse
import com.zurche.topreddit.home.data.remote.service.TopStoryData
import com.zurche.topreddit.home.ui.PostListViewModel
import com.zurche.topreddit.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

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

    @Before
    fun setUp() {
        // do something if required
    }

    @Test
    fun `given server response 200 when fetch should return success`() {
        val mockTopStory = mockk<TopStoryData>()
        every { mockTopStory.children } returns emptyList()
        val mockTopPostResponse = mockk<TopStoriesResponse>()
        every { mockTopPostResponse.data } returns mockTopStory
        val mockResponse = mockk<Response<TopStoriesResponse>>()
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
    fun `given server response error when fetch should return error` () {
        val mockTopStory = mockk<TopStoryData>()
        every { mockTopStory.children } returns emptyList()
        val mockTopPostResponse = mockk<TopStoriesResponse>()
        every { mockTopPostResponse.data } returns mockTopStory
        val mockResponse = mockk<Response<TopStoriesResponse>>()
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

    @After
    fun tearDown() {
        // do something if required
    }

}