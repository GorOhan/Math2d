package com.ohanyan.mathgame.playground.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.digitalink.Ink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnNumbersViewModel @Inject constructor(
    private val mlKitHelper: MLKitHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnNumbersUIState())
    val uiState = _uiState.asStateFlow()

    init {
        learnNextNumber()
    }

    fun learnNextNumber() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(playState = PlayState.START)
            }
            delay(3000L)
            _uiState.update {
                it.copy(playState = PlayState.HINT)
            }
            delay(5000L)
            _uiState.update {
                it.copy(playState = PlayState.DRAW)
            }
        }
    }

    fun afterDraw(strokes: List<Ink.Stroke>) {
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
                                playState = PlayState.START
                            )
                        }
                        learnNextNumber()
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            playState = PlayState.START
                        )
                    }
                    learnNextNumber()
                }

            }
        }
    }
}

data class LearnNumbersUIState(
    val boardText: String = "0",
    val numbers: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    val numberIteration: ListIterator<Int> = numbers.listIterator(),
    val currentNumber: Int = numberIteration.next(),
    val playState: PlayState = PlayState.NONE,
    val showSuccessLottie: Boolean = false
)

enum class PlayState {
    START,
    HINT,
    DRAW,
    NONE,
}