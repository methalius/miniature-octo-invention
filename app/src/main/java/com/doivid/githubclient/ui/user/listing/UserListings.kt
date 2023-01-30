package com.doivid.githubclient.ui.user.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.doivid.githubclient.R
import com.doivid.githubclient.domain.UserListingEntry
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListingPage(
    listingViewModel: UserListingsViewModel = hiltViewModel(),
    onUserTapped: (String) -> Unit
) {
    val items by listingViewModel.users.collectAsState(emptyList())
    val paginationState by listingViewModel.paginationState
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.listing_title))
            })
        }
    ) {
        UserListing(
            modifier = Modifier.padding(it),
            users = items,
            paginationState = paginationState,
            onUserTapped = onUserTapped,
            onEndReached = {
                listingViewModel.fetchMoreUsers()
            })
    }
}

@OptIn(FlowPreview::class)
@Composable
fun UserListing(
    modifier: Modifier = Modifier,
    users: List<UserListingEntry>,
    paginationState: PaginationState,
    onUserTapped: (String) -> Unit,
    onEndReached: () -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = users,
            key = { it.id }
        ) { entry ->
            UserListingItem(
                modifier = Modifier.clickable {
                    onUserTapped(entry.login)
                }, entry
            )
        }

        when (paginationState) {
            PaginationState.LoadingFirstBatch -> item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            PaginationState.FetchingMore -> item {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
            }
            PaginationState.Error -> {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.generic_error_message_try_again),
                            Modifier.clickable { onEndReached() })
                    }
                }
            }
            PaginationState.Idle,
            PaginationState.Done -> {
            }
        }
    }

    LaunchedEffect(listState, paginationState) {
        snapshotFlow { listState.layoutInfo }.debounce(300).collectLatest { layoutInfo ->
            val lastVisibleItemIndex =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@collectLatest
            if ((layoutInfo.totalItemsCount - lastVisibleItemIndex.index) < 10 && paginationState == PaginationState.Idle) {
                onEndReached()
            }
        }
    }
}

@Composable
fun UserListingItem(
    modifier: Modifier,
    user: UserListingEntry
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .padding(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.avatarUrl)
                .crossfade(true)
                .placeholder(R.drawable.ic_profile)
                .fallback(R.drawable.ic_broken_image)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(height = 56.dp, width = 56.dp)
                .aspectRatio(1f)
                .clip(shape = MaterialTheme.shapes.medium),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = user.login,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}