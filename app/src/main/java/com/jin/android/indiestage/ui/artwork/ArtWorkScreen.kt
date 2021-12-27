package com.jin.android.indiestage.ui.artwork

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.ui.theme.IndieStageTheme
import com.jin.android.indiestage.util.ArtWorkViewModelFactory

@ExperimentalPagerApi
@Composable
fun ArtWorkScreen(
    onBackPress: () -> Unit,
    exhibitionId: String,
    artWorkId: String,
    mode: String,
    viewModel: ArtWorkViewModel = viewModel(
        factory = ArtWorkViewModelFactory(
            exhibitionRepo = ExhibitionRepo(),
            exhibitionId = exhibitionId,
            artWorkId = artWorkId,
            mode = mode
        )
    )
) {
    when (val artWorkState = viewModel.artWorkStateFlow.collectAsState().value) {
        is ArtWorkOnError -> {
            Log.e("art work", artWorkState.exception.toString())
            Text("Error!")
        }
        is ArtWorkOnSuccess -> {
            artWorkState.querySnapshot?.toObject(ArtWork::class.java)?.let { artWork ->
                Log.d("art work", artWork.toString())
                ArtWorkContents(
                    onBackPress = onBackPress,
                    artWork = artWork,
                    mode = mode
                )
            }
        }
        null -> {
            Text("Null")
        }
    }
}

@Composable
fun ArtWorkAppbar(
    title: String,
    onBackPress: () -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        IconButton(onClick = onBackPress) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
        Text(modifier = Modifier.align(Alignment.CenterVertically), text = title)
    }
}

@ExperimentalPagerApi
@Composable
fun ArtWorkPager(artWork: ArtWork, mode: String) {
    val pagerState = rememberPagerState()
    Row(Modifier.fillMaxSize()) {
        Column(
            Modifier.align(CenterVertically)
        ) {
            HorizontalPager(
                count = artWork.pageNum,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
            ) { page ->
                ArtWorkPage(
                    page,
                    artWork.contents[page],
                    mode
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }

}


@Composable
fun ArtWorkPage(
    page: Int,
    content: Content,
    mode: String
) {
    if (mode == "auth") {
        authPage(page = page, content = content)
    } else {
        guestPage()
    }
}

@Composable
fun authPage(
    page: Int,
    content: Content
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
    ) {
        Image(
            painter = rememberImagePainter(data = content.image),
            contentDescription = "page",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            content.text.split("\\n").forEach {
                Text(
                    text = it,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(10.dp)
                        .background(colorResource(id = R.color.text_background))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = (page + 1).toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun guestPage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "lock",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@ExperimentalPagerApi
@Composable
fun ArtWorkContents(
    onBackPress: () -> Unit,
    artWork: ArtWork,
    mode: String
) {
    Column() {
        ArtWorkAppbar(title = artWork.title, onBackPress = onBackPress)
        ArtWorkPager(artWork, mode)
    }
}

@Preview
@Composable
fun PreviewPage() {
    IndieStageTheme() {
        Column() {
            ArtWorkPage(0, Content(), "auth")
        }
    }
}
