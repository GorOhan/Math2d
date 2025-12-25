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
import androidx.core.graphics.drawable.toBitmap
import com.google.mlkit.vision.digitalink.Ink
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import kotlinx.coroutines.delay

@Composable
fun HintDigitAnimation(
    modifier: Modifier,
    number: Int,
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
        offsets = getHintByNumber(number = number,maxX,maxY)
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
                println("MAX MAX $maxX $maxY")
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

fun getHintByNumber(number: Int, maxX: Float, maxY: Float): List<Offset> {
    return when (number) {
        0 -> generateZeroOffsets(maxX, maxY)
        1 -> generateOneOffsets()
        3 -> generateTwoOffsets()
        else -> generateZeroOffsets(maxX, maxY)
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

fun generateOneOffsets(
    targetMaxX: Float = 590f,
    targetMaxY: Float = 1104f
): List<Offset> {

    val original = listOf(
        Offset(836.86035f, 374.98828f),
        Offset(846.83496f, 370.02246f),
        Offset(871.34375f, 360.00293f),
        Offset(900.31445f, 343.5459f),
        Offset(906.4297f, 337.50293f),
        Offset(923.06445f, 315.8506f),
        Offset(923.90234f, 295.0078f),
        Offset(928.94824f, 418.42578f),
        Offset(928.94824f, 522.2803f),
        Offset(923.90234f, 599.5078f),
        Offset(918.9512f, 654.50684f),
        Offset(918.9512f, 714.458f),
        Offset(913.9043f, 725.0127f),
        Offset(900.98535f, 727.8877f),
        Offset(858.2715f, 729.9785f),
        Offset(809.08887f, 734.9883f),
        Offset(734.2471f, 734.9883f),
        Offset(721.83984f, 727.8418f),
        Offset(773.0742f, 729.9785f),
        Offset(860.92773f, 725.0127f),
        Offset(958.042f, 725.0127f),
        Offset(1060.125f, 739.99805f)
    )

    val minX = original.minOf { it.x }
    val minY = original.minOf { it.y }
    val maxX = original.maxOf { it.x }
    val maxY = original.maxOf { it.y }

    val scaleX = targetMaxX / (maxX - minX)
    val scaleY = targetMaxY / (maxY - minY)

    return original.map {
        Offset(
            x = (it.x - minX) * scaleX*.6f,
            y = (it.y - minY) * scaleY*.6f
        )
    }
}


val offsets1 = listOf(
    Offset(836.86035f, 374.98828f),
    Offset(836.86035f, 374.98828f),
    Offset(846.83496f, 370.02246f),
    Offset(856.4346f, 365.0127f),
    Offset(871.34375f, 360.00293f),
    Offset(874.32227f, 354.5879f),
    Offset(884.7256f, 354.99316f),
    Offset(889.28613f, 349.60352f),
    Offset(896.4326f, 350.02734f),
    Offset(900.31445f, 343.5459f),
    Offset(906.4297f, 337.50293f),
    Offset(906.1289f, 334.99805f),
    Offset(908.8584f, 327.94043f),
    Offset(915.94336f, 329.98828f),
    Offset(913.9043f, 325.02246f),
    Offset(918.9512f, 321.8496f),
    Offset(923.06445f, 315.8506f),
    Offset(923.90234f, 310.7881f),
    Offset(923.90234f, 307.5537f),
    Offset(923.90234f, 305.02734f),
    Offset(929.21094f, 305.02734f),
    Offset(928.94824f, 297.5127f),
    Offset(928.94824f, 295.0078f),
    Offset(928.94824f, 311.62207f),
    Offset(928.94824f, 332.50684f),
    Offset(928.94824f, 368.96094f),
    Offset(928.94824f, 418.42578f),
    Offset(928.94824f, 463.32227f),
    Offset(928.94824f, 522.2803f),
    Offset(925.7168f, 567.792f),
    Offset(923.90234f, 599.5078f),
    Offset(920.8545f, 629.2344f),
    Offset(918.9512f, 654.50684f),
    Offset(918.9512f, 671.14453f),
    Offset(918.9512f, 688.1572f),
    Offset(918.9512f, 698.1758f),
    Offset(911.6406f, 714.458f),
    Offset(913.9043f, 719.9668f),
    Offset(913.9043f, 723.5381f),
    Offset(913.9043f, 725.0127f),
    Offset(906.54785f, 725.0127f),
    Offset(900.98535f, 727.8877f),
    Offset(883.8623f, 729.9785f),
    Offset(858.2715f, 729.9785f),
    Offset(834.1289f, 734.9199f),
    Offset(809.08887f, 734.9883f),
    Offset(783.23535f, 734.9883f),
    Offset(764.8047f, 734.9883f),
    Offset(749.08496f, 734.9883f),
    Offset(734.2471f, 734.9883f),
    Offset(728.9971f, 734.9883f),
    Offset(721.83984f, 727.8418f),
    Offset(731.79297f, 729.9785f),
    Offset(749.83496f, 729.9785f),
    Offset(773.0742f, 729.9785f),
    Offset(806.1953f, 725.0127f),
    Offset(833.1748f, 725.0127f),
    Offset(860.92773f, 725.0127f),
    Offset(883.458f, 725.0127f),
    Offset(903.3799f, 725.0127f),
    Offset(918.58203f, 725.0127f),
    Offset(938.36914f, 725.0127f),
    Offset(958.042f, 725.0127f),
    Offset(985.40234f, 728.2246f),
    Offset(1003.9707f, 729.9785f),
    Offset(1019.0254f, 734.9883f),
    Offset(1036.7441f, 734.9883f),
    Offset(1046.8643f, 739.99805f),
    Offset(1056.3457f, 739.99805f),
    Offset(1060.125f, 739.99805f)
)

fun generateTwoOffsets(
    targetMaxX: Float = 590f,
    targetMaxY: Float = 1104f
): List<Offset> {

    val original = listOf(
        Offset(765.45703f, 234.47266f),
        Offset(776.5117f, 223.38184f),
        Offset(791.49414f, 221.0039f),
        Offset(808.5381f, 211.99512f),
        Offset(832.6328f, 202.10156f),
        Offset(848.01465f, 203.03027f),
        Offset(869.2363f, 195.03223f),
        Offset(888.7012f, 195.03223f),
        Offset(912.8574f, 195.03223f),
        Offset(942.04297f, 195.03223f),
        Offset(968.83105f, 195.03223f),
        Offset(994.6592f, 195.03223f),
        Offset(1004.3125f, 202.48438f),
        Offset(1012.92773f, 210.8877f),
        Offset(1019.70703f, 218.81836f),
        Offset(1021.8779f, 221.0039f),

        // vertical down
        Offset(1021.8779f, 256.46875f),
        Offset(1021.8779f, 300.26074f),
        Offset(1021.8779f, 350.99414f),
        Offset(1004.9297f, 385.13086f),
        Offset(986.9346f, 427.90527f),

        // curve
        Offset(950.0039f, 480.98438f),
        Offset(923.5049f, 497.9912f),
        Offset(888.5166f, 507.0f),
        Offset(854.0625f, 507.0f),
        Offset(826.3545f, 507.0f),

        // bottom line
        Offset(843.47363f, 497.9912f),
        Offset(891.0137f, 480.98438f),
        Offset(959.2627f, 472.01953f),
        Offset(1004.06445f, 464.78613f),
        Offset(1042.667f, 472.01953f),

        // tail
        Offset(1066.4453f, 508.54004f),
        Offset(1082.9111f, 553.4824f),
        Offset(1082.9111f, 616.1338f),
        Offset(1082.9111f, 678.85254f),
        Offset(1060.6533f, 709.79297f),
        Offset(1017.3447f, 741.0088f)
    )

    val minX = original.minOf { it.x }
    val minY = original.minOf { it.y }
    val maxX = original.maxOf { it.x }
    val maxY = original.maxOf { it.y }

    val scaleX = targetMaxX / (maxX - minX)
    val scaleY = targetMaxY / (maxY - minY)

    return original.map {
        Offset(
            x = (it.x - minX) * scaleX,
            y = (it.y - minY) * scaleY
        )
    }
}

