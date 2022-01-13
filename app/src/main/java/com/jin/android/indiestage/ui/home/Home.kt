package com.jin.android.indiestage.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jin.android.indiestage.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jin.android.indiestage.data.firestore.Exhibition
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
import com.jin.android.indiestage.data.firestore.OnError
import com.jin.android.indiestage.data.firestore.OnSuccess
import com.jin.android.indiestage.data.room.BookMarkDataSource
import com.jin.android.indiestage.data.room.CheckedInDataSource
import com.jin.android.indiestage.ui.home.my.MyScreen
import com.jin.android.indiestage.ui.home.explore.ExploreScreen
import com.jin.android.indiestage.ui.home.explore.TabItem
import com.jin.android.indiestage.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun Home(
    navigateToTicketBox: (String) -> Unit,
    navigateToQuickEnter: ()->Unit,
    checkedInDataSource: CheckedInDataSource,
    bookMarkDataSource: BookMarkDataSource,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(
            checkedInDataSource,
            bookMarkDataSource,
            ExhibitionRepository()
        )
    )
) {
    val homeViewState by viewModel.state.collectAsState()

    val tabItemList = mutableListOf<TabItem>()
    when (val opened = homeViewState.openedExhibitionFlow) {
        is OnSuccess -> {
            opened.querySnapshot?.toObjects(Exhibition::class.java)?.let {
                tabItemList.add(TabItem(
                    title = "Opened Exhibitions",
                    itemList = it,
                    onItemClicked = navigateToTicketBox,
                    getMoreInfoClicked = {}

                ))
            }
        }
        is OnError -> {
            opened.exception?.printStackTrace()
        }
    }
    when (val closed = homeViewState.closedExhibitionFlow) {
        is OnSuccess -> {
            closed.querySnapshot?.toObjects(Exhibition::class.java)?.let {
                tabItemList.add(TabItem(
                    title = "Closed Exhibitions",
                    itemList = it,
                    onItemClicked = navigateToTicketBox,
                    getMoreInfoClicked = {}

                ))
            }
        }
        is OnError -> {
            closed.exception?.printStackTrace()
        }
    }

    Surface(Modifier.fillMaxSize()) {
        HomeContent(
            viewModel = viewModel,
            tabItemList = tabItemList,
            selectedHomeCategory = homeViewState.selectedHomeCategory,
            onCategorySelected = viewModel::onHomeCategorySelected,
            navigateToQuickEnter = navigateToQuickEnter
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    tabItemList: List<TabItem>,
    selectedHomeCategory: HomeCategory,
    onCategorySelected: (HomeCategory) -> Unit,
    navigateToQuickEnter: ()->Unit,
) {
    val fabShape = RoundedCornerShape(50)
    Scaffold(
        topBar = {
            HomeTopAppBar(
                backgroundColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedHomeCategory) {
                    HomeCategory.Exhibition -> {
                        ExploreScreen(tabItemList = tabItemList)
                    }
                    HomeCategory.My -> {
                        MyScreen(viewModel)
                    }
                }
            }
        },
        floatingActionButton = { HomeFabButton(fabShape = fabShape, onClick = navigateToQuickEnter) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            HomeBottomAppBar(
                selectedHomeCategory = selectedHomeCategory,
                onCategorySelected = onCategorySelected,
                fabShape = fabShape
            )
        }
    )
}

@Composable
fun HomeTopAppBar(
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row {
                Icon(
                    painter = painterResource(R.drawable.ic_loco_text_temp),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .heightIn(max = 24.dp)
                )
            }
        },
        backgroundColor = backgroundColor,
        actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = { /* TODO: Open Setting */ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.setting)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun HomeBottomAppBar(
    selectedHomeCategory: HomeCategory,
    onCategorySelected: (HomeCategory) -> Unit,
    fabShape: Shape
) {

    BottomAppBar(
        cutoutShape = fabShape,
        content = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Article, "")
                    },
                    label = { Text(text = stringResource(id = R.string.exhibition_tab)) },
                    selected = selectedHomeCategory == HomeCategory.Exhibition,
                    onClick = { onCategorySelected(HomeCategory.Exhibition) },
                    alwaysShowLabel = false
                )

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.FactCheck, "")
                    },

                    label = { Text(text = stringResource(id = R.string.my_tab)) },
                    selected = selectedHomeCategory == HomeCategory.My,
                    onClick = { onCategorySelected(HomeCategory.My) },
                    alwaysShowLabel = false
                )
            }
        }
    )
}

@Composable
fun HomeFabButton(
    fabShape: Shape,
    onClick: ()->Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = fabShape,
    ) {
        Icon(Icons.Filled.Camera, "")
    }
}