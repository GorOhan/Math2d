package com.ohanyan.mathgame.playground.addition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AdditionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AdditionUIState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<AdditionEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        handleIntent(AdditionIntent.GenerateQuestion)
    }

    fun handleIntent(intent: AdditionIntent) {
        when (intent) {
            is AdditionIntent.GenerateQuestion -> generateNewQuestion()
            is AdditionIntent.AnswerSelected -> checkAnswer(intent.answer)
        }
    }

    private fun generateNewQuestion() {
        val num1 = Random.nextInt(1, 10)
        val num2 = Random.nextInt(1, 10)
        
        // Random image for visual representation
        val images = listOf(
            com.ohanyan.mathgame.ui.R.drawable.ic_apple,
            com.ohanyan.mathgame.ui.R.drawable.icecream2,
            com.ohanyan.mathgame.ui.R.drawable.fox,
            com.ohanyan.mathgame.ui.R.drawable.giraff,
            com.ohanyan.mathgame.ui.R.drawable.chipmunk
        )
        val randomImage = images.random()
        
        _uiState.update {
            it.copy(
                firstNumber = num1,
                secondNumber = num2,
                userAnswer = "",
                isAnswerCorrect = null,
                imgResId = randomImage
            )
        }
    }

    private fun checkAnswer(answer: String) {
        val sum = uiState.value.firstNumber + uiState.value.secondNumber
        val userAnswerInt = answer.toIntOrNull()

        if (userAnswerInt != null && userAnswerInt == sum) {
            _uiState.update { it.copy(isAnswerCorrect = true) }
            sendEffect(AdditionEffect.ShowCorrectFeedback)
            viewModelScope.launch {
                delay(1500)
                handleIntent(AdditionIntent.GenerateQuestion)
            }
        } else {
            _uiState.update { it.copy(isAnswerCorrect = false) }
            sendEffect(AdditionEffect.ShowWrongFeedback)
            viewModelScope.launch {
                delay(1000)
                _uiState.update { it.copy(isAnswerCorrect = null) }
            }
        }
    }

    private fun sendEffect(effect: AdditionEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}

// Intent - User actions
sealed interface AdditionIntent {
    data object GenerateQuestion : AdditionIntent
    data class AnswerSelected(val answer: String) : AdditionIntent
}

// Effect - One-time side effects
sealed interface AdditionEffect {
    data object ShowCorrectFeedback : AdditionEffect
    data object ShowWrongFeedback : AdditionEffect
}

// State - UI state
data class AdditionUIState(
    val firstNumber: Int = 0,
    val secondNumber: Int = 0,
    val userAnswer: String = "",
    val isAnswerCorrect: Boolean? = null,
    val imgResId: Int = com.ohanyan.mathgame.ui.R.drawable.ic_apple
)
