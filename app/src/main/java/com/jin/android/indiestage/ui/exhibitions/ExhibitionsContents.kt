package com.jin.android.indiestage.ui.exhibitions

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.firestore.Exhibition
import com.jin.android.indiestage.data.firestore.ExhibitionResponse
import com.jin.android.indiestage.data.firestore.OnSuccess

@Composable
fun ExhibitionsContents(
    viewModel: ExhibitionsViewModel,
    openedExhibitions: ExhibitionResponse,
    onClick: (String) -> Unit = {}
) {
    openedExhibitions.let {
        when (it) {
            is OnSuccess -> {
                val contents = it.querySnapshot?.toObjects(Exhibition::class.java)
                contents?.let {
                    ExhibitionsList(it , onClick)
                }
            }
            else -> {

            }
        }
    }
}

@Composable
fun ExhibitionsList(
    list: List<Exhibition>, onClick: (String) -> Unit ) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(
            items = list,
            itemContent = { item ->
                ExhibitionsCard(data = item) { onClick(item.id) }
            }
        )
    }
}


@Composable
fun ExhibitionsCard(data: Exhibition , onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .padding(5.dp)
            .clickable {onClick(data.id)}
    ) {
        Row() {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(5.dp)
                    .align(CenterVertically),
            ) {
                Image(
                    painter = rememberImagePainter(data = data.image),
                    contentDescription = data.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                if (data.isOpened.not()) {
                    Surface(
                        color = colorResource(id = R.color.shadow50),
                        modifier = Modifier.fillMaxSize()
                    ) {}
                }
            }

            Column(Modifier.padding(5.dp)) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(text = data.description, maxLines = 4,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}