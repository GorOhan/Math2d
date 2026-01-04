package com.ohanyan.mathgame.playground.writing

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme

@Composable
fun DrawingArea(
    path: Path,
    number: Int,
    modifier: Modifier,
    shouldShake: Boolean = false,
    addPoint: (offsetX: Float, offsetY: Float, action: PathAction) -> Unit = { _, _,_ -> }
) {
    val context = LocalContext.current
    val icChalk = context.getDrawable(com.ohanyan.mathgame.ui.R.drawable.ic_chalk)?.toBitmap()
    val lastPosition = remember { mutableStateOf<Offset?>(null) }
    val chalkColor = MathAppTheme.colors.coreWhite

    val chalkPosition = remember { mutableStateOf(Offset.Zero) }
    
    // Shake animation
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (shouldShake) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )

    key(number) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .offset(x = if (shouldShake) shakeOffset.dp else 0.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            addPoint(offset.x, offset.y, PathAction.MOVE)
                            lastPosition.value = offset
                            chalkPosition.value = offset
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            lastPosition.value?.let {
                                addPoint(change.position.x, change.position.y, PathAction.LINE)
                            }
                            lastPosition.value = change.position
                            chalkPosition.value = change.position
                        },
                        onDragEnd = {
                            chalkPosition.value = Offset.Zero
                        }
                    )
                }
        ) {
            clipRect {
                drawPath(
                    path = path,
                    color = chalkColor,
                    style = Stroke(width = 18f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                )

                if (chalkPosition.value != Offset.Zero) {
                    icChalk?.asImageBitmap()?.let {
                        drawImage(
                            image = it,
                            topLeft = chalkPosition.value - Offset(
                                x = 0f,
                                y = 50f
                            )
                        )
                    }
                }
            }
        }
    }
}

enum class PathAction {
    MOVE,
    LINE
}
