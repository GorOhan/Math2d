package com.ohanyan.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation(
    modifier: Modifier = Modifier,
    text: String,
    typingSpeed: Long = 50L
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        for (i in 1..text.length) {
            displayedText = text.substring(0, i)
            delay(typingSpeed)
        }
    }
    Text(
        modifier = modifier,
        text = displayedText,
        style = MathAppTheme.typography.h1Bee,
        color = MathAppTheme.colors.mainBlue,
        fontSize = 36.sp
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