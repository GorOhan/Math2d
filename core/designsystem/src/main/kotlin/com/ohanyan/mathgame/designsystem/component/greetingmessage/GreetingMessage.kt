package com.ohanyan.mathgame.designsystem.component.greetingmessage

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.AppFont
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme


@Composable
fun GreetingMessage(
    modifier: Modifier,
    withAnimation: Boolean = false
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by if (withAnimation) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 42f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Text(
        modifier = modifier.padding(bottom = scale.dp),
        text = "Let's learn together",
        fontSize = 32.sp,
        color = MathAppTheme.colors.coreYellow,
        fontFamily = AppFont
    )
}