package com.jin.android.indiestage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.jin.android.indiestage.ui.theme.IndieStageTheme

class MainActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val indieStageApplication: IndieStage = application as IndieStage
        setContent {
            IndieStageTheme {
                Surface(color = MaterialTheme.colors.background) {
                    IndieStageApp(indieStageApplication)
                }
            }
        }
    }
}