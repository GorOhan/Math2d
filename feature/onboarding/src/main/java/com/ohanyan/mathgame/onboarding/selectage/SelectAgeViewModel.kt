package com.ohanyan.mathgame.onboarding.selectage

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SelectAgeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseAgeUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                ageOptions = listOf(
                    AgeItem("2", Random.nextInt(0, 66), AgeColor.RED),
                    AgeItem("3", Random.nextInt(44, 88), AgeColor.BLUE),
                    AgeItem("4", Random.nextInt(44, 88), AgeColor.YELLOW),
                    AgeItem("5", Random.nextInt(0, 24), AgeColor.GREEN),
                    AgeItem("6", Random.nextInt(0, 34), AgeColor.BLUE),
                    AgeItem("7", Random.nextInt(0, 44), AgeColor.RED)

                )
            )
        }
    }

    fun selectAge(age: String) {
        _uiState.update {
            if (age == it.selectedAge) it.copy(selectedAge = "")
            else it.copy(selectedAge = age)
        }
    }
}

@Immutable
data class ChooseAgeUIState(
    val selectedAge: String = "",
    val ageOptions: List<AgeItem> = listOf(),
)

data class AgeItem(
    val age: String,
    val topPadding: Int,
    val color: AgeColor,
)

enum class AgeColor {
    RED, BLUE, GREEN, YELLOW
}
