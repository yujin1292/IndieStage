package com.jin.android.indiestage.ui.home.checkedin

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import com.jin.android.indiestage.data.checkedin.CheckedInEntity
import com.jin.android.indiestage.ui.home.HomeViewModel
import com.jin.android.indiestage.util.lerp
import kotlin.math.absoluteValue

@ExperimentalPagerApi
@Composable
fun CheckedInScreen(viewModel: HomeViewModel) {
    val list = viewModel.checkedInList.observeAsState(arrayListOf()).value
    val listState = rememberLazyListState()

    val tickets = listOf("one", "two") // TODO 임시데이터 -> 저장된 데이터로 변경

    LazyColumn(state = listState) {
        item {
            Column() {
                Text(
                    text = "입장 가능한 전시", style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(12.dp)
                )
                CheckedInPager(list)
                Text(
                    text = "사용한 티켓", style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        items(
            items = tickets,
            itemContent = { item -> Ticket(item) }
        )
        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}

@ExperimentalPagerApi
@Composable
fun CheckedInPager(checkedInList: List<CheckedInEntity>) {
    val pagerState = rememberPagerState()
    Column() {
        HorizontalPager(
            count = checkedInList.size,
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
            { CheckedInPage(checkedInList[page]) }
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
fun CheckedInPage(item: CheckedInEntity) {
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
fun Ticket(item: String) {
    Card(modifier = Modifier.padding(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5f)
                .background(androidx.compose.ui.graphics.Color.Yellow)
        ) {
            Text(
                text = item,
                modifier = Modifier.align(Center)
            )
        }
    }
}