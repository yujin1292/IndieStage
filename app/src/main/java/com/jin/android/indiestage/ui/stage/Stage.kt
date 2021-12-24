package com.jin.android.indiestage.ui.stage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Stage(
    onBackPress: () -> Unit,
    navigateToArtWork: (stageUri: String, artWorkUri: String, mode: String, page: Int) -> Unit,
    stageUri: String,
    viewModel: StageViewModel = viewModel()
) {
    Surface(Modifier.fillMaxSize()) {
        StageContent(onBackPress = onBackPress, stageUri = stageUri, navigateToArtWork = navigateToArtWork)
    }
}

@Composable
fun StageContent(
    onBackPress: () -> Unit,
    stageUri: String,
    navigateToArtWork: (
        stageUri: String,
        artWorkUri: String,
        mode: String,
        page: Int
    ) -> Unit
) {
    Column() {

        Text(stageUri)
        Button(onClick = { onBackPress() }) {
            Text("Back")
        }
        Button(onClick = { navigateToArtWork("a","b","c",0) }) {
            Text("GoArtWork")
        }
    }
}

@Composable
fun StageAppBar(
    onBackPress: () -> Unit
) {


}

@Composable
fun StageArtistIntro() {

}

@Composable
fun WorkList() {

}

@Composable
fun WorkElement() {

}