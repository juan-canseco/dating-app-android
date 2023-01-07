package com.org.datingapp.features.auth.router

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.MainDispatcherRule
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.auth.Authenticator
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RouterViewModelTests : AndroidTest() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private lateinit var routerViewModel : RouterViewModel

    private lateinit var router : Router

    @MockK
    private lateinit var authenticator : Authenticator

    @MockK
    private lateinit var eventObserver : Observer<Event<RouterViewModel.Navigation>?>

    @Before
    fun setup() {
        router = Router(authenticator)
        routerViewModel = RouterViewModel(router)
    }


    @Test
    fun `routerViewModel when start with userLoggedIn and userProfileComplete should update livedata with userLoggedIn`() = runTest {

        coEvery { authenticator.userLoggedIn() } returns true
        coEvery { authenticator.profileComplete() } returns true

        every { eventObserver.onChanged(Event(RouterViewModel.Navigation.ShowUserLoggedIn))} answers {callOriginal()}

        routerViewModel.events.observeForever(eventObserver)
        routerViewModel.start()

        verify { eventObserver.onChanged(Event(RouterViewModel.Navigation.ShowUserLoggedIn))}
    }

    @Test
    fun `routerViewModel when start with userLoggedIn not logged in should update livedata with userNotLoggedIn`() = runTest {

        coEvery { authenticator.userLoggedIn() } returns false

        every { eventObserver.onChanged(Event(RouterViewModel.Navigation.ShowUserNotLoggedIn))} answers {callOriginal()}

        routerViewModel.events.observeForever(eventObserver)
        routerViewModel.start()

        verify { eventObserver.onChanged(Event(RouterViewModel.Navigation.ShowUserNotLoggedIn))}
    }

    @Test
    fun `routerViewModel when start with userLoggedIn and userProfileIncomplete should update livedata with userProfileIncomplete`() = runTest {

        coEvery { authenticator.userLoggedIn() } returns true
        coEvery { authenticator.profileComplete() } returns false

        every { eventObserver.onChanged(Event(RouterViewModel.Navigation.ShowUserProfileIncomplete))} answers {callOriginal()}

        routerViewModel.events.observeForever(eventObserver)
        routerViewModel.start()

        verify { eventObserver.onChanged(Event(RouterViewModel.Navigation.ShowUserProfileIncomplete))}
    }







}