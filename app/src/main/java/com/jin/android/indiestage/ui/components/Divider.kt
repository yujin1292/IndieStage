package com.jin.android.indiestage.ui.components


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IndieStageDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray.copy(alpha = 0.5f),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier.padding(start=5.dp, end=5.dp),
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}
