package com.ohanyan.mathgame.playground.count

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme


@Composable
fun DrawOnCanvas() {
    val path = remember { Path() }
    val context = LocalContext.current
    val lastPosition = remember { mutableStateOf<Offset?>(null) }
    val chalkColor = MathAppTheme.colors.coreWhite
    val rec = DigitRecognizer(context)
    var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 144.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            path.moveTo(offset.x, offset.y)
                            lastPosition.value = offset
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            lastPosition.value?.let {
                                path.lineTo(change.position.x, change.position.y)
                            }
                            lastPosition.value = change.position
                        },
                        onDragEnd = {
                            capturedImage = captureDrawing(path = path, density = density)

                            println("I RECOGNIZED ${capturedImage?.asAndroidBitmap()
                                ?.let { rec.recognize(it) }}")
                         }
                    )
                }
        ) {
            drawPath(
                path = path,
                color = chalkColor,
                style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )

        }


        // Display Captured Image
        capturedImage?.let {
            Image(
                bitmap = it,
                contentDescription = "Captured Drawing",
                modifier = Modifier
                    .size(244.dp)
                    .background(color = MathAppTheme.colors.red)
                    .padding(16.dp)
            )
        }
    }
}

fun captureDrawing(path: Path, density: Float): ImageBitmap {
    val width = (300 * density).toInt()
    val height = (300 * density).toInt()

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    val paint = android.graphics.Paint().apply {
        this.color = Color.WHITE
        this.strokeWidth = 16f
        this.style = android.graphics.Paint.Style.STROKE
        this.isAntiAlias = true
        this.strokeCap = android.graphics.Paint.Cap.ROUND
        this.strokeJoin = android.graphics.Paint.Join.ROUND
    }

    val androidPath = android.graphics.Path().apply {
        path.asAndroidPath().let { this.set(it) }
    }

    canvas.drawPath(androidPath, paint)


    return bitmap.asImageBitmap()
}