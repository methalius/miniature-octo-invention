package com.doivid.githubclient.ui.user.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.doivid.githubclient.R
import com.doivid.githubclient.domain.UserListingEntry
import com.doivid.githubclient.ui.theme.GithubSampleClientTheme
import com.doivid.githubclient.users

@Composable
fun UserListingItem(
    modifier: Modifier,
    user: UserListingEntry
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.avatarUrl)
                .crossfade(true)
                .placeholder(R.drawable.baseline_portrait_24)
                .fallback(R.drawable.baseline_broken_image_24)
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


@Composable
fun UserListing(
    modifier: Modifier = Modifier,
    onUserTapped: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(users) {
            UserListingItem(
                modifier = Modifier.clickable {
                    onUserTapped(it.profileUrl)
                }, it
            )
        }
    }
}



@Preview(showBackground = true, heightDp = 400)
@Composable
fun UserListingPreviewLightMode() {
    GithubSampleClientTheme(false) {
        UserListing {}
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun UserListingPreviewDarkMode() {
    GithubSampleClientTheme(true) {
        UserListing {}
    }
}