package com.ohanyan.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.ui.R
import com.ohanyan.ui.component.mathaction.MathLoading

@Composable
fun PlayGame(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.6f else 1f,
        animationSpec = tween(
            durationMillis = 160,
            easing = FastOutSlowInEasing
        ),
        label = "pressScale",
        finishedListener = { onClick() }
    )

    Column(
        modifier = modifier
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .scale(scale)
                .padding(horizontal = 32.dp)
                .shadow(6.dp, shape = CircleShape)
                .clickable(
                    onClick = {},
                    interactionSource = interactionSource,
                    indication = null,
                ),
            painter = painterResource(R.drawable.ic_play),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        MathLoading()
    }
}


@Composable
fun NextNumber(
    modifier: Modifier = Modifier,
    nextNumber: String,
    @DrawableRes busDrawableRes: Int = R.drawable.ic_school_bus,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()


    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.6f else 1f,
        animationSpec = tween(
            durationMillis = 160,
            easing = FastOutSlowInEasing
        ),
        label = "pressScale",
        finishedListener = { onClick() }
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .scale(scale)
                .clickable(
                    onClick = {},
                    interactionSource = interactionSource,
                    indication = null,
                ),
            painter = painterResource(busDrawableRes),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = nextNumber,
            style = MathAppTheme.typography.chalk,
            color = MathAppTheme.colors.secondaryWhite,
            fontSize = 48.sp,
        )
    }
}

@Composable
fun NumberAndBus(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .width(124.dp),
        painter = painterResource(R.drawable.ic_school_bus),
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
    )
}

@Composable
fun Point(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")


    @Composable
    fun returnPadding(): Float {

        val padding by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 4f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse,
            ), label = ""
        )
        return padding
    }
    val padding = returnPadding()

    Image(
        modifier = modifier
            .width(48.dp)
            .rotate(90f)
            .offset(x = (2*padding).dp),
        painter = painterResource(R.drawable.ic_point),
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
    )
}