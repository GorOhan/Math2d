package com.ohanyan.mathgame.designsystem.component.header

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.AppFont
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun HeaderFromTop(title: String) {

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var isShaking by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    var startAnimation by remember { mutableStateOf(false) }
    val padding by animateDpAsState(
        targetValue = if (startAnimation) 48.dp else 0.dp,
        animationSpec = tween(durationMillis = 1000), label = "",
        finishedListener = {
            isShaking = true
            scope.launch {
                repeat(15) { // Shake for 15 iterations
                    offsetX.animateTo(
                        Random.nextInt(-35, 15).toFloat(),
                        animationSpec = tween(100)
                    )
                    offsetY.animateTo(
                        Random.nextInt(-35, 15).toFloat(),
                        animationSpec = tween(100)
                    )
                }
                offsetX.animateTo(0f, animationSpec = tween(200))
                offsetY.animateTo(0f, animationSpec = tween(200))
                isShaking = false
            }
        }
    )



    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding),
        horizontalArrangement = Arrangement.Center
    ) {
        title.forEachIndexed { index, letter ->
                Text(
                    modifier = Modifier.offset {
                        IntOffset(
                            offsetX.value.roundToInt(),
                            offsetY.value.roundToInt()
                        )
                    },
                    text = letter.toString(),
                    fontSize = 52.sp,
                    color = MathAppTheme.colors.coreYellow,
                    fontFamily = AppFont
                )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    MathAppTheme {
        HeaderFromTop("Android")
    }
}