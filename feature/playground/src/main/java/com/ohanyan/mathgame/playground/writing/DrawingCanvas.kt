package com.ohanyan.mathgame.playground.writing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.google.mlkit.vision.digitalink.Ink
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.playground.R

@Composable
fun DrawOnCanvas(
    number: Int,
    modifier: Modifier,
    onDragEnd: (strokes: List<Ink.Stroke>) -> Unit = {},
    resetCanvas: Boolean = false,
) {
    val path = remember { Path() }
    val lastPosition = remember { mutableStateOf<Offset?>(null) }
    val chalkColor = MathAppTheme.colors.coreWhite

    val context = LocalContext.current
    val icChalk = context.getDrawable(com.ohanyan.mathgame.ui.R.drawable.ic_chalk)?.toBitmap()

    val strokes = remember { mutableListOf<Ink.Stroke>() }
    var strokeBuilder = remember { Ink.Stroke.builder() }
    val chalkPosition = remember { mutableStateOf(Offset.Zero) }

    // Reset canvas when resetCanvas is true
    if (resetCanvas) {
        path.reset()
        strokes.clear()
        strokeBuilder = Ink.Stroke.builder()
        lastPosition.value = null
    }

    key(number) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            strokes.clear()
                            strokeBuilder = Ink.Stroke.builder()
                            path.reset()

                            path.moveTo(offset.x, offset.y)
                            lastPosition.value = offset
                            chalkPosition.value = offset

                            strokeBuilder.addPoint(
                                Ink.Point.create(
                                    offset.x,
                                    offset.y,
                                    System.currentTimeMillis()
                                )
                            )
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            lastPosition.value?.let {
                                path.lineTo(change.position.x, change.position.y)
                            }
                            lastPosition.value = change.position
                            chalkPosition.value = change.position
                            strokeBuilder.addPoint(
                                Ink.Point.create(
                                    change.position.x,
                                    change.position.y,
                                    System.currentTimeMillis()
                                )
                            )
                        },
                        onDragEnd = {
                            chalkPosition.value = Offset.Zero

                            strokes.add(strokeBuilder.build())
                            println(strokes)
                            onDragEnd(strokes)

                            //todo should be cleared after recognize to  immediately
                            strokes.clear()
                            strokeBuilder = Ink.Stroke.builder()
                            path.reset()

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


