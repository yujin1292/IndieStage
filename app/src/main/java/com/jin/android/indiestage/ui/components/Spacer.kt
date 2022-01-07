package com.jin.android.indiestage.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


val shortGap = 8.dp
val midGap = 16.dp

@Composable
fun ShortSpacer(modifier: Modifier = Modifier.height(shortGap)) {
    Spacer(modifier)
}

@Composable
fun MidSpacer(modifier: Modifier = Modifier.height(midGap)) {
    Spacer(modifier)
}