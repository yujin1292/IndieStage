package com.jin.android.indiestage.ui.stage

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.ui.theme.IndieStageTheme
import com.jin.android.indiestage.util.StageViewModelFactory

@Composable
fun Stage(
    onBackPress: () -> Unit,
    navigateToArtWork: (exhibitionId: String, artWorkId: String, mode: String) -> Unit,
    exhibitionId: String,
    viewModel: StageViewModel = viewModel(
        factory = StageViewModelFactory(
            exhibitionRepo = ExhibitionRepo(),
            exhibitionId = exhibitionId
        )
    )
) {
    when (val exhibitions = viewModel.exhibitionStateFlow.collectAsState().value) {
        is OnError -> {
            Log.e("stage", exhibitions.exception.toString())
            Text("Error")
        }
        is OnSuccess -> {
            exhibitions.querySnapshot?.toObjects(Exhibition::class.java)?.let {
                Box(Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberImagePainter(data = it[0].image),
                        contentDescription = "poster",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = colorResource(id = R.color.shadow50)
                    ) {}
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)) {
                        StageAppBar(title = it[0].title, onBackPress = onBackPress)
                        StageContent(
                            navigateToArtWork = navigateToArtWork,
                            viewModel = viewModel,
                            exhibitionId = exhibitionId
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun StageContent(
    navigateToArtWork: (
        exhibitionId: String,
        artWorkId: String,
        mode: String
    ) -> Unit,
    exhibitionId: String,
    viewModel: StageViewModel
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        StageArtistIntro(viewModel)
        WorkList(navigateToArtWork, exhibitionId, viewModel)
    }
}

@Composable
fun StageAppBar(
    title:String,
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

@Composable
fun StageArtistIntro(
    viewModel: StageViewModel
) {
    when (val artistResponse = viewModel.getArtistInfo().collectAsState(null).value) {
        is ArtistOnError -> {
            Log.e("stage", artistResponse.exception.toString())
            Text("Error")
        }
        is ArtistOnSuccess -> {
            artistResponse.querySnapshot?.toObjects(Artist::class.java)?.let {
                Log.d("stage", it.toString())
                if(it.size>0) ArtistInfoScreen(it[0])
            }
        }
    }
}

@Composable
fun ArtistInfoScreen(artist: Artist) {
    Card(
        contentColor = Color.DarkGray,
        backgroundColor = colorResource(R.color.text_background),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Image(
                painter = rememberImagePainter(data = artist.image),
                contentDescription = "artist pic",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .clip(shape = RoundedCornerShape(10.dp))
            )

            Text(
                text = artist.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = artist.intro.replace("\\n", "\n\n"),
                fontSize = 12.sp
            )


            Text(
                text = stringResource(id = R.string.contact),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                fontSize = 12.sp,
                text = artist.email
            )
            Text(
                fontSize = 12.sp,
                text = artist.homepage
            )

            Text(
                text = stringResource(id = R.string.history),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = artist.history.replace("\\n", "\n\n"),
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun WorkList(
    navigateToArtWork: (
        exhibitionId: String,
        artWorkId: String,
        mode: String
    ) -> Unit,
    exhibitionId: String,
    viewModel: StageViewModel
) {
    when (val artWorkResponse = viewModel.getArtWorkInfo().collectAsState(null).value) {
        is ArtWorksOnError -> {
            Log.e("stage", artWorkResponse.exception.toString())
            Text("Error")
        }
        is ArtWorksOnSuccess -> {
            artWorkResponse.querySnapshot?.toObjects(ArtWork::class.java)?.let {
                Column() {
                    LazyRow(modifier = Modifier.padding(vertical = 4.dp)) {
                        items(items = it) { work ->
                            WorkElement(navigateToArtWork = navigateToArtWork, item = work, exhibitionId = exhibitionId)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun WorkElement(
    navigateToArtWork: (
        exhibitionId: String,
        artWorkId: String,
        mode: String
    ) -> Unit,
    exhibitionId: String,
    item: ArtWork
) {
    Card(
        modifier = Modifier
            .clickable { navigateToArtWork(exhibitionId, item.id, "auth") } // TODO mode 변경
            .height(300.dp)
            .aspectRatio(0.7f)
            .padding(end = 10.dp)
    ) {
        Box {
            Image(
                painter = rememberImagePainter(data = item.image[0]),
                contentDescription = "image",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Center)
                    .background(colorResource(id = R.color.text_background))
            )
        }

    }
}

@Preview
@Composable
fun PreviewArtist() {
    IndieStageTheme {
        ArtistInfoScreen(Artist())
    }
}
