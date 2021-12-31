package com.jin.android.indiestage

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.jin.android.indiestage.ui.artwork.ArtWorkScreen
import com.jin.android.indiestage.ui.artwork.ArtWorkScreenAsGuest
import com.jin.android.indiestage.ui.home.Home
import com.jin.android.indiestage.ui.stage.Stage
import com.jin.android.indiestage.ui.ticketbox.TicketBox
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun IndieStageApp(
    appState: IndieStageAppState = rememberIndieStageAppState()
) {
    if (appState.isOnline) {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { backStackEntry ->
                Home(
                    navigateToTicketBox = { exhibitionId ->
                        appState.navigateToTicketBox(exhibitionId, backStackEntry)
                    }
                )
            }
            composable(Screen.Stage.route) { backStageEntry ->
                backStageEntry.arguments?.getString("exhibitionId")?.let { exhibitionId ->
                    backStageEntry.arguments?.getString("mode")?.let { mode ->
                        Stage(
                            onBackPress = appState::navigateBack,
                            exhibitionId = exhibitionId,
                            navigateToArtWork = { exhibitionId, artWorkId ->
                                appState.navigateToArtWork(
                                    exhibitionId = exhibitionId,
                                    artWorkId = artWorkId,
                                    mode = mode,
                                    from = backStageEntry
                                )
                            }
                        )
                    }


                }
            }
            composable(Screen.TicketBox.route) { navBackStackEntry ->
                navBackStackEntry.arguments?.getString("exhibitionId")?.let {
                    TicketBox(
                        onBackPress = appState::navigateBack,
                        navigateToStage = { exhibitionId, mode ->
                            appState.navigateToStage(exhibitionId, mode, navBackStackEntry)
                        },
                        exhibitionId = it
                    )
                }
            }
            composable(Screen.ArtWork.route) { backStageEntry ->

                val exhibitionId: String = backStageEntry.arguments?.getString("exhibitionId").run {
                    this ?: "null"
                }
                val artWorkId: String = backStageEntry.arguments?.getString("artWorkId").run {
                    this ?: "null"
                }

                ArtWorkScreen(
                    onBackPress = appState::navigateBack,
                    exhibitionId = exhibitionId,
                    artWorkId = artWorkId
                )

            }
            composable(Screen.ArtWorkAsGuest.route) { backStageEntry ->

                val exhibitionId: String = backStageEntry.arguments?.getString("exhibitionId").run {
                    this ?: "null"
                }
                val artWorkId: String = backStageEntry.arguments?.getString("artWorkId").run {
                    this ?: "null"
                }

                ArtWorkScreenAsGuest(
                    onBackPress = appState::navigateBack,
                    exhibitionId = exhibitionId,
                    artWorkId = artWorkId
                )
            }
        }
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry_label))
            }
        }
    )
}
