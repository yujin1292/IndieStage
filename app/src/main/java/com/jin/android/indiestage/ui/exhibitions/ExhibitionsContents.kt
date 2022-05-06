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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.ktx.toObject
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.firestore.Exhibition
import com.jin.android.indiestage.data.firestore.ExhibitionResponse
import com.jin.android.indiestage.data.firestore.OnSuccess

@Composable
fun ExhibitionsContents(
    viewModel: ExhibitionsViewModel,
    exhibitions: ExhibitionResponse,
    onClick: (String) -> Unit = {}
) {
    exhibitions.let {
        when (it) {
            is OnSuccess -> {
                val list = mutableListOf<Exhibition>()

                it.querySnapshot?.let{ snapShot ->
                    for( query in snapShot){
                        val item = query.toObject<Exhibition>()
                        item.isOpened = query["isOpened"] == true
                        list.add(item)
                    }
                }
                ExhibitionsList(list, onClick)

            }
            else -> {

            }
        }
    }
}

@Composable
fun ExhibitionsList(
    list: List<Exhibition>, onClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(
            items = list,
            itemContent = { item ->
                ExhibitionsCard(data = item, onClick = onClick)
            }
        )
    }
}


@Composable
fun ExhibitionsCard(data: Exhibition, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .padding(5.dp)
            .clickable {
                if (data.isOpened) onClick(data.id)
                else onClick("준비 중 입니다!")
            }
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
                if (data.isOpened.not() ) {
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
                Text(
                    text = data.description, maxLines = 4,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}