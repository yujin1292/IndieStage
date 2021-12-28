package com.jin.android.indiestage.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jin.android.indiestage.R
import com.jin.android.indiestage.ui.theme.MinContrastOfPrimaryVsSurface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jin.android.indiestage.data.Exhibition
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.data.OnError
import com.jin.android.indiestage.data.OnSuccess
import com.jin.android.indiestage.util.*


@ExperimentalPagerApi
@Composable
fun Home(
    navigateToTicketBox: (String) -> Unit, //TODO navigateToTicketBox 으로 변경
    viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(ExhibitionRepo()))
) {
    Surface(Modifier.fillMaxSize()) {
        HomeContent(
            navigateToTicketBox = navigateToTicketBox,
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
        )
    }
}

@ExperimentalPagerApi
@Composable
fun HomeContent(
    navigateToTicketBox: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    Column {
        val surfaceColor = MaterialTheme.colors.surface
        val dominantColorState = rememberDominantColorState { color ->
            // We want a color which has sufficient contrast against the surface color
            color.contrastAgainst(surfaceColor) >= MinContrastOfPrimaryVsSurface
        }

        when (val exhibitions = viewModel.exhibitionStateFlow.collectAsState().value) {
            is OnError -> {
                Log.e("home", exhibitions.exception.toString())
                Text("Error")
            }
            is OnSuccess -> {
                exhibitions.querySnapshot?.toObjects(Exhibition::class.java)?.let {
                    DynamicThemePrimaryColorsFromImage(dominantColorState) {
                        val pagerState = rememberPagerState()
                        val selectedImageUrl = it.getOrNull(pagerState.currentPage)?.image

                        // When the selected image url changes, call updateColorsFromImageUrl() or reset()
                        LaunchedEffect(selectedImageUrl) {
                            if (selectedImageUrl != null) {
                                dominantColorState.updateColorsFromImageUrl(selectedImageUrl)
                            } else {
                                dominantColorState.reset()
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalGradientScrim(
                                    color = MaterialTheme.colors.primary.copy(alpha = 0.38f),
                                    startYPercentage = 1f,
                                    endYPercentage = 0f
                                )
                        ) {
                            //val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
                            val appBarColor = Color.Transparent

                            Spacer(
                                Modifier
                                    .background(appBarColor)
                                    .fillMaxWidth()
                            )

                            HomeAppBar(
                                backgroundColor = appBarColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                            if (it.isNotEmpty()) {
                                Spacer(Modifier.height(16.dp))
                                HorizontalPager(
                                    count = it.size,
                                    state = pagerState,
                                    modifier = modifier
                                ) { page ->
                                    PosterItem(
                                        onClick = navigateToTicketBox,
                                        item = it[page],
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .fillMaxHeight()
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeAppBar(
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
private fun PosterItem(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    item: Exhibition
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .aspectRatio(0.7f)
                .clickable(onClick = { expanded = !expanded })
        ) {
            Image(
                painter = rememberImagePainter(data = item.image),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
            )

            if (expanded) {
                Surface(
                    color = colorResource(id = R.color.shadow50),
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { onClick(item.id) },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        ) {
                            Text("전시 입장")
                        }
                    }

                }

            }
        }
    }
}

