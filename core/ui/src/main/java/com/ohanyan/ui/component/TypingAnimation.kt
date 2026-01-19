package com.ohanyan.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MathAppTheme.typography.h1Bee,
    color: Color = MathAppTheme.colors.mainBlue,
    fontSize: Int = 36,
    typingSpeed: Long = 50L
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        for (i in 1..text.length) {
            displayedText = text.take(i)
            delay(typingSpeed)
        }
    }

    Text(
        modifier = modifier,
        text = displayedText,
        style = style,
        color = color,
        fontSize = fontSize.sp
    )
}

@Composable
@Preview
fun TypingAnimationPreview() {
    TypingAnimation(
        text = "Hello, Jetpack Compose!",
        typingSpeed = 150L // Adjust typing speed in milliseconds
    )
}