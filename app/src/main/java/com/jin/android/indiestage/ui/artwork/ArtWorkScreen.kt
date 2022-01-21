package com.jin.android.indiestage.ui.artwork

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.firestore.*
import com.jin.android.indiestage.ui.theme.IndieStageTheme
import com.jin.android.indiestage.util.ViewModelFactory
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun ArtWorkScreen(
    onBackPress: () -> Unit,
    exhibitionId: String,
    artWorkId: String,
    viewModel: ArtWorkViewModel = viewModel(
        factory = ViewModelFactory(
            exhibitionRepository = ExhibitionRepository(),
            exhibitionId = exhibitionId,
            artWorkId = artWorkId
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
                    artWork = artWork
                )
            }
        }
        null -> {
            Text("Null")
        }
    }
}

@ExperimentalPagerApi
@Composable
fun ArtWorkContents(
    onBackPress: () -> Unit,
    artWork: ArtWork
) {
    Column {
        ArtWorkAppbar(title = artWork.title, onBackPress = onBackPress)
        ArtWorkPager(artWork)
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
        Text(modifier = Modifier.align(CenterVertically), text = title)
    }
}

@ExperimentalPagerApi
@Composable
fun ArtWorkPager(artWork: ArtWork) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        itemsIndexed(artWork.contents) { index, item ->
            AuthPage(
                index,
                item
            )
        }
    }
}


@Composable
fun AuthPage(
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


@Preview
@Composable
fun PreviewPage() {
    IndieStageTheme {
        Column {
            AuthPage(0, Content())
        }
    }
}
