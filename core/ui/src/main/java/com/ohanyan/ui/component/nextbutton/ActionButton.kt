package com.ohanyan.ui.component.nextbutton

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
fun ActionButton(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    actionType: ActionType = ActionType.NEXT,
    onClick: () -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val padding by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse,
        ), label = ""
    )

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scaleByClick by animateFloatAsState(
        targetValue = if (pressed) 0.7f else 1f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = ""
    )

    AnimatedVisibility(
        modifier = modifier
            .scale(scaleByClick)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onClick()
            }
            .padding(
                when (actionType) {
                    ActionType.NEXT -> PaddingValues(bottom = (8 * padding).dp)
                    ActionType.PREVIOUS -> PaddingValues(top = (8 * padding).dp)
                    ActionType.SETTINGS -> PaddingValues(top = (8 * padding).dp)

                }
            ),
        visible = visible,
    ) {
        Image(
            modifier = Modifier
                .padding(32.dp),
            painter = painterResource(actionType.iconId),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
    }
}

enum class ActionType(@DrawableRes val iconId: Int) {
    NEXT(R.drawable.ic_next),
    PREVIOUS(R.drawable.ic_previous),
    SETTINGS(R.drawable.ic_settings)
}

@Preview
@Composable
fun ActionButtonPreview() {
    Row {
        ActionButton(actionType = ActionType.NEXT)
        ActionButton(actionType = ActionType.PREVIOUS)
        ActionButton(actionType = ActionType.SETTINGS)
    }
}