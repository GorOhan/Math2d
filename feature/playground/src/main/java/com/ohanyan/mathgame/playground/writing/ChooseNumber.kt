package com.ohanyan.mathgame.playground.writing

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.ui.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseNumber(
    modifier: Modifier,
    maxAvailableNumber: Int,
    onNumberClick: (Int) -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 5
        ) {
            (0..9).forEach { number ->

                val interactionSource = remember { MutableInteractionSource() }
                val pressed by interactionSource.collectIsPressedAsState()

                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.7f else 1f,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
                    finishedListener = {
                        onNumberClick(number)
                    },
                    label = ""
                )

                val isLocked = number > maxAvailableNumber
                Box(
                    modifier = Modifier
                        .scale(scale)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            enabled = !isLocked) { }
                ) {
                    if (isLocked) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 2.dp, end = 2.dp),
                            painter = painterResource(R.drawable.ic_lock),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                        )
                    }
                    Box(
                        modifier
                            .padding(16.dp)
                            .border(
                                border = BorderStroke(1.dp, MathAppTheme.colors.secondaryWhite),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.TopStart),
                            text = number.toString(),
                            style = MathAppTheme.typography.chalk,
                            color = MathAppTheme.colors.secondaryWhite,
                            textAlign = TextAlign.Center,
                            fontSize = 64.sp,
                        )

                    }
                }
            }
        }
    }
}