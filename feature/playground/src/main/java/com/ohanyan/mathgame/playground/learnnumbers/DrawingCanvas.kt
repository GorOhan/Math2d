package com.ohanyan.mathgame.playground.learnnumbers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import com.google.mlkit.vision.digitalink.Ink
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme

@Composable
fun DrawOnCanvas(
    number: Int,
    modifier: Modifier,
    onDragEnd: (strokes:List<Ink.Stroke>) -> Unit = {},
) {
    val path = remember { Path() }
    val lastPosition = remember { mutableStateOf<Offset?>(null) }
    val chalkColor = MathAppTheme.colors.coreWhite
    val scope = rememberCoroutineScope()

    val strokes = remember { mutableListOf<Ink.Stroke>() }
    var strokeBuilder = remember { Ink.Stroke.builder() }

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
                            strokeBuilder.addPoint(
                                Ink.Point.create(
                                    change.position.x,
                                    change.position.y,
                                    System.currentTimeMillis()
                                )
                            )
                        },
                        onDragEnd = {
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
            }

        }
    }
}

