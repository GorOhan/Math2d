package com.ohanyan.mathgame.playground.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ohanyan.mathgame.playground.count.LearnCountScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlaygroundScreen {
    @Serializable
    data object LearnCountScreen : PlaygroundScreen

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
}

fun NavController.navigateToPlaygroundScreen(screen: PlaygroundScreen) =
    navigate(route = screen, null)
