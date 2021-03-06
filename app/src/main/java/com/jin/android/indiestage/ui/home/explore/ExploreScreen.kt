package com.jin.android.indiestage.ui.home.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jin.android.indiestage.data.firestore.Banner

@ExperimentalPagerApi
@Composable
fun ExploreScreen(tabItemList: List<TabItem> , bannerList : List<Banner>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        HomeBanner(bannerList)
        TabLayouts(tabItems = tabItemList)
        Spacer(modifier = Modifier.height(64.dp))
    }
}