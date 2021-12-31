package com.jin.android.indiestage.ui.artwork

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.ArtWork
import com.jin.android.indiestage.data.ArtWorkOnError
import com.jin.android.indiestage.data.ArtWorkOnSuccess
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.util.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun ArtWorkScreenAsGuest(
    onBackPress: () -> Unit,
    exhibitionId: String,
    artWorkId: String,
    viewModel: ArtWorkViewModel = viewModel(
        factory = ViewModelFactory(
            exhibitionRepo = ExhibitionRepo(),
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
                ArtWorkContentsAsGuest(
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
fun ArtWorkContentsAsGuest(
    onBackPress: () -> Unit,
    artWork: ArtWork
) {
    Column {
        ArtWorkAppbarAsGuest(title = artWork.title, onBackPress = onBackPress)
        ArtWorkPagerAsGuest(artWork)
    }
}

@Composable
fun ArtWorkAppbarAsGuest(
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
fun ArtWorkPagerAsGuest(artWork: ArtWork) {
    val pagerState = rememberPagerState()
    Row(Modifier.fillMaxSize()) {
        Column(
            Modifier.align(Alignment.CenterVertically)
        ) {
            HorizontalPager(
                count = artWork.pageNum,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                GuestPage()
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
fun GuestPage() {
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

