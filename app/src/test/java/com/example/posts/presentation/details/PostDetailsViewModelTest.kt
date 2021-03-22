package com.example.posts.presentation.details

import app.cash.turbine.test
import com.example.domain.model.Error
import com.example.domain.model.Post
import com.example.domain.model.Result
import com.example.domain.repository.PostsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.lang.Exception
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class PostDetailsViewModelTest {

    private lateinit var sut: PostDetailsViewModel

    private lateinit var postsRepository: PostsRepository

    private val dispatcher = TestCoroutineDispatcher()

    private val argumentReturnsSuccess = "arg_success"
    private val argumentReturnsFailure = "arg_failure"

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        postsRepository = Mockito.mock(PostsRepository::class.java)

        sut = PostDetailsViewModel(postsRepository)

        runBlocking {
            Mockito.doReturn(Result.Success(Post())).`when`(postsRepository).getPost(argumentReturnsSuccess)
            Mockito.doReturn(Result.Failure(listOf(Error.Server("Server error", Exception())))).`when`(postsRepository).getPost(argumentReturnsFailure)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun viewState_repositoryGetPostReturnsSuccess_viewStateIsInitialLoadingSuccess() = runBlockingTest {


        sut.viewState.test {
            sut.fetchPost(argumentReturnsSuccess)

            assert(expectItem() is ViewState.Initial)
            assert(expectItem() is ViewState.Loading)
            assert(expectItem() is ViewState.Success)

            cancel()
        }
    }

    @Test
    fun viewState_repositoryGetPostReturnsFailure_viewStateIsInitialLoadingFailure() = runBlockingTest {


        sut.viewState.test {
            sut.fetchPost(argumentReturnsFailure)

            assert(expectItem() is ViewState.Initial)
            assert(expectItem() is ViewState.Loading)
            assert(expectItem() is ViewState.Failure)

            cancel()
        }
    }

    @Test
    fun viewState_repositoryGetPostReturnsSuccessGetPostReturnsFailure_viewStateIsInitialLoadingSuccessLoadingFailure() = runBlockingTest {

        sut.viewState.test {
            sut.fetchPost(argumentReturnsSuccess)

            assert(expectItem() is ViewState.Initial)
            assert(expectItem() is ViewState.Loading)
            assert(expectItem() is ViewState.Success)

            sut.fetchPost(argumentReturnsFailure)

            assert(expectItem() is ViewState.Loading)
            assert(expectItem() is ViewState.Failure)

            cancel()
        }
    }

    @Test
    fun viewState_repositoryGetPostNotInvoked_viewStateIsInitial() = runBlockingTest {

        sut.viewState.test {

            assert(expectItem() is ViewState.Initial)

            cancel()
        }
    }
}
