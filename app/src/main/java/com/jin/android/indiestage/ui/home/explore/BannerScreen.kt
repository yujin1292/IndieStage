package com.jin.android.indiestage.ui.home.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.jin.android.indiestage.data.firestore.Banner
import com.jin.android.indiestage.ui.theme.IndieStageTheme

@ExperimentalPagerApi
@Composable
fun HomeBanner(bannerList: List<Banner>) {
    val pagerState = rememberPagerState()

    Box(Modifier.padding(8.dp)) {
        HorizontalPager(
            count = bannerList.size,
            state = pagerState,
        ) { page ->
            BannerItem(
                bannerList[page], Modifier.align(Alignment.BottomStart)
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
fun BannerItem(banner: Banner, modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(data = banner.image),
                contentDescription = banner.text,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
    Column(
        modifier = modifier.padding(20.dp)
    ) {
        Text(banner.text)
        Spacer(modifier = Modifier.height(20.dp))
    }
}