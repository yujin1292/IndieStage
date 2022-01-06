package com.jin.android.indiestage.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jin.android.indiestage.R


@Composable
fun BorderChip(text: String) {
    Surface(
        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(R.color.text_background)
        )
    ) {
        Text(text = text, textAlign = TextAlign.Center,
            modifier=Modifier.padding(5.dp))
    }
}

@Composable
fun BorderChip(text: String, isSelected: Boolean) {
    Surface(
        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isSelected -> colorResource(R.color.teal_200)
                else -> colorResource(R.color.purple_500)
            }
        )
    ) {
        Text(text = text, textAlign = TextAlign.Center,
        modifier=Modifier.padding(5.dp))
    }
}

@Composable
fun ChipVerticalGrid(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var currentRow = 0
        var currentOrigin = IntOffset.Zero
        val spacingValue = spacing.toPx().toInt()
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentOrigin.x > 0f && currentOrigin.x + placeable.width > constraints.maxWidth) {
                currentRow += 1
                currentOrigin = currentOrigin.copy(x = 0, y = currentOrigin.y + placeable.height + spacingValue)
            }

            placeable to currentOrigin.also {
                currentOrigin = it.copy(x = it.x + placeable.width + spacingValue)
            }
        }

        layout(
            width = constraints.maxWidth,
            height = placeables.lastOrNull()?.run { first.height + second.y } ?: 0
        ) {
            placeables.forEach {
                val (placeable, origin) = it
                placeable.place(origin.x, origin.y)
            }
        }
    }
}
