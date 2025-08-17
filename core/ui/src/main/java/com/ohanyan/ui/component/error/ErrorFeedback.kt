package com.ohanyan.ui.component.error

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme

@Composable
fun ErrorFeedback(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    errorMessage: String = "Oops! Try again!",
    recognizedText: String = "",
    expectedText: String = "",
    onTryAgain: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(100),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
        exit = scaleOut(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MathAppTheme.colors.red.copy(alpha = alpha)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                // Error icon (red circle with X) with shake animation
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(scale)
                        .graphicsLayer(
                            translationX = shakeOffset
                        )
                        .background(
                            color = MathAppTheme.colors.red,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = MathAppTheme.colors.coreWhite,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Error message
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(top = 16.dp),
                    style = MathAppTheme.typography.chalk,
                    color = MathAppTheme.colors.red,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                
                // Show what was recognized vs expected (if available)
                if (recognizedText.isNotEmpty() && expectedText.isNotEmpty()) {
                    Text(
                        text = "You drew: $recognizedText",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MathAppTheme.typography.chalk,
                        color = MathAppTheme.colors.secondaryWhite,
                        fontSize = 18.sp
                    )
                    
                    Text(
                        text = "Try drawing: $expectedText",
                        modifier = Modifier.padding(top = 4.dp),
                        style = MathAppTheme.typography.chalk,
                        color = MathAppTheme.colors.coreYellow,
                        fontSize = 18.sp
                    )
                }
                
                // Try Again button
                Box(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .clickable { onTryAgain() }
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MathAppTheme.colors.coreYellow.copy(0.8f),
                                    MathAppTheme.colors.coreYellow.copy(0.6f)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MathAppTheme.colors.coreWhite,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Try Again",
                        style = MathAppTheme.typography.chalk,
                        color = MathAppTheme.colors.coreWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
