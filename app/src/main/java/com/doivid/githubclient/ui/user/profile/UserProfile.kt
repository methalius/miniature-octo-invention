package com.doivid.githubclient.ui.user.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.doivid.githubclient.R
import com.doivid.githubclient.domain.*
import com.doivid.githubclient.ui.theme.GithubSampleClientTheme
import java.time.OffsetDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilePage(
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
    userLogin: String?,
    onNavigationUp: () -> Unit
) {
    val userLoadableState = userProfileViewModel.userLoadable.collectAsState(
        initial = Loadable.Uninitialized
    )
    val eventsLoadableState = userProfileViewModel.eventsLoadable.collectAsState(
        initial = Loadable.Uninitialized
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_icon_content_description),
                        modifier = Modifier.clickable { onNavigationUp() })
                }
            )
        }
    ) {
        UserProfilePageContent(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            userLoadable = userLoadableState.value,
            eventsLoadable = eventsLoadableState.value
        )
    }
    LaunchedEffect(userLogin) {
        userProfileViewModel.loadUserDetails(userLogin)
    }
}

@Composable
fun UserProfilePageContent(
    modifier: Modifier = Modifier,
    userLoadable: Loadable<UserDetails>,
    eventsLoadable: Loadable<List<GithubEvent>>
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        when (userLoadable) {
            is Loadable.Error -> {
                item {
                    Text(
                        text = userLoadable.exception.localizedMessage
                            ?: stringResource(R.string.generic_error_message_try_again)
                    )
                }
            }
            Loadable.Uninitialized,
            Loadable.Loading -> item {
                CircularProgressIndicator()
            }
            is Loadable.Loaded -> {
                this.userProfile(userLoadable.value)
            }
        }

        when (eventsLoadable) {
            is Loadable.Error -> {
                item {
                    Text(
                        text = eventsLoadable.exception.localizedMessage
                            ?: stringResource(R.string.generic_error_message_try_again)
                    )
                }
            }
            Loadable.Loading,
            Loadable.Uninitialized -> item {
                CircularProgressIndicator()
            }
            is Loadable.Loaded -> {
                if (eventsLoadable.value.isNotEmpty()) {
                    this.userActivity(eventsLoadable.value)
                }
            }
        }
    }
}

fun LazyListScope.userActivity(events: List<GithubEvent>) {
    item {
        Spacer(modifier = Modifier.height(12.dp))
        UserProfileExtraWrapper {
            Text(text = stringResource(R.string.activity))
        }
    }
    items(events) {
        GithubEventListItem(it)
    }
}

@Composable
fun GithubEventListItem(event: GithubEvent) {
    UserProfileExtraWrapper {
        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "type: ${event.type}")
            Text(text = "who: ${event.actor.login}")
            Text(text = "where: ${event.repository?.name}")
            Text(text = "when: ${event.createdAt}")
        }
    }
}

fun LazyListScope.userProfile(user: UserDetails) {
    item {
        UserProfileHeader(avatarUrl = user.avatarUrl, name = user.name, username = user.login)
        Spacer(modifier = Modifier.height(8.dp))
    }
    user.bio?.let { bio ->
        item {
            UserProfileExtraWrapper { Text(text = bio) }
        }
    }
    userProfileExtras(user)
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
            .padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
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

fun LazyListScope.userProfileExtras(
    userDetails: UserDetails
) {
    userDetails.apply {
        if (!company.isNullOrBlank()) {
            item {
                UserProfileExtra(description = company, type = ProfileExtra.Company)
            }
        }
        if (!blog.isNullOrBlank()) {
            item {
                UserProfileExtra(description = blog, type = ProfileExtra.Blog)
            }
        }
        if (!email.isNullOrBlank()) {
            item {
                UserProfileExtra(description = email, type = ProfileExtra.Email)
            }
        }
        if (!twitterHandle.isNullOrBlank()) {
            item {
                UserProfileExtra(description = twitterHandle, type = ProfileExtra.Twitter)
            }
        }
        (followers to following).takeIf { it.first > 0 || it.second > 0 }?.let {
            item {
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
    Email
}

fun ProfileExtra.iconResId() = when (this) {
    ProfileExtra.Company -> R.drawable.ic_business
    ProfileExtra.Blog -> R.drawable.ic_link
    ProfileExtra.Twitter -> R.drawable.ic_twitter
    ProfileExtra.Network -> R.drawable.ic_group
    ProfileExtra.Email -> R.drawable.ic_email
}

@Composable
fun UserProfileExtra(description: String, type: ProfileExtra) {
    UserProfileExtraWrapper(
        icon = { Icon(painterResource(id = type.iconResId()), null) },
        content = { Text(text = description) }
    )
}

@Composable
fun UserProfileExtraWrapper(
    icon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(Modifier.padding(horizontal = 16.dp)) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        content()
    }
}

@Composable
fun UserProfileNetwork(followers: Int, following: Int) {
    UserProfileExtraWrapper(icon = {
        Icon(painterResource(id = ProfileExtra.Network.iconResId()), null)
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (followers > 0) {
                Text(
                    text = LocalContext.current.resources.getQuantityString(
                        R.plurals.followers,
                        followers,
                        followers
                    )
                )
            }
            if (followers > 0 && following > 0) {
                val circleColor = LocalContentColor.current
                Canvas(modifier = Modifier.size(16.dp), onDraw = {
                    drawCircle(circleColor, radius = 2.dp.toPx())
                })
            }
            if (following > 0) {
                Text(text = stringResource(id = R.string.following, following))
            }
        }
    }
}

@Preview
@Composable
fun UserProfilePagePreview() {
    GithubSampleClientTheme(false) {
        UserProfilePageContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
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
            ),
            Loadable.Loaded(
                listOf(
                    GithubEvent(
                        GithubEventType.CreateEvent,
                        UserListingEntry(1, "daividssilverio", "http://abc", "daividssilverio"),
                        Repository(1, "something", "something"),
                        OffsetDateTime.now()
                    ),
                    GithubEvent(
                        GithubEventType.PublicEvent,
                        UserListingEntry(1, "daividssilverio", "http://abc", "daividssilverio"),
                        Repository(1, "something", "something"),
                        OffsetDateTime.now()
                    )
                )
            )
        )
    }
}