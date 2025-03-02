package com.ohanyan.mathgame.playground.count

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LearnCountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LearnCountUIState())
    val uiState = _uiState.asStateFlow()

    val mockList = listOf(
        CountTestItem(count = 7, imgResId = com.ohanyan.mathgame.ui.R.drawable.icecream2, listOf("7","3")),
        CountTestItem(count = 13, imgResId = com.ohanyan.mathgame.ui.R.drawable.chipmunk, listOf("7","13")),
        CountTestItem(count = 2, imgResId = com.ohanyan.mathgame.ui.R.drawable.fox, listOf("4","1")),
        CountTestItem(count = 6, imgResId = com.ohanyan.mathgame.ui.R.drawable.giraff, listOf("2","5")),
        CountTestItem(count = 5, imgResId = com.ohanyan.mathgame.ui.R.drawable.ic_apple, listOf("2","3")),
    )

    init {
        _uiState.update {
            it.copy(
                currentTest = mockList.first(),
            )
        }
    }

    private fun nextOption() {
        viewModelScope.launch {
            delay(1500L)
            val index = mockList.indexOf(uiState.value.currentTest)
            _uiState.update {
                it.copy(
                    currentTest = mockList[index + 1],
                    isAnsweredCorrect = null
                )
            }
        }
    }

    fun onOptionSelected(value: String) {
        viewModelScope.launch {
            if (value == uiState.value.currentTest?.count.toString()) {
                _uiState.update {
                    it.copy(
                        isAnsweredCorrect = true,
                    )
                }
                nextOption()
            } else {
                _uiState.update {
                    it.copy(
                        isAnsweredCorrect = false,
                    )
                }

            }
        }
    }
}

data class LearnCountUIState(
    val currentTest: CountTestItem? = null,
    val isAnsweredCorrect: Boolean? = null
)

data class CountTestItem(
    val count: Int = -1,
    @DrawableRes val imgResId: Int = -1,
    val answerOptions: List<String> = emptyList(),
)