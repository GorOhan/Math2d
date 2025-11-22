package com.ohanyan.mathgame.onboarding.selectage

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SelectAgeViewModelTest {

    private lateinit var viewModel: SelectAgeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SelectAgeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialStateHasAgeOptions() = runTest {
        val state = viewModel.uiState.value
        assertEquals("", state.selectedAge)
        assertTrue(state.ageOptions.isNotEmpty())
        assertEquals(6, state.ageOptions.size)
    }

    @Test
    fun selectAgeUpdatesSelectedAge() {
        viewModel.selectAge("5")
        assertEquals("5", viewModel.uiState.value.selectedAge)
    }

    @Test
    fun selectAgeTogglesSelectedAgeIfSameAgeSelected() {
        viewModel.selectAge("5")
        assertEquals("5", viewModel.uiState.value.selectedAge)
        
        viewModel.selectAge("5")
        assertEquals("", viewModel.uiState.value.selectedAge)
    }

    @Test
    fun selectAgeChangesSelectedAgeIfDifferentAgeSelected() {
        viewModel.selectAge("5")
        assertEquals("5", viewModel.uiState.value.selectedAge)
        
        viewModel.selectAge("6")
        assertEquals("6", viewModel.uiState.value.selectedAge)
    }
}
