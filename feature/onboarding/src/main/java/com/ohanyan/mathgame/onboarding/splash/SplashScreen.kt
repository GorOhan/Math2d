package com.ohanyan.mathgame.onboarding.splash

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.onboarding.R
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.mathaction.MathLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onAnimationEnd: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SplashScreenUI(
        uiState = uiState,
        onAnimationEnd = onAnimationEnd,
    )
}

@Composable
private fun SplashScreenUI(
    uiState: SplashUIState,
    onAnimationEnd: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val isHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var alpha by remember { mutableFloatStateOf(0f) }
    val secondLineAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 2000),
        label = ""
    )

    var offsetMainHeroY by remember { mutableStateOf(0.dp) }
    val animatedOffsetY by animateDpAsState(
        targetValue = offsetMainHeroY,
        animationSpec = tween(durationMillis = 2000),
        finishedListener = {
            coroutineScope.launch {
                alpha += 1f
                delay(1500L)
                onAnimationEnd()
            }
        },
        label = ""
    )

    LaunchedEffect(Unit) {
        offsetMainHeroY -= if (isHorizontal) 90.dp else 284.dp
        delay(1400)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.coreBlue.copy(0.4f),
                        MathAppTheme.colors.darkPurpleGray90.copy(0.1f)
                    )
                )
            ),
    ) {

        MathLoading(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = if (isHorizontal) 16.dp else 48.dp),
            iconSize = 46.dp
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = animatedOffsetY),
            horizontalArrangement = Arrangement.Center,
        ) {
            if (isHorizontal) {
                MainHero(modifier = Modifier.size(204.dp))
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isHorizontal) {
                    MainHero(modifier = Modifier.size(204.dp))
                }
                Text(
                    modifier = Modifier.padding(),
                    text = stringResource(R.string.feature_onboarding_hello_math),
                    fontSize = 32.sp,
                    color = MathAppTheme.colors.coreBlue,
                    style = MathAppTheme.typography.h1Bee
                )

                Text(
                    modifier = Modifier
                        .alpha(secondLineAlpha)
                        .padding(top = 24.dp),
                    text = stringResource(R.string.feature_onboarding_lets_learn),
                    fontSize = 32.sp,
                    color = MathAppTheme.colors.coreGreen,
                    style = MathAppTheme.typography.h1Bee
                )
            }

        }
    }
}

@Composable
@MathPreview
fun SelectAgeScreenPreview() {
    SplashScreen()
}