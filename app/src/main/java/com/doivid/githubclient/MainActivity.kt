package com.doivid.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.doivid.githubclient.ui.theme.GithubSampleClientTheme
import com.doivid.githubclient.ui.user.listing.UsersListingPage
import com.doivid.githubclient.ui.user.profile.UserProfilePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubSampleClientTheme {
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
        NavHost(navController = navController, startDestination = "users") {
            composable("users") {
                UsersListingPage {
                    navController.navigate("users/$it")
                }
            }
            composable(
                "users/{userLogin}",
                arguments = listOf(navArgument("userLogin") { type = NavType.StringType })
            ) { backStackEntry ->
                UserProfilePage(
                    userLogin = backStackEntry.arguments?.getString("userLogin"),
                    onNavigationUp = {
                        navController.navigateUp()
                    })
            }
        }
    }
}