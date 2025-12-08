package com.ohanyan.mathgame.playground.writing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.google.mlkit.vision.digitalink.Ink
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlinx.coroutines.delay

@Composable
fun HintDigitAnimation(
    modifier: Modifier,
    number: Int,
    boardText: String,
    resetCanvas: Boolean = false,
) {
    var index by remember { mutableIntStateOf(1) }

    var maxX by remember { mutableFloatStateOf(1f) }
    var maxY by remember { mutableFloatStateOf(1f) }

    var offsets by remember { mutableStateOf(listOf<Offset>()) }

    val chalkPosition = remember { mutableStateOf(Offset.Zero) }

    val context = LocalContext.current
    val icChalk = context.getDrawable(com.ohanyan.mathgame.ui.R.drawable.ic_chalk)?.toBitmap()

    LaunchedEffect(Unit) {
        delay(200L)
        offsets = generateZeroOffsets(maxX,maxY)
        offsets.indices.drop(1).forEach { i ->
            delay(10)
            index = i
        }
    }
    val height = LocalDensity.current.density

    val path = remember { Path() }
    val lastPosition = remember { mutableStateOf<Offset?>(null) }
    val chalkColor = MathAppTheme.colors.coreWhite
    val strokes = remember { mutableListOf<Ink.Stroke>() }

    // Reset canvas when resetCanvas is true
    if (resetCanvas) {
        index = 1
        path.reset()
        strokes.clear()
        lastPosition.value = null
    }

    Box {
        key(number) {
            Canvas(modifier = modifier.fillMaxSize()) {

                maxX = size.width
                maxY = size.height
                clipRect {
                    if (offsets.isEmpty() || index < 1 ) return@Canvas

                    val path = Path().apply {
                        moveTo(offsets[0].x, offsets[0].y)
                        for (i in 1..index) {
                            chalkPosition.value = offsets[i]
                            lineTo(offsets[i].x, offsets[i].y)
                        }
                    }

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
}

fun generateZeroOffsets(
    maxX: Float,
    maxY: Float,
    paddingFactor: Float = 0.5f,   // 90% of the area
    step: Float = 1f
): List<Offset> {

    // shrink radii
    val radiusX = (maxX / 2f) * paddingFactor
    val radiusY = (maxY / 2f) * paddingFactor

    // keep center the same
    val centerX = maxX / 2f
    val centerY = maxY / 2f

    val points = mutableListOf<Offset>()

    var angle = 0f
    while (angle < 360f) {
        val rad = Math.toRadians(angle.toDouble())

        val x = centerX + radiusX * kotlin.math.cos(rad).toFloat()
        val y = centerY + radiusY * kotlin.math.sin(rad).toFloat()

        points.add(Offset(x, y))

        angle += step
    }

    return points
}
