package com.doivid.githubclient.ui.user.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doivid.githubclient.data.GithubRepository
import com.doivid.githubclient.domain.GithubEvent
import com.doivid.githubclient.domain.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Loadable<out T> {
    open operator fun invoke(): T? = null

    data class Loaded<out T>(val value: T) : Loadable<T>() {
        final override fun invoke(): T = value
    }

    object Loading : Loadable<Nothing>()
    object Uninitialized : Loadable<Nothing>()
    data class Error<out T>(val exception: Exception) : Loadable<T>()
}


@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
    private val _userLoadable: MutableStateFlow<Loadable<UserDetails>> =
        MutableStateFlow(Loadable.Uninitialized)
    private val _eventsLoadable: MutableStateFlow<Loadable<List<GithubEvent>>> =
        MutableStateFlow(Loadable.Uninitialized)

    val userLoadable: Flow<Loadable<UserDetails>> = _userLoadable
    val eventsLoadable: Flow<Loadable<List<GithubEvent>>> = _eventsLoadable

    fun loadUserDetails(userLogin: String?) = viewModelScope.launch {
        if (userLogin.isNullOrBlank()) {
            _userLoadable.value = Loadable.Error(Exception("Invalid/unknown user"))
            return@launch
        }
        _userLoadable.value = Loadable.Loading
        _eventsLoadable.value = Loadable.Loading
        val getUser = async { repository.getUser(userLogin) }
        val getEvents = async { repository.getEventsForUser(userLogin) }

        try {
            _userLoadable.value = Loadable.Loaded(getUser.await())
        } catch (ex: Exception) {
            Log.e("xxaa", "failed to load user", ex)
            _userLoadable.value = Loadable.Error(ex)
        }

        try {
            _eventsLoadable.value = Loadable.Loaded(getEvents.await())
        } catch (ex: Exception) {
            Log.d("xxaa", "failed to load user's events", ex)
            _eventsLoadable.value = Loadable.Error(ex)
        }
    }
}