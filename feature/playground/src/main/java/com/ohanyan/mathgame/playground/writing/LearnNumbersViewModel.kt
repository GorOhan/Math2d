package com.ohanyan.mathgame.playground.writing

import android.os.CountDownTimer
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ohanyan.common.musicmanager.MusicManager
import com.google.mlkit.vision.digitalink.recognition.Ink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.ohanyan.mathgame.common.data.UserPreferencesRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class LearnNumbersViewModel @Inject constructor(
    private val mlKitHelper: MLKitHelper,
    private val userPreferencesRepository: UserPreferencesRepository
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


    init {
        userPreferencesRepository.maxAvailableNumber.onEach { maxNumber ->
            _uiState.value = _uiState.value.copy(maxAvailableNumber = maxNumber)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            userPreferencesRepository.musicOn.collect { isMusicOn ->
                _uiState.update { state ->
                    state.copy(isMusicOn = isMusicOn)
                }
            }
        }
    }

    fun undoDrawing() {
        viewModelScope.launch {
            strokeBuilder = Ink.Stroke.builder()
            currentPoints.clear()
            _path.value = Path()
            _uiState.update {
                it.copy(showNextButton = false)
            }
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

        _uiState.update {
            it.copy(showNextButton = currentPoints.size > 30)
        }
        val item = Ink.Point.create(
            offsetX,
            offsetY,
            System.currentTimeMillis()
        )
        currentPoints.add(Pair(item, action))
        strokeBuilder.addPoint(item)
    }

    fun showMenu() {
        _uiState.update {
            it.copy(
                playState = PlayState.CHOOSE_NUMBER
            )
        }
    }

    private fun learnNextNumber() {
        viewModelScope.launch {
            strokeBuilder = Ink.Stroke.builder()
            currentPoints.clear()
            _path.value.reset()
            setTickerState(playState = PlayState.START)
            setTickerState(playState = PlayState.HINT)
            setTickerState(playState = PlayState.DRAW)
        }
    }

    fun afterDraw() {
        mlKitHelper.recognizeDrawing(
            hardMode = false,
            strokes = strokeBuilder.build(),
            inputNumber = uiState.value.currentNumber.toString()
        ) { isCorrect ->
            viewModelScope.launch {
                delay(1000L)
                if (isCorrect) {
                    _uiState.update { it.copy(showSuccessLottie = true) }
                    delay(5500)
                    _uiState.update { it.copy(showSuccessLottie = false) }

                    if (uiState.value.numberIteration.hasNext()) {
                        _uiState.update {
                            val currentNumber = it.numberIteration.next()
                            it.copy(currentNumber = currentNumber, nextNumber = currentNumber + 1)

                        }
                        userPreferencesRepository.updateMaxAvailableNumber(_uiState.value.currentNumber)

                        _uiState.update {
                            it.copy(
                                boardText = _uiState.value.currentNumber.toString(),
                            )
                        }
                    }
                    learnNextNumber()
                } else {
                    _uiState.update { it.copy(showErrorLottie = true) }
                    delay(4000)
                    _uiState.update { it.copy(showErrorLottie = false) }
                    learnNextNumber()
                }
                _uiState.update { it.copy(showNextButton = false) }

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
            afterDraw()
        }

        delay(playState.duration)
    }

    private fun getFormattedTime(millisUntilFinished: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        return seconds.toString()
    }

    fun onMusicOnChange(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateMusicOn(isChecked)
        }
    }

    fun onNumberChosen(number: Int) {
        if (number > _uiState.value.maxAvailableNumber) return
        viewModelScope.launch {
            while (number != _uiState.value.currentNumber) {
                _uiState.update {
                    val currentNumber = it.numberIteration.next()
                    it.copy(currentNumber = currentNumber, nextNumber = currentNumber + 1)
                }
            }
            _uiState.update {
                it.copy(
                    boardText = number.toString(),
                )
            }

            learnNextNumber()
        }
    }
}

data class LearnNumbersUIState(

    val boardText: String = "0",
    val numbers: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    val numberIteration: ListIterator<Int> = numbers.listIterator(),
    val currentNumber: Int = numberIteration.next(),
    val playState: PlayState = PlayState.NONE,
    val nextNumber: Int = 1,
    val showSuccessLottie: Boolean = false,
    val showErrorLottie: Boolean = false,
    val showHintChalk: Boolean = true,
    val isMusicOn: Boolean = MusicManager.isPlaying,
    val showNextButton: Boolean = false,
    val maxAvailableNumber: Int = 0,
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
    CHOOSE_NUMBER(0),
}