package com.jin.android.indiestage.ui.home.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.firestore.Exhibition
import com.jin.android.indiestage.ui.components.midGap
import com.jin.android.indiestage.ui.theme.IndieStageTheme

@Composable
fun TabLayouts(tabItems: List<TabItem>) {
    tabItems.map { item ->
        TabItemScreen(item)
    }
}

@Composable
fun TabItemScreen(tabItem: TabItem) {
    val listState = rememberLazyListState()
    val isClosed = tabItem.title == stringResource(id = R.string.ready_exhibitions)

    Column {
        Text(
            text = tabItem.title, style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 12.dp, top = 5.dp)
        )
        MoreExhibitions(tabItem.title, tabItem.getMoreInfoClicked)
        LazyRow(state = listState) {

            items(
                items = tabItem.itemList,
                itemContent = { item ->
                    PosterItem(
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable {
                                if (!isClosed) tabItem.onItemClicked(item.id)
                            },
                        image = item.image,
                        title = item.title,
                        description = item.description,
                        isClosed = isClosed
                    )
                }
            )
        }
    }
}

@Composable
fun PosterItem(
    modifier: Modifier,
    image: String,
    title: String,
    description: String,
    isClosed: Boolean
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .aspectRatio(0.75f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            ) {
                Image(
                    painter = rememberImagePainter(data = image),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                if (isClosed) {
                    Surface(
                        color = colorResource(id = R.color.shadow50),
                        modifier = Modifier.fillMaxSize()
                    ) {}
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.caption,
                    maxLines = 2
                )
            }

        }
    }
}

@Composable
fun MoreExhibitions(
    title: String,
    onClick: (String) -> Unit
) {
    Row {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(Modifier.clickable { onClick(title) })
        {
            Text(
                text = "더 보러가기",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(17.dp)
            )
            Icon(
                imageVector = Icons.Outlined.ArrowRight, contentDescription = "more Info",
                Modifier.size(17.dp).align(CenterVertically)
            )
        }
    }
}


data class TabItem(
    val title: String,
    val itemList: List<Exhibition>,
    val getMoreInfoClicked: (String) -> Unit,
    val onItemClicked: (String) -> Unit,
)


@Composable
@Preview
fun TabLayoutPreview() {
    IndieStageTheme() {
        val f: (String) -> Unit = {  }
        TabLayouts(
            listOf(
                TabItem(
                    title = "title", itemList = mutableListOf(Exhibition()),
                    getMoreInfoClicked = f, onItemClicked = f
                )
            )
        )
    }
}