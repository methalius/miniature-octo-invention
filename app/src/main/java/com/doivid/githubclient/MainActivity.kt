package com.doivid.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doivid.githubclient.domain.UserListingEntry
import com.doivid.githubclient.ui.theme.GithubSampleClientTheme
import com.doivid.githubclient.ui.user.listing.UserListing
import com.doivid.githubclient.ui.user.listing.UsersListingPage
import com.doivid.githubclient.ui.user.profile.UserProfile
import dagger.hilt.android.AndroidEntryPoint

val users = listOf(
    UserListingEntry(1,"daividssilverio", "https://avatars.githubusercontent.com/u/2173493?v=4", "")
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubSampleClientTheme {
                // A surface container using the 'background' color from the theme
                App()
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                UsersListingPage {
                    navController.navigate("profile")
                }
            }
            composable("profile") {
                UserProfile(users.first())
            }
        }
    }
}