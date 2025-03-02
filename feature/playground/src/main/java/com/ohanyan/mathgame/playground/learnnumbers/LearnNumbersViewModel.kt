package com.ohanyan.mathgame.playground.learnnumbers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LearnNumbersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LearnNumbersUIState())
    val uiState = _uiState.asStateFlow()

    private fun nextNumber() {
        if (uiState.value.numberIteration.hasNext()) {
            _uiState.update {
                it.copy(currentNumber = it.numberIteration.next())
            }
            makeBoardText(BoardTextState.INITIAL)
        }
    }

    fun makeBoardText(boardTextState: BoardTextState) {
        viewModelScope.launch {
            val number = uiState.value.currentNumber
            _uiState.update {
                when (boardTextState) {
                    BoardTextState.INITIAL -> {
                        it.copy(
                            boardText = " $number, $number, $number, ..."
                        )
                    }

                    BoardTextState.FULL -> {
                        it.copy(
                            boardText = " $number, $number, $number, $number"
                        )
                    }
                }
            }
            if (boardTextState == BoardTextState.FULL) {
                delay(600L)
                nextNumber()
            }
        }
    }
}


enum class BoardTextState {
    INITIAL,
    FULL,
}

data class LearnNumbersUIState(
    val boardText: String = "0,0,0,0,0...",
    val numbers: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    val numberIteration: ListIterator<Int> = numbers.listIterator(),
    val currentNumber: Int = numberIteration.next(),
)