package com.jin.android.indiestage.ui.artwork

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.firestore.ArtWork
import com.jin.android.indiestage.data.firestore.ArtWorkOnError
import com.jin.android.indiestage.data.firestore.ArtWorkOnSuccess
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
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
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        itemsIndexed(artWork.contents) { _, _ ->
            GuestPage()
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

