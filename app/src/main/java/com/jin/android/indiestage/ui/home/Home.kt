package com.jin.android.indiestage.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jin.android.indiestage.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jin.android.indiestage.data.firestore.*
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
    navigateToQuickEnter: () -> Unit,
    navigateToExhibitions: (String) -> Unit,
    checkedInDataSource: CheckedInDataSource,
    bookMarkDataSource: BookMarkDataSource,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(
            checkedInDataSource,
            bookMarkDataSource,
            FireStoreRepository()
        )
    )
) {
    val homeViewState by viewModel.state.collectAsState()

    val tabItemList = mutableListOf<TabItem>()
    var bannerList:List<Banner> = listOf()

    when (val opened = homeViewState.openedExhibitionFlow) {
        is OnSuccess -> {
            val list = mutableListOf<Exhibition>()

            opened.querySnapshot?.let { snapShot ->
                for (query in snapShot) {
                    val item = query.toObject<Exhibition>()
                    item.isOpened = query["isOpened"] == true
                    list.add(item)
                }
            }
            tabItemList.add(TabItem(
                title = stringResource(id = R.string.open_exhibitions),
                itemList = list,
                onItemClicked = navigateToTicketBox,
                getMoreInfoClicked = { navigateToExhibitions("opened") }
            ))

        }
        is OnError -> {
            opened.exception?.printStackTrace()
        }
    }
    when (val closed = homeViewState.closedExhibitionFlow) {
        is OnSuccess -> {
            val list = mutableListOf<Exhibition>()

            closed.querySnapshot?.let { snapShot ->
                for (query in snapShot) {
                    val item = query.toObject<Exhibition>()
                    item.isOpened = query["isOpened"] == true
                    list.add(item)
                }
            }

            tabItemList.add(TabItem(
                title = stringResource(id = R.string.ready_exhibitions),
                itemList = list,
                onItemClicked = navigateToTicketBox,
                getMoreInfoClicked = { navigateToExhibitions("ready") }
            ))
        }
        is OnError -> {
            closed.exception?.printStackTrace()
        }
    }
    when (val banners = homeViewState.bannerFlow){
        is BannerOnSuccess ->{
            banners.data?.let{
                bannerList = it.toObjects()
            }
        }
        is BannerOnError ->{
            banners.exception?.printStackTrace()
        }
    }

    Surface(Modifier.fillMaxSize()) {
        HomeContent(
            viewModel = viewModel,
            tabItemList = tabItemList,
            bannerList = bannerList,
            selectedHomeCategory = homeViewState.selectedHomeCategory,
            onCategorySelected = viewModel::onHomeCategorySelected,
            navigateToQuickEnter = navigateToQuickEnter,
            navigateToTicketBox = navigateToTicketBox
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    tabItemList: List<TabItem>,
    bannerList :List<Banner>,
    selectedHomeCategory: HomeCategory,
    onCategorySelected: (HomeCategory) -> Unit,
    navigateToTicketBox: (String) -> Unit,
    navigateToQuickEnter: () -> Unit,
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
                        ExploreScreen(tabItemList, bannerList)
                    }
                    HomeCategory.My -> {
                        MyScreen(viewModel, navigateToTicketBox)
                    }
                }
            }
        },
        floatingActionButton = {
            HomeFabButton(
                fabShape = fabShape,
                onClick = navigateToQuickEnter
            )
        },
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
        /* actions = {
             CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                 IconButton(
                     onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.setting)
                    )
                }
            }
        },*/
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
            BottomNavigation(
                backgroundColor = colorResource(id = R.color.purple_700),
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Article, "")
                    },
                    label = { Text(text = stringResource(id = R.string.exhibition_tab)) },
                    selected = selectedHomeCategory == HomeCategory.Exhibition,
                    onClick = { onCategorySelected(HomeCategory.Exhibition) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(0.4f),
                    alwaysShowLabel = false
                )

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.FactCheck, "")
                    },

                    label = { Text(text = stringResource(id = R.string.my_tab)) },
                    selected = selectedHomeCategory == HomeCategory.My,
                    onClick = { onCategorySelected(HomeCategory.My) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(0.4f),
                    alwaysShowLabel = false
                )
            }
        }
    )
}

@Composable
fun HomeFabButton(
    fabShape: Shape,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = fabShape,
        backgroundColor = colorResource(id = R.color.purple_200)
    ) {
        Icon(Icons.Filled.Camera, "")
    }
}