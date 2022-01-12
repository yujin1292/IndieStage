package com.jin.android.indiestage.ui.home.checkedin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import com.jin.android.indiestage.data.room.ExhibitionEntity
import com.jin.android.indiestage.ui.home.HomeViewModel
import com.jin.android.indiestage.util.lerp
import kotlin.math.absoluteValue

@ExperimentalPagerApi
@Composable
fun CheckedInScreen(viewModel: HomeViewModel) {

    val bookmarkedList = viewModel.bookmarkedList.observeAsState(arrayListOf()).value
    val checkedInList = viewModel.checkedInList.observeAsState(arrayListOf()).value

    val listState = rememberLazyListState()


    LazyColumn(state = listState) {
        item {
            Text(
                text = "즐겨찾기한 전시", style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(12.dp)
            )
        }
        item {
            CheckedInPager(bookmarkedList)
        }
        item {
            Text(
                text = "사용한 티켓", style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(12.dp)
            )
        }
        items(
            items = checkedInList,
            itemContent = { item -> Ticket(item) }
        )
        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}

@ExperimentalPagerApi
@Composable
fun CheckedInPager(exhibitionList: List<ExhibitionEntity>) {
    val pagerState = rememberPagerState()
    Column() {
        HorizontalPager(
            count = exhibitionList.size,
            state = pagerState,
            modifier = Modifier
        ) { page ->
            Card(
                Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions

                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .padding(start = 64.dp, end = 64.dp)
                    .aspectRatio(0.7f)
            )
            { CheckedInPage(exhibitionList[page]) }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(12.dp)
                .align(CenterHorizontally)
        )
    }

}

@Composable
fun CheckedInPage(item: ExhibitionEntity) {
    Column() {
        Image(
            painter = rememberImagePainter(data = item.image),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = item.description,
            )
        }
    }

}

@Composable
fun Ticket(exhibitionEntity: ExhibitionEntity) {
    Card(modifier = Modifier.padding(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5f)
                .background(androidx.compose.ui.graphics.Color.Yellow)
        ) {
            Text(
                text = exhibitionEntity.title,
                modifier = Modifier.align(Center)
            )
        }
    }
}