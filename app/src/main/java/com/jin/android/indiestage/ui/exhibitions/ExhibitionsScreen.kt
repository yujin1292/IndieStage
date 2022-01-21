package com.jin.android.indiestage.ui.exhibitions

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.*
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
import com.jin.android.indiestage.util.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun ExhibitionsScreen(
    mode: String,
    viewModel: ExhibitionsViewModel = viewModel(factory = ViewModelFactory(exhibitionRepository = ExhibitionRepository()))
) {

    val state = viewModel.state.collectAsState()

    val tabs = listOf(
        TabItem(Icons.Filled.Article, "Open", viewModel) { ExhibitionsContents(viewModel , state.value.openedExhibitionFlow) },
        TabItem(Icons.Filled.Article, "Ready", viewModel) { ExhibitionsContents(viewModel, state.value.readyExhibitionFlow) }
    )

    val pagerState = if (mode == "ready") rememberPagerState(1)
    else rememberPagerState()

    Column {
        Tabs(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

data class TabItem(
    var icon: ImageVector,
    var title: String,
    var viewModel: ExhibitionsViewModel,
    var screen: @Composable (ExhibitionsViewModel?) -> Unit
)

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = { Icon(tab.icon, contentDescription = "") },
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}


@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen(tabs[page].viewModel)
    }
}