package com.ohanyan.ui.component.success

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.ui.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuccessBalloons(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val balloonsCount = if (isHorizontal) 30 else 15

    LaunchedEffect(Unit) {
        // different delay for each balloon
        delay(3000L)

        onAnimationEnd()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        FlowRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = balloonsCount
        ) {
            repeat(balloonsCount) { index ->

                val infiniteTransition = rememberInfiniteTransition(label = "")

                val offsetY by infiniteTransition.animateFloat(
                    initialValue = 4000f,
                    targetValue = -1200f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(Random.nextInt(2000, 3000)),
                        repeatMode = RepeatMode.Restart,
                        initialStartOffset = StartOffset(0)
                    ), label = ""
                )

                Image(
                    painter = painterResource(R.drawable.ic_balloons),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(top = 2.dp, end = 2.dp)
                        .graphicsLayer {
                            translationY = offsetY
                        }
                )
            }
        }
    }
}