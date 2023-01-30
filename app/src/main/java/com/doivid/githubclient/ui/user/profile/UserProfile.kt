package com.doivid.githubclient.ui.user.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.doivid.githubclient.R
import com.doivid.githubclient.domain.UserDetails
import com.doivid.githubclient.domain.UserListingEntry
import com.doivid.githubclient.ui.theme.GithubSampleClientTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(userListing: UserListingEntry) {
    val user = userListing.let {
        UserDetails(
            login = "daividssilverio",
            name = "Daivid S. Silverio",
            bio = "I like small things",
            id = 1,
            avatarUrl = "https://avatars.githubusercontent.com/u/2173493?v=4",
            company = "Toggl Track",
            location = "Tokyo, Japan",
            blog = "track.toggl.com",
            twitterHandle = "daividssilverio",
            email = "doivid@hey.com",
            followers = 23,
            following = 16,
            publicRepositories = 34
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { }, navigationIcon = {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back icon")
            })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            UserProfileHeader(avatarUrl = user.avatarUrl, name = user.name, username = user.login)
            user.bio?.let { bio -> Text(text = bio) }
            UserProfileExtras(user)
        }
    }
}

@Composable
fun UserProfileHeader(
    avatarUrl: String?,
    name: String,
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
                text = name,
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
        UserProfile(userListing = UserListingEntry("", "", ""))
    }
}