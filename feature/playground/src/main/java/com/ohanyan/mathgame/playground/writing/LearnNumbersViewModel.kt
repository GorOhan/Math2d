package com.ohanyan.mathgame.playground.writing

import android.os.CountDownTimer
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.digitalink.Ink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LearnNumbersViewModel @Inject constructor(
    private val mlKitHelper: MLKitHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnNumbersUIState())
    val uiState = _uiState.asStateFlow()

    private val _tickerState = MutableStateFlow(TickerState())
    val tickerState = _tickerState.asStateFlow()

    private val _path = MutableStateFlow(Path())
    val path = _path.asStateFlow()

    private var strokeBuilder = Ink.Stroke.builder()

    private val currentPoints = mutableListOf<Pair<Ink.Point, PathAction>>()
    private var countDownTimer: CountDownTimer? = null

    fun undoDrawing() {
        viewModelScope.launch {
            strokeBuilder = Ink.Stroke.builder()
            currentPoints.clear()
            _path.value.reset()
//            currentPoints.removeAt(currentPoints.lastIndex)
//            _path.value.reset()
//            currentPoints.forEachIndexed { index, it ->
//                strokeBuilder.addPoint(it.first)
//                when (it.second) {
//                    PathAction.MOVE -> {
//                        _path.value.moveTo(it.first.x, it.first.y)
//                    }
//                    PathAction.LINE -> {
//                        _path.value.lineTo(it.first.x, it.first.y)
//                    }
//                }
//            }
        }
    }

    fun addPoints(offsetX: Float, offsetY: Float, action: PathAction) {
        when (action) {
            PathAction.MOVE -> {
                hideHintChalk()
                _path.value.moveTo(offsetX, offsetY)
            }

            PathAction.LINE -> {
                _path.value.lineTo(offsetX, offsetY)

            }
        }
        val item = Ink.Point.create(
            offsetX,
            offsetY,
            System.currentTimeMillis()
        )
        currentPoints.add(Pair(item, action))
        strokeBuilder.addPoint(item)
    }

    fun learnNextNumber() {
        viewModelScope.launch {
            strokeBuilder = Ink.Stroke.builder()
            currentPoints.clear()
            _path.value.reset()
            setTickerState(playState = PlayState.START)
            setTickerState(playState = PlayState.HINT)
            setTickerState(playState = PlayState.DRAW)
        }
    }

    private fun afterDraw(strokes: Ink.Stroke) {
        mlKitHelper.recognizeDrawing(strokes) { recognizedText ->
            viewModelScope.launch {
                delay(1000L)
                val isCorrect = recognizedText == uiState.value.currentNumber.toString()
                if (isCorrect) {
                    _uiState.update { it.copy(showSuccessLottie = true) }
                    delay(5500)
                    _uiState.update { it.copy(showSuccessLottie = false) }

                    if (uiState.value.numberIteration.hasNext()) {
                        _uiState.update {
                            it.copy(currentNumber = it.numberIteration.next())
                        }

                        _uiState.update {
                            it.copy(
                                boardText = _uiState.value.currentNumber.toString(),
                            )
                        }
                    }
                }
                learnNextNumber()
            }
        }
    }

    private fun startCountDown(mills: Long, playState: PlayState, onFinish: () -> Unit) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(mills, 10) {
            override fun onTick(millisUntilFinished: Long) {
                _tickerState.update {
                    it.copy(
                        tickerProgress = millisUntilFinished.toFloat() / mills,
                        tickerValue = getFormattedTime(millisUntilFinished)
                    )
                }
            }

            override fun onFinish() {
                if (playState == PlayState.DRAW) {
                    onFinish()
                }
                this.cancel()
            }
        }

        countDownTimer?.start()
    }

    private fun hideHintChalk() {
        _uiState.update { it.copy(showHintChalk = false) }
    }

    private suspend fun setTickerState(playState: PlayState) {
        _uiState.update {
            it.copy(playState = playState, showHintChalk = true)
        }

        startCountDown(playState.duration, playState = playState) {
            afterDraw(strokeBuilder.build())
        }

        delay(playState.duration)
    }

    private fun getFormattedTime(millisUntilFinished: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        return seconds.toString()
    }
}

data class LearnNumbersUIState(
    val boardText: String = "0",
    val numbers: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    val numberIteration: ListIterator<Int> = numbers.listIterator(),
    val currentNumber: Int = numberIteration.next(),
    val playState: PlayState = PlayState.NONE,
    val showSuccessLottie: Boolean = false,
    val showHintChalk: Boolean = true
)

data class TickerState(
    val tickerProgress: Float = 0.0f,
    val tickerValue: String = ""
)

enum class PlayState(val duration: Long) {
    START(4_000),
    HINT(7_500),
    DRAW(25_000),
    NONE(0),
}