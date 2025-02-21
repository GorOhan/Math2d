package com.ohanyan.ui.component.chalkoard

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import android.graphics.Canvas as AndroidCanvas
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.TypingAnimation

@Composable
fun ChalkBoard(
    title: String = "How many items do you see?",
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    val path = remember { Path() }
    val lastPosition = remember { mutableStateOf<Offset?>(null) }
    val chalkColor = MathAppTheme.colors.coreWhite
    val chalkBrush = Brush.sweepGradient(
        listOf(Color.White.copy(alpha = 0.8f), Color.Gray.copy(alpha = 0.5f))
    )


//    Canvas(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(vertical = 48.dp, horizontal = 144.dp)
//            .zIndex(2f)
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { offset ->
//                        path.moveTo(offset.x, offset.y)
//                        lastPosition.value = offset
//                    },
//                    onDrag = { change, _ ->
//                        change.consume()
//                        lastPosition.value?.let {
//                            path.lineTo(change.position.x, change.position.y)
//                        }
//                        lastPosition.value = change.position
//                    }
//                )
//            }
//    ) {
//        drawPath(
//            path = path,
//            color = chalkColor,
//            style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
//        )
//    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 144.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.brownBorder.copy(1f),
                        MathAppTheme.colors.brownBorder.copy(.9f)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                )
                .clip(shape = RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MathAppTheme.colors.greenBack.copy(1f),
                            MathAppTheme.colors.greenBack.copy(.9f)
                        )
                    )
                )

        ) {
            TypingAnimation(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                text = title,
                style = MathAppTheme.typography.chalk,
                color = MathAppTheme.colors.secondaryWhite,
                fontSize = 32,
                typingSpeed = 50L
            )
            content()
        }
    }

}

fun pathToBitmap(path: Path): Bitmap {
    val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
    val canvas = AndroidCanvas(bitmap)
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLUE
        strokeWidth = 4f
        style = android.graphics.Paint.Style.STROKE
    }
    canvas.drawPath(path.asAndroidPath(), paint)
    return bitmap
}

@Preview
@Composable
fun ChalkBoardPreview() {
    ChalkBoard()
}