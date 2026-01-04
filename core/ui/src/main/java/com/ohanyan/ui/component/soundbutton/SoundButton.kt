package com.ohanyan.ui.component.soundbutton

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.ui.R

@Composable
fun SoundButton(
    modifier: Modifier = Modifier,
    isOn: Boolean = false,
    onClick: (Boolean) -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")


    @Composable
    fun returnPadding(): Float {
        val padding by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse,
            ), label = ""
        )
        return padding
    }

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scaleByClick by animateFloatAsState(
        targetValue = if (pressed) 0.7f else 1f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = ""
    )

    val padding = returnPadding()

    Box(
        modifier = modifier
            .scale(scaleByClick)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onClick(!isOn)
            }
            .padding(
                PaddingValues(top = (8 * padding).dp)
            ),
    ) {
        Image(
            modifier = Modifier
                .padding(32.dp)
                .size(42.dp),
            painter = painterResource(if (isOn) R.drawable.ic_sound_on else R.drawable.ic_sound_off),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
    }
}


@Preview
@Composable
fun SoundButtonPreview() {
    Row {
        SoundButton(isOn = false)
        SoundButton(isOn = true)
    }
}