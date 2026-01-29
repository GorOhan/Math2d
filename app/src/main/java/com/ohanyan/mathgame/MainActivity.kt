package com.ohanyan.mathgame

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.ohanyan.common.musicmanager.MusicManager
import com.ohanyan.mathgame.common.data.UserPreferencesRepository
import com.ohanyan.mathgame.navigation.MathNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val context = LocalContext.current

            DisposableEffect(lifecycleOwner) {
                MusicManager.register(lifecycleOwner, context)
                onDispose { MusicManager.unregister(lifecycleOwner) }   // optional but tidy
            }

            val musicOn by userPreferencesRepository.musicOn.collectAsState(initial = true)
            LaunchedEffect(musicOn) {
                MusicManager.setMusicEnabled(musicOn)
            }

            MaterialTheme {
                enableEdgeToEdge()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MathNavHost()
                }
            }
        }
    }
}