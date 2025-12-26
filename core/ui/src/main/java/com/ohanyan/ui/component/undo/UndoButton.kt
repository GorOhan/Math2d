package com.ohanyan.ui.component.undo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.ui.R

@Composable
fun UndoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onClick()
            }
            .padding(),
    ) {
        Image(
            modifier = Modifier
                .padding(32.dp),
            painter = painterResource(R.drawable.ic_undo),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
    }
}