package com.ohanyan.mathgame.playground.writing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay

@Composable
fun DrawOnCanvas(
    number: Int,
    modifier: Modifier,
    onDragEnd: (strokes: List<Ink.Stroke>) -> Unit = {},
    offsets: List<Offset> = emptyList(),
    resetCanvas: Boolean = false,
) {
//    val offsets = listOf(
//        Offset(602.0244f, 133.86035f),
//        Offset(602.0244f, 133.86035f),
//        Offset(584.0303f, 151.83203f),
//        Offset(561.7959f, 183.08008f),
//        Offset(544.00684f, 200.84863f),
//        Offset(528.25977f, 225.7539f),
//        Offset(519.876f, 244.02832f),
//        Offset(506.4502f, 256.4961f),
//        Offset(510.92578f, 261.0f),
//        Offset(510.92578f, 270.0088f),
//        Offset(510.92578f, 292.46484f),
//        Offset(510.92578f, 303.89648f),
//        Offset(510.92578f, 319.51367f),
//        Offset(510.92578f, 329.88184f),
//        Offset(510.92578f, 345.4629f),
//        Offset(510.92578f, 353.26953f),
//        Offset(510.92578f, 362.30957f),
//        Offset(510.92578f, 371.36523f),
//        Offset(510.92578f, 380.05762f),
//        Offset(523.4873f, 385.98047f),
//        Offset(530.0215f, 394.98926f),
//        Offset(528.9209f, 408.50293f),
//        Offset(540.91797f, 417.51172f),
//        Offset(550.209f, 426.3838f),
//        Offset(554.91504f, 422.01562f),
//        Offset(565.084f, 430.01367f),
//        Offset(573.11426f, 439.18066f),
//        Offset(586.33594f, 438.97852f),
//        Offset(594.3369f, 438.97852f),
//        Offset(606.7422f, 438.97852f),
//        Offset(622.32715f, 438.97852f),
//        Offset(632.93066f, 438.97852f),
//        Offset(650.81445f, 432.09375f),
//        Offset(660.7988f, 423.02148f),
//        Offset(677.8369f, 406.08398f),
//        Offset(688.88184f, 388.33105f),
//        Offset(707.6221f, 367.2578f),
//        Offset(716.8418f, 340.06543f),
//        Offset(723.9209f, 313.79883f),
//        Offset(732.8711f, 285.59082f),
//        Offset(741.917f, 250.41699f),
//        Offset(750.8672f, 232.9873f),
//        Offset(759.7539f, 208.29883f),
//        Offset(759.9121f, 189.30078f),
//        Offset(759.9121f, 174.20996f),
//        Offset(759.9121f, 164.09961f),
//        Offset(759.9121f, 149.48926f),
//        Offset(759.9121f, 141.52734f),
//        Offset(759.9121f, 132.60156f),
//        Offset(759.9121f, 120.243164f),
//        Offset(750.8672f, 112.03711f),
//        Offset(743.03125f, 110.00391f),
//        Offset(731.5615f, 100.99512f),
//        Offset(723.9209f, 100.99512f),
//        Offset(706.22754f, 100.99512f),
//        Offset(693.45215f, 100.99512f),
//        Offset(679.2119f, 100.99512f),
//        Offset(670.4336f, 100.99512f),
//        Offset(661.9365f, 100.99512f),
//        Offset(650.1406f, 112.74219f),
//        Offset(637.1904f, 124.72461f),
//        Offset(619.7881f, 133.86426f),
//        Offset(602.2529f, 142.6084f),
//        Offset(592.07715f, 152.81445f),
//        Offset(586.6377f, 158.28906f),
//        Offset(590.90625f, 163.00195f)
//    )

    val offsets = listOf(
        // Top curve of "2"
        Offset(40f, 40f),
        Offset(60f, 20f),
        Offset(90f, 10f),
        Offset(130f, 10f),
        Offset(160f, 25f),
        Offset(180f, 45f),
        Offset(190f, 70f),
        Offset(190f, 95f),
        Offset(175f, 120f),
        Offset(150f, 140f),
        Offset(120f, 155f),

        // Middle diagonal
        Offset(95f, 175f),
        Offset(70f, 205f),
        Offset(55f, 230f),
        Offset(45f, 260f),

        // Bottom base
        Offset(50f, 285f),
        Offset(80f, 300f),
        Offset(120f, 305f),
        Offset(160f, 300f),
        Offset(190f, 285f)
    )
//    val offsets = listOf(
//        Offset(100f, 0f),
//        Offset(140f, 10f),
//        Offset(170f, 40f),
//        Offset(190f, 90f),
//        Offset(200f, 150f),
//        Offset(190f, 210f),
//        Offset(170f, 260f),
//        Offset(140f, 290f),
//        Offset(100f, 300f),
//        Offset(60f, 290f),
//        Offset(30f, 260f),
//        Offset(10f, 210f),
//        Offset(0f, 150f),
//        Offset(10f, 90f),
//        Offset(30f, 40f),
//        Offset(60f, 10f),
//        Offset(100f, 0f)  // closes the loop
//    )

    var index by remember { mutableStateOf(1) }

    LaunchedEffect(offsets) {
        delay(200L)
        offsets.indices.drop(1).forEach { i ->
            delay(40)
            index = i
        }
    }

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
                         //   strokes.clear()
                            strokeBuilder = Ink.Stroke.builder()
                        //    path.reset()

                            println()
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
                            println("mypath $strokes")
                          //  onDragEnd(strokes)

                            //todo should be cleared after recognize to  immediately
                         //   strokes.clear()
                         //   strokeBuilder = Ink.Stroke.builder()
                        //    path.reset()

                        }
                    )
                }
        ) {
            clipRect {
                if (index < 1) return@Canvas

                val path = Path().apply {
                    moveTo(offsets[0].x, offsets[0].y)
                    for (i in 1..index) {
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


