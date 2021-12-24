package com.jin.android.indiestage.ui.stage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Stage(
    onBackPress: () -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        StageContent(onBackPress = onBackPress)
    }
}

@Composable
fun StageContent(onBackPress: () -> Unit) {
    Row() {

        Text("hello!")
        Button(onClick = { onBackPress() }) {
            Text("Back")
        }
    }
}