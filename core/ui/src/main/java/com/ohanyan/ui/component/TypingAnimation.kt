package com.ohanyan.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation(text: String, typingSpeed: Long = 50L) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        for (i in 1..text.length) {
            displayedText = text.substring(0, i)
            delay(typingSpeed)
        }
    }
    Text(
        text = displayedText,
        style = MathAppTheme.typography.display,
        color = MathAppTheme.colors.coreYellow,
    )
}

@Composable
fun TypingAnimationPreview() {
    TypingAnimation(
        text = "Hello, Jetpack Compose!",
        typingSpeed = 150L // Adjust typing speed in milliseconds
    )
}