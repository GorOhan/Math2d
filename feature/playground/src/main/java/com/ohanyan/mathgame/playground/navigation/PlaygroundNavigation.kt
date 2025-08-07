package com.ohanyan.mathgame.playground.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ohanyan.mathgame.playground.counting.LearnCountScreen
import com.ohanyan.mathgame.playground.writing.LearnNumbersScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlaygroundScreen {
    @Serializable
    data object LearnCountScreen : PlaygroundScreen

    @Serializable
    data object LearnNumberScreen : PlaygroundScreen

}

fun NavGraphBuilder.playgroundScreens(
    onNavigation: (PlaygroundScreen) -> Unit,
    onBackClick: () -> Unit,
) {

    composable<PlaygroundScreen.LearnCountScreen> {
        LearnCountScreen(
            onBackClick = onBackClick,
        )
    }

    composable<PlaygroundScreen.LearnNumberScreen> {
        LearnNumbersScreen(
            onBackClick = onBackClick,
        )
    }
}

fun NavController.navigateToPlaygroundScreen(screen: PlaygroundScreen) =
    navigate(route = screen, null)
