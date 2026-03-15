package com.ohanyan.mathgame.playground.subtraction

import com.ohanyan.mathgame.common.data.UserPreferencesRepository

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
class SubtractionViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubtractionUIState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<SubtractionEffect>()
    val effects = _effects.receiveAsFlow()
    private var firstQuestion = true

    init {
        handleIntent(SubtractionIntent.GenerateQuestion)
        viewModelScope.launch {
            userPreferencesRepository.musicOn.collect { isMusicOn ->
                _uiState.update { state ->
                    state.copy(isMusicOn = isMusicOn)
                }
            }
        }
    }

    fun handleIntent(intent: SubtractionIntent) {
        when (intent) {
            is SubtractionIntent.GenerateQuestion -> generateNewQuestion()
            is SubtractionIntent.AnswerSelected -> checkAnswer(intent.answer)
            is SubtractionIntent.ToggleMusic -> onMusicOnChange(intent.isChecked)
        }
    }

    private fun generateNewQuestion() {
        // Ensure minuend > subtrahend so result is always positive
        val num1 = Random.nextInt(1, 10)
        val num2 = Random.nextInt(0, num1)

        val images = listOf(
            com.ohanyan.mathgame.ui.R.drawable.ic_apple,
            com.ohanyan.mathgame.ui.R.drawable.icecream2,
            com.ohanyan.mathgame.ui.R.drawable.fox,
            com.ohanyan.mathgame.ui.R.drawable.giraff,
            com.ohanyan.mathgame.ui.R.drawable.chipmunk
        )
        val randomImage = images.random()

        val difference = num1 - num2
        val wrong1 = (difference - Random.nextInt(1, 3)).coerceAtLeast(0)
        val wrong2 = difference + Random.nextInt(1, 3)
        val options = listOf(difference, wrong1, wrong2).shuffled()

        _uiState.update {
            it.copy(
                firstNumber = num1,
                secondNumber = num2,
                userAnswer = "",
                isAnswerCorrect = null,
                imgResId = randomImage,
                options = options,
                selectedAnswer = null,
                hintOption = if (firstQuestion) difference else null
            )
        }
        firstQuestion = false
    }

    private fun checkAnswer(answer: Int) {
        val difference = uiState.value.firstNumber - uiState.value.secondNumber

        _uiState.update { it.copy(selectedAnswer = answer) }

        if (answer == difference) {
            _uiState.update { it.copy(isAnswerCorrect = true) }
            sendEffect(SubtractionEffect.ShowCorrectFeedback)
            viewModelScope.launch {
                delay(1500)
                handleIntent(SubtractionIntent.GenerateQuestion)
            }
        } else {
            _uiState.update { it.copy(isAnswerCorrect = false) }
            sendEffect(SubtractionEffect.ShowWrongFeedback)
            viewModelScope.launch {
                delay(4000)
                _uiState.update {
                    it.copy(
                        isAnswerCorrect = null,
                        selectedAnswer = null,
                        options = it.options.filter { it != answer }
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: SubtractionEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    private fun onMusicOnChange(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateMusicOn(isChecked)
        }
    }
}

// Intent - User actions
sealed interface SubtractionIntent {
    data object GenerateQuestion : SubtractionIntent
    data class AnswerSelected(val answer: Int) : SubtractionIntent
    data class ToggleMusic(val isChecked: Boolean) : SubtractionIntent
}

// Effect - One-time side effects
sealed interface SubtractionEffect {
    data object ShowCorrectFeedback : SubtractionEffect
    data object ShowWrongFeedback : SubtractionEffect
}

// State - UI state
data class SubtractionUIState(
    val firstNumber: Int = 0,
    val secondNumber: Int = 0,
    val userAnswer: String = "",
    val isAnswerCorrect: Boolean? = false,
    val imgResId: Int = com.ohanyan.mathgame.ui.R.drawable.ic_apple,
    val isMusicOn: Boolean = true,
    val options: List<Int> = emptyList(),
    val selectedAnswer: Int? = null,
    val hintOption: Int? = null
)
