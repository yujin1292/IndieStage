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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.ktx.toObject
import com.jin.android.indiestage.IndieStageApp
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.ArtWork
import com.jin.android.indiestage.data.ArtWorkOnError
import com.jin.android.indiestage.data.ArtWorkOnSuccess
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.ui.theme.IndieStageTheme
import com.jin.android.indiestage.util.ArtWorkViewModelFactory

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
                    artWork = artWork
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

@Composable
fun ArtWorkPage(artWork: ArtWork) {
    val page by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
    ) {
        Image(
            painter = rememberImagePainter(data = artWork.image[page]),
            contentDescription = "page",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            artWork.text[page].split("\\n").forEach {
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
fun ArtWorkContents(
    onBackPress: () -> Unit,
    artWork: ArtWork
) {
    Column() {
        ArtWorkAppbar(title = artWork.title, onBackPress = onBackPress)
        ArtWorkPage(artWork)
    }
}

@Preview
@Composable
fun PreviewPage() {
    IndieStageTheme() {
        Column() {
            ArtWorkPage(ArtWork())
        }
    }
}
