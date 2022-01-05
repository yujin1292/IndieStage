package com.jin.android.indiestage.ui.stage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.ui.components.IndieStageDivider
import com.jin.android.indiestage.util.ViewModelFactory
import com.jin.android.indiestage.util.lerp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.max
import kotlin.math.min

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@ExperimentalCoroutinesApi
@Composable
fun Stage(
    onBackPress: () -> Unit,
    navigateToArtWork: (exhibitionId: String, artWorkId: String) -> Unit,
    exhibitionId: String,
    viewModel: StageViewModel = viewModel(
        factory = ViewModelFactory(
            exhibitionRepo = ExhibitionRepo(),
            exhibitionId = exhibitionId
        )
    )
) {

    val viewState by viewModel.state.collectAsState()

    StageScreen(
        navigateToArtWork = navigateToArtWork,
        exhibitionId = exhibitionId,
        exhibitionResponse = viewState.exhibitionFlow,
        artistResponse = viewState.artistInfoFlow,
        artWorksResponse = viewState.artWorkInfoFlow,
        onBackPress = onBackPress
    )
}

@Composable
fun StageScreen(
    navigateToArtWork: (
        exhibitionId: String,
        artWorkId: String
    ) -> Unit,
    exhibitionResponse: ExhibitionResponse,
    artistResponse: ArtistResponse,
    artWorksResponse: ArtWorksResponse,
    exhibitionId: String,
    onBackPress: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        if (artistResponse is ArtistOnSuccess && artWorksResponse is ArtWorksOnSuccess && exhibitionResponse is OnSuccess) {
            val artist =
                artistResponse.querySnapshot?.toObjects(Artist::class.java)?.get(0) ?: Artist()
            val artWorks =
                artWorksResponse.querySnapshot?.toObjects(ArtWork::class.java) ?: listOf(ArtWork())
            val exhibition =
                exhibitionResponse.querySnapshot?.toObjects(Exhibition::class.java)?.get(0)
                    ?: Exhibition()

            Header(exhibition.image)
            Body(
                artWorks = artWorks, artist = artist, scroll = scroll,
                exhibitionId = exhibitionId,
                navigateToArtWork = navigateToArtWork,
                exhibition = exhibition
            )
            Title(exhibition, scroll.value)
            ProfileImage(artist.image, scroll.value)
            Up(onBackPress)
        } else {
            if (artistResponse is ArtistOnError) artistResponse.exception?.printStackTrace()
            if (artWorksResponse is ArtWorksOnError) artWorksResponse.exception?.printStackTrace()
            if (exhibitionResponse is OnError) exhibitionResponse.exception?.printStackTrace()
            Text("Error!")
        }

    }
}

@Composable
private fun Header(imageUrl: String) {
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(true)
                placeholder(drawableResId = R.drawable.ic_launcher_foreground)
            }
        ),
        contentDescription = "header",
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(Color.Transparent)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.back)
        )
    }
}

@Composable
private fun Body(
    navigateToArtWork: (
        exhibitionId: String,
        artWorkId: String
    ) -> Unit,
    exhibitionId: String,
    artist: Artist,
    artWorks: List<ArtWork>,
    exhibition: Exhibition,
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            Surface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    InfoScreen(artist = artist, exhibition = exhibition)

                    Spacer(Modifier.height(40.dp))
                    // TODO 장르 / 해쉬태그  Chip 형태 표기

                    Spacer(Modifier.height(16.dp))
                    // 작품 리스트
                    artWorks.forEach { artWork ->
                        ArtWorkElement(
                            navigateToArtWork = navigateToArtWork,
                            exhibitionId = exhibitionId,
                            item = artWork
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding(start = false, end = false)
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoScreen(exhibition: Exhibition, artist: Artist) {

    Spacer(Modifier.height(16.dp))
    Text(
        text = exhibition.description.replace("\\n", "\n"),
        modifier = HzPadding
    )
    Spacer(Modifier.height(16.dp))
    IndieStageDivider()
    Spacer(Modifier.height(16.dp))
    Text(
        text = artist.name,
        style = MaterialTheme.typography.h6,
        modifier = HzPadding
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = artist.intro.replace("\\n", "\n"),
        modifier = HzPadding
    )

    Spacer(Modifier.height(16.dp))


    Text(
        text = stringResource(id = R.string.contact),
        textDecoration = TextDecoration.Underline,
        modifier = HzPadding
    )
    Text(
        text = artist.email,
        modifier = HzPadding
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = artist.homepage,
        modifier = HzPadding
    )
    Spacer(Modifier.height(16.dp))

    var seeMore by remember { mutableStateOf(true) }
    Text(
        text = stringResource(id = R.string.history),
        textDecoration = TextDecoration.Underline,
        modifier = HzPadding
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = artist.history.replace("\\n", "\n"),
        style = MaterialTheme.typography.body1,
        maxLines = if (seeMore) 4 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
        modifier = HzPadding
    )
    val textButton = if (seeMore) "see More" else "see Less"

    Text(
        text = textButton,
        style = MaterialTheme.typography.button,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .heightIn(20.dp)
            .fillMaxWidth()
            .padding(top = 15.dp)
            .clickable {
                seeMore = !seeMore
            }
    )

}

@Composable
private fun Title(exhibition: Exhibition, scroll: Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .fillMaxWidth()
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
            .background(color = MaterialTheme.colors.background)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = exhibition.title,
            style = MaterialTheme.typography.h4,
            modifier = HzPadding
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "sub title",
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            modifier = HzPadding
        )
    }
}

@Composable
private fun ProfileImage(
    imageUrl: String,
    scroll: Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
        collapseFraction = collapseFraction,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        CircleImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CircleImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    Surface(
        color = Color.LightGray,
        elevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                    placeholder(drawableResId = R.drawable.ic_launcher_foreground)
                }
            ),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun ArtWorkElement(
    navigateToArtWork: (
        exhibitionId: String,
        artWorkId: String
    ) -> Unit,
    exhibitionId: String,
    item: ArtWork
) {
    Card(
        modifier = Modifier
            .clickable { navigateToArtWork(exhibitionId, item.id) }
            .fillMaxWidth()
            .aspectRatio(4f)
            .padding(end = 10.dp)
    ) {
        Box {
            Image(
                painter = rememberImagePainter(data = item.contents[0].image),
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
                    .align(Alignment.Center)
                    .background(colorResource(id = R.color.text_background))
            )
        }

    }
}

@Composable
fun StageAppBar(
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
