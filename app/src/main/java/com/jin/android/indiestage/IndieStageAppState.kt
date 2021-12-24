package com.jin.android.indiestage

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TicketBox : Screen("ticketBox")
    object Stage : Screen("stage/{stageUri}") {
        fun createRoute(stageUri: String) = "stage/$stageUri"
    }
    object ArtWork : Screen("stage/{stageUri}/{artWorkUri}/{mode}/{page}") {
        fun createRoute(stageUri: String, artWorkUri: String, mode: String, page: Int) =
            "stage/$stageUri/$artWorkUri/$mode/$page"
    }
}

@Composable
fun rememberIndieStageAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(navController, context) {
    IndieStageAppState(navController = navController, context = context)
}

class IndieStageAppState(
    private val context: Context,
    val navController: NavHostController
) {
    var isOnline by mutableStateOf(checkIfOnline())
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline()
    }

    fun navigateToStage(stageUri: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            val encodedUri = Uri.encode(stageUri)
            navController.navigate(Screen.Stage.createRoute(encodedUri))
        }
    }

    fun navigateToTicketBox(){

    }

    fun navigateToArtWork(
        stageUri: String,
        artWorkUri: String,
        mode: String,
        page: Int,
        from: NavBackStackEntry
    ) {
        if (from.lifecycleIsResumed()) {
            val encodedArtistUri = Uri.encode(stageUri)
            val encodedArtWorkUri = Uri.encode(artWorkUri)
            val encodedModeUri = Uri.encode(mode)

            navController.navigate(
                Screen.ArtWork.createRoute(
                    stageUri = encodedArtistUri,
                    artWorkUri = encodedArtWorkUri,
                    mode = encodedModeUri,
                    page = page
                )
            )
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    @Suppress("DEPRECATION")
    private fun checkIfOnline(): Boolean {
        val cm = ContextCompat.getSystemService(context, ConnectivityManager::class.java)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            cm?.activeNetworkInfo?.isConnectedOrConnecting == true
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED