package com.ohanyan.mathgame.playground.count

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LearnCountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LearnCountUIState())
    val uiState = _uiState.asStateFlow()

    val mockList = listOf(
        CountOption(count = 3, imgResId = com.ohanyan.mathgame.ui.R.drawable.icecream2),
        CountOption(count = 3, imgResId = com.ohanyan.mathgame.ui.R.drawable.chipmunk),
        CountOption(count = 4, imgResId = com.ohanyan.mathgame.ui.R.drawable.fox),
        CountOption(count = 4, imgResId = com.ohanyan.mathgame.ui.R.drawable.giraff),
        CountOption(count = 5, imgResId = com.ohanyan.mathgame.ui.R.drawable.ic_apple),
    )

    init {
        _uiState.update {
            it.copy(
                options = mockList,
                currentOptionIndex = 2
            )
        }
    }

    fun nextOption() {
        _uiState.update {
            it.copy(
                options = mockList,
                currentOptionIndex = it.currentOptionIndex + 1
            )
        }
    }
}

data class LearnCountUIState(
    val title: String = "",
    val options: List<CountOption> = emptyList(),
    val currentOptionIndex: Int = 0,
)

data class CountOption(
    val count: Int = -1,
    @DrawableRes val imgResId: Int = -1,
)