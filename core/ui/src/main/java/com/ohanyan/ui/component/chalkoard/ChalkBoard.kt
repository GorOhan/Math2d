package com.ohanyan.ui.component.chalkoard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.TypingAnimation

@Composable
fun ChalkBoard(
    title: String = "",
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 144.dp)
            .border(
                width = 12.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.brownBorder.copy(1f),
                        MathAppTheme.colors.brownBorder.copy(.9f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                )
                .clip(shape = RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MathAppTheme.colors.greenBack.copy(1f),
                            MathAppTheme.colors.greenBack.copy(.9f)
                        )
                    )
                )

        ) {
            TypingAnimation(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(32.dp),
                text = title,
                style = MathAppTheme.typography.chalk,
                color = MathAppTheme.colors.secondaryWhite,
                fontSize = 32,
                typingSpeed = 50L
            )
        }
        content()
    }

}

@Preview
@Composable
fun ChalkBoardPreview() {
    ChalkBoard()
}