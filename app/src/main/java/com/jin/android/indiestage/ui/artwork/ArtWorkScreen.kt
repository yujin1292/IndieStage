package com.jin.android.indiestage.ui.artwork

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ArtWorkScreen(
    onBackPress: () -> Unit,
    exhibitionId: String,
    artWorkId: String,
    mode: String
) {
    Column() {
        Text("ArtWork")
        Text("exhibitionId : $exhibitionId")
        Text("artWorkId : $artWorkId")
        Text("mode : $mode")
    }
}
