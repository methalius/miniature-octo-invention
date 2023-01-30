package com.doivid.githubclient.ui.user.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.doivid.githubclient.R
import com.doivid.githubclient.domain.GithubEvent
import com.doivid.githubclient.domain.UserDetails
import com.doivid.githubclient.ui.theme.GithubSampleClientTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilePage(
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
    userLogin: String?,
    onNavigationUp: () -> Unit
) {
    val userLoadableState =
        userProfileViewModel.userLoadable.collectAsState(initial = Loadable.Uninitialized)
    val eventsLoadableState =
        userProfileViewModel.eventsLoadable.collectAsState(initial = Loadable.Uninitialized)
    val userLoadable = userLoadableState.value
    val eventsLoadable = eventsLoadableState.value
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "back icon",
                        modifier = Modifier.clickable { onNavigationUp() })
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (userLoadable) {
                is Loadable.Error -> {
                    Text(
                        text = userLoadable.exception.localizedMessage
                            ?: "Oops, something went wrong, try again."
                    )
                }
                Loadable.Uninitialized,
                Loadable.Loading -> CircularProgressIndicator()
                is Loadable.Loaded -> {
                    UserProfile(userLoadable)
                }
            }

            when (eventsLoadable) {
                is Loadable.Error -> {
                    Text(
                        text = eventsLoadable.exception.localizedMessage
                            ?: "Oops, something went wrong, try again."
                    )
                }
                Loadable.Loading,
                Loadable.Uninitialized -> CircularProgressIndicator()
                is Loadable.Loaded -> UserActivity(eventsLoadable)
            }
        }
    }
    LaunchedEffect(userLogin) {
        userProfileViewModel.loadUserDetails(userLogin)
    }
}

@Composable
fun UserActivity(eventsLoadable: Loadable.Loaded<List<GithubEvent>>) {
    val events = eventsLoadable.value
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(events) {
            GithubEventListItem(it)
        }
    }
}

@Composable
fun GithubEventListItem(event: GithubEvent) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
        Text(text = "type: ${event.type}")
        Text(text = "who: ${event.actor.login}")
        Text(text = "where: ${event.repository?.name}")
        Text(text = "when: ${event.createdAt}")
    }
}

@Composable
fun UserProfile(userLoadable: Loadable.Loaded<UserDetails>) {
    val user = userLoadable.value
    UserProfileHeader(avatarUrl = user.avatarUrl, name = user.name, username = user.login)
    user.bio?.let { bio -> Text(text = bio) }
    UserProfileExtras(user)
}

@Composable
fun UserProfileHeader(
    avatarUrl: String?,
    name: String?,
    username: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl)
                .crossfade(true)
                .placeholder(R.drawable.baseline_portrait_24)
                .fallback(R.drawable.baseline_broken_image_24)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(height = 98.dp, width = 98.dp)
                .aspectRatio(1f)
                .clip(shape = MaterialTheme.shapes.medium),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name ?: username,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = username,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
fun UserProfileExtras(
    userDetails: UserDetails
) {
    Column {
        userDetails.apply {
            company?.let {
                UserProfileExtra(description = it, type = ProfileExtra.Company)
            }
            blog?.let {
                UserProfileExtra(description = it, type = ProfileExtra.Blog)
            }
            email?.let {
                UserProfileExtra(description = it, type = ProfileExtra.Email)
            }
            twitterHandle?.let {
                UserProfileExtra(description = it, type = ProfileExtra.Twitter)
            }
            (followers to following).takeIf { it.first > 0 || it.second > 0 }?.let {
                UserProfileNetwork(followers = it.first, following = it.second)
            }
        }
    }
}

enum class ProfileExtra {
    Company,
    Blog,
    Twitter,
    Network,
    Email,
}

fun ProfileExtra.icon() = when (this) {
    ProfileExtra.Company -> Icons.Outlined.Build
    ProfileExtra.Blog -> Icons.Outlined.Info
    ProfileExtra.Twitter -> Icons.Outlined.Phone
    ProfileExtra.Network -> Icons.Outlined.Person
    ProfileExtra.Email -> Icons.Outlined.Email
}

@Composable
fun UserProfileExtra(description: String, type: ProfileExtra) {
    Row {
        Icon(type.icon(), null)
        Text(text = description)
    }
}


@Composable
fun UserProfileNetwork(followers: Int, following: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(ProfileExtra.Network.icon(), null)
        if (followers > 0) {
            Text(text = "$followers followers")
        }
        if (followers > 0 && following > 0) {
            Canvas(modifier = Modifier.size(16.dp), onDraw = {
                drawCircle(Color.Black, radius = 2.dp.toPx())
            })
        }
        if (following > 0) {
            Text(text = "$following following")
        }
    }
}

@Preview
@Composable
fun UserProfilePagePreview() {
    GithubSampleClientTheme(false) {
        UserProfile(
            Loadable.Loaded(
                UserDetails(
                    "daividssilverio",
                    "Daivid",
                    "Me",
                    1,
                    "http://abc",
                    "company",
                    "tokyo, japan",
                    "none",
                    "daividssilverio",
                    "doivid@hey.com",
                    12,
                    24,
                    3
                )
            )
        )
    }
}