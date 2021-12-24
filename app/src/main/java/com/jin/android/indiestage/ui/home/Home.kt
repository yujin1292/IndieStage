package com.jin.android.indiestage.ui.home

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
import com.jin.android.indiestage.util.DynamicThemePrimaryColorsFromImage
import com.jin.android.indiestage.util.contrastAgainst
import com.jin.android.indiestage.util.rememberDominantColorState
import com.jin.android.indiestage.util.verticalGradientScrim
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jin.android.indiestage.data.Exhibition


@ExperimentalPagerApi
@Composable
fun Home(
    navigateToStage: (String) -> Unit, //TODO navigateToTicketBox 으로 변경
    viewModel: HomeViewModel = viewModel()
) {
    Surface(Modifier.fillMaxSize()) {
        HomeContent(
            navigateToStage = navigateToStage,
            modifier = Modifier.fillMaxSize(),
            posters = viewModel.tempData
        )
    }
}

@ExperimentalPagerApi
@Composable
fun HomeContent(
    navigateToStage: (String) -> Unit,
    modifier: Modifier = Modifier,
    posters: List<Exhibition>,
) {
    Column {
        val surfaceColor = MaterialTheme.colors.surface
        val dominantColorState = rememberDominantColorState { color ->
            // We want a color which has sufficient contrast against the surface color
            color.contrastAgainst(surfaceColor) >= MinContrastOfPrimaryVsSurface
        }

        DynamicThemePrimaryColorsFromImage(dominantColorState) {
            val pagerState = rememberPagerState()
            val selectedImageUrl = posters.getOrNull(pagerState.currentPage)?.posterImageUrl

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

                if (posters.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))

                    HorizontalPager(
                        count = posters.size,
                        state = pagerState,
                        modifier = modifier
                    ) { page ->
                        PosterItem(
                            onClick = navigateToStage,
                            item = posters[page],
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
    item:Exhibition
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
                .aspectRatio(1f)
                .clickable(onClick = { expanded = !expanded })
        ) {
            if (item.posterImageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = item.posterImageUrl),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                )
            }

            if (expanded) {
                Surface(
                    color = colorResource(id = R.color.shadow50),
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Row( verticalAlignment = Alignment.CenterVertically){
                        Button(
                            onClick = {onClick(item.stageUri)},
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

