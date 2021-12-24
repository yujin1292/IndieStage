package com.jin.android.indiestage

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jin.android.indiestage.ui.artwork.ArtWork
import com.jin.android.indiestage.ui.home.Home
import com.jin.android.indiestage.ui.stage.Stage

@ExperimentalPagerApi
@Composable
fun IndieStageApp(appState: IndieStageAppState = rememberIndieStageAppState()) {

    if (appState.isOnline) {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { backStackEntry ->
                Home(
                    navigateToStage = { stageUri ->
                        appState.navigateToStage(stageUri, backStackEntry)
                    }
                )
            }
            composable(Screen.Stage.route) { backStageEntry ->
                backStageEntry.arguments?.getString("stageUri")?.let {
                    Stage(
                        onBackPress = appState::navigateBack,
                        stageUri = it,
                        navigateToArtWork = { stageUri, artWorkUri, mode, page ->
                            appState.navigateToArtWork(
                                stageUri = stageUri,
                                artWorkUri = artWorkUri,
                                mode = mode,
                                page = page,
                                from = backStageEntry
                            )
                        }
                    )
                }
            }
            composable(Screen.ArtWork.route){
                ArtWork()
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
