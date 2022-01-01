package com.jin.android.indiestage.ui.ticketbox

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.*
import com.jin.android.indiestage.R
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.util.QrCodeAnalyzer
import com.jin.android.indiestage.util.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun TicketBox(
    onBackPress: () -> Unit,
    navigateToStage: (String, String) -> Unit,
    exhibitionId: String,
    viewModel: TicketBoxViewModel = viewModel(
        factory = ViewModelFactory(
            exhibitionRepo = ExhibitionRepo(),
            exhibitionId = exhibitionId
        )
    )
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                Log.d("TicketBox", "PERMISSION GRANTED")
                hasCamPermission = granted

            } else {
                Log.d("TicketBox", "PERMISSION DENIED")
            }
        }
    )

    LaunchedEffect(key1 = true) { launcher.launch(Manifest.permission.CAMERA) }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TicketBoxAppBar(title = "TicketBox", onBackPress = onBackPress, navigateToStage, exhibitionId)

        if (hasCamPermission) {
            QRCodeScanner(
                context = context,
                lifecycleOwner = lifecycleOwner,
                navigateToStage = navigateToStage,
                exhibitionId = exhibitionId,
                viewModel = viewModel
            )
        } else {
            IconButton(
                onClick = { launcher.launch(Manifest.permission.CAMERA) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "have to get permission")
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(id = R.string.retry_label)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun TicketBoxAppBar(
    title: String,
    onBackPress: () -> Unit,
    navigateToStage: (String, String) -> Unit,
    exhibitionId: String
) {
    TopAppBar(
        title = {
            Row {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
                Text(modifier = Modifier.align(Alignment.CenterVertically), text = title)
            }
        },
        actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Button(
                    onClick = {navigateToStage(exhibitionId, "guest") }
                ) {
                    Text("둘러보기")
                }
            }
        }
    )
}

@ExperimentalCoroutinesApi
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun QRCodeScanner(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    viewModel: TicketBoxViewModel,
    navigateToStage: (String, String) -> Unit,
    exhibitionId: String,
) {
    var code by remember { mutableStateOf("") }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    Column {
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer { result ->
                        code = result
                    }
                )

                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        if (code != "") {
            viewModel.checkEnterCode(code)
        }
        if (viewModel.startNavigateToStageWithAuth.value) {
            navigateToStage(exhibitionId, "auth")
        }else{
            if (code != "") {
                Toast.makeText(
                    context,
                    stringResource(id = R.string.retry_label),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}