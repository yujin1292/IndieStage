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
    object TicketBox : Screen("ticketBox/{exhibitionId}") {
        fun createRoute(exhibitionId: String) = "ticketBox/$exhibitionId"
    }

    object Stage : Screen("stage/{exhibitionId}/{mode}") {
        fun createRoute(exhibitionId: String, mode: String) = "stage/$exhibitionId/$mode"
    }

    object ArtWorkAsGuest : Screen("stage/{exhibitionId}/{artWorkId}/guest") {
        fun createRoute(exhibitionId: String, artWorkId: String) =
            "stage/$exhibitionId/$artWorkId/guest"
    }

    object ArtWork : Screen("stage/{exhibitionId}/{artWorkId}/auth") {
        fun createRoute(exhibitionId: String, artWorkId: String) =
            "stage/$exhibitionId/$artWorkId/auth"
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

    fun navigateToStage(exhibitionId: String, mode: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.popBackStack()
            val encodedId = Uri.encode(exhibitionId)
            val encodedMode = Uri.encode(mode)
            navController.navigate(Screen.Stage.createRoute(encodedId, encodedMode))
        }
    }

    fun navigateToTicketBox(exhibitionId: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            val encodedId = Uri.encode(exhibitionId)
            navController.navigate(Screen.TicketBox.createRoute(encodedId))
        }
    }

    fun navigateToArtWork(
        exhibitionId: String,
        artWorkId: String,
        mode: String,
        from: NavBackStackEntry
    ) {
        if (from.lifecycleIsResumed()) {
            val encodedExhibitionId = Uri.encode(exhibitionId)
            val encodedArtWorkId = Uri.encode(artWorkId)
            when (mode) {
                "auth" -> {
                    navController.navigate(
                        Screen.ArtWork.createRoute(
                            exhibitionId = encodedExhibitionId,
                            artWorkId = encodedArtWorkId,
                        )
                    )
                }
                "guest" -> {
                    navController.navigate(
                        Screen.ArtWorkAsGuest.createRoute(
                            exhibitionId = encodedExhibitionId,
                            artWorkId = encodedArtWorkId
                        )
                    )
                }
            }

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