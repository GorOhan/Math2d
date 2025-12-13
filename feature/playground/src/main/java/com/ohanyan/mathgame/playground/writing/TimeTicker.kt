package com.ohanyan.mathgame.playground.writing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme

@Composable
fun TimeTicker(
    modifier: Modifier = Modifier,
    state: State<TickerState>
) {
    val yellow = MathAppTheme.colors.coreYellow

    Box(
        modifier = modifier
            .size(64.dp),
        contentAlignment = Alignment.Center

    ) {
        Canvas(modifier = Modifier.size(60.dp)) {
            drawArc(
                color = yellow,
                -45f,
                360 * state.value.tickerProgress,
                useCenter = false,
                style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = state.value.tickerValue,
            modifier = Modifier
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            style = MathAppTheme.typography.display,
            fontSize = 32.sp,
            color = MathAppTheme.colors.coreYellow
        )
    }
}

@Composable
@MathPreview
fun TimeTickerPreview() {
    // TimeTicker(state = TimeTickerState(playState = PlayState.DRAW))
}