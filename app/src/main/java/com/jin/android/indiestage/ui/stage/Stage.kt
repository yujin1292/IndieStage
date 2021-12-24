package com.jin.android.indiestage.ui.stage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jin.android.indiestage.R
import com.jin.android.indiestage.ui.theme.IndieStageTheme

@Composable
fun Stage(
    onBackPress: () -> Unit,
    navigateToArtWork: (stageUri: String, artWorkUri: String, mode: String, page: Int) -> Unit,
    stageUri: String,
    viewModel: StageViewModel = viewModel()
) {
    viewModel.exhibition.title = stageUri
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_loco_text_temp),
            contentDescription = "poster",
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(20,20,20,9))
        )
        StageContent(
            onBackPress = onBackPress,
            navigateToArtWork = navigateToArtWork,
            viewModel = viewModel
        )
    }
}

@Composable
fun StageContent(
    onBackPress: () -> Unit,
    navigateToArtWork: (
        stageUri: String,
        artWorkUri: String,
        mode: String,
        page: Int
    ) -> Unit,
    viewModel: StageViewModel
) {
    Column(modifier = Modifier.padding(20.dp)) {
        StageAppBar(viewModel = viewModel, onBackPress = onBackPress)
        StageArtistIntro(viewModel.artistInformation)
        WorkList(navigateToArtWork, viewModel)
    }
}

@Composable
fun StageAppBar(
    viewModel: StageViewModel,
    onBackPress: () -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        IconButton(onClick = onBackPress) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
        Text(modifier = Modifier.align(CenterVertically), text = viewModel.exhibition.title)
    }
}

@Composable
fun StageArtistIntro(
    artistInformation: ArtistInfo
) {
    Column() {
        Text("작가 소개")
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .aspectRatio(2f)
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "artist pic",
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(0.8f)
                    .clip(
                        shape = CutCornerShape(
                            topStart = 5.dp,
                            bottomEnd = 5.dp
                        )
                    )
                    .background(Color.DarkGray)
                    .padding(10.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 20.dp)
                    .fillMaxSize()
            ) {
                Text(artistInformation.artistName)
                Text(artistInformation.Describe)
            }
        }
    }
}

@Composable
fun WorkList(
    navigateToArtWork: (
        stageUri: String,
        artWorkUri: String,
        mode: String,
        page: Int
    ) -> Unit,
    viewModel: StageViewModel
) {
    Column() {
        Text("전시 항목")
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = viewModel.artWorkList) { work ->
                WorkElement(navigateToArtWork = navigateToArtWork, item = work)
            }
        }
    }

}

@Composable
fun WorkElement(
    navigateToArtWork: (
        stageUri: String,
        artWorkUri: String,
        mode: String,
        page: Int
    ) -> Unit,
    item: ArtWorkInfo
) {
    Card(
        modifier = Modifier
            .clickable { navigateToArtWork(")a", "b", "c", 0) }
            .fillMaxWidth()
            .aspectRatio(3f)
    ) {
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "image",
                modifier = Modifier.padding(10.dp)
            )
            Column() {
                Text(item.title)
                Text("Description")
            }
        }
    }
}

@Preview
@Composable
fun PreviewArtist() {
    IndieStageTheme {
        StageArtistIntro(
            artistInformation = ArtistInfo(
                " ",
                "name",
                "소개말"
            )
        )
    }
}

@Preview
@Composable
fun PreviewWorkElement() {
    IndieStageTheme {
        WorkElement(
            navigateToArtWork = { _, _, _, _ -> },
            ArtWorkInfo("artworkUri", "image", "title")
        )
    }
}