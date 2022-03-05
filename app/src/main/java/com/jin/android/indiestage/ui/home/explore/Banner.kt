package com.jin.android.indiestage.ui.home.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.jin.android.indiestage.ui.theme.IndieStageTheme

@ExperimentalPagerApi
@Composable
fun HomeBanner(
    bannerList: List<Color> = listOf(Color.Gray, Color.LightGray)
) {
    val pagerState = rememberPagerState()

    Box(Modifier.padding(8.dp)) {
        HorizontalPager(
            count = bannerList.size,
            state = pagerState,
        ) { page ->
            BannerItem(
                bannerList[page], Modifier
                    .align(Alignment.BottomStart)
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.BottomCenter)
        )
    }

}

@Composable
fun BannerItem(color: Color = Color.LightGray, modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(color)
                .fillMaxSize()
        ) { //TODO Set Banner image
        }
    }
    Column(
        modifier = modifier.padding(20.dp)
    ) {
        Text("Indie Stage")
        Text("내 손안의 전시회!\n인디 작가들을 응원해주세요")
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
@Preview
fun BannerItemPreview() {
    IndieStageTheme() {
        BannerItem(modifier = Modifier)
    }
}