package com.doivid.githubclient.ui.user.listing

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doivid.githubclient.data.GithubRepository
import com.doivid.githubclient.domain.UserListingEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListingsViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {
    private val _users: MutableStateFlow<List<UserListingEntry>> = MutableStateFlow(
        emptyList()
    )
    private var _since by mutableStateOf(0L)
    private var _paginationState = mutableStateOf(PaginationState.LoadingFirstBatch)

    val users: Flow<List<UserListingEntry>> = _users
    val paginationState: State<PaginationState> = _paginationState

    init {
        fetchMoreUsers()
    }

    fun fetchMoreUsers() = viewModelScope.launch {
        if (_paginationState.value == PaginationState.FetchingMore) return@launch
        _paginationState.value =
            if (_since == 0L) PaginationState.LoadingFirstBatch else PaginationState.FetchingMore
        try {
            val response = repository.getUsers(_since)
            val mightHaveMore = response.isNotEmpty()
            _users.update { currentList ->
                currentList + response.map {
                    UserListingEntry(
                        it.id,
                        it.login,
                        it.avatarUrl,
                        it.url
                    )
                }
            }
            if (mightHaveMore) {
                _since = response.last().id
            }
            _paginationState.value = if (mightHaveMore) PaginationState.Idle else PaginationState.Done
        } catch (ex: java.lang.Exception) {
            _paginationState.value = PaginationState.Error
        }
    }
}

enum class PaginationState {
    LoadingFirstBatch,
    FetchingMore,
    Idle,
    Error,
    Done
}