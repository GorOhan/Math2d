package com.ohanyan.mathgame.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ohanyan.mathgame.settings.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingsScreen {
    @Serializable
    data object SettingsMain : SettingsScreen

}

fun NavGraphBuilder.settingsScreens(
    onNavigation: (SettingsScreen) -> Unit,
    onBackClick: () -> Unit,
) {

    composable<SettingsScreen.SettingsMain> {
        SettingsScreen()
    }

}

fun NavController.navigateToScreen(screen: SettingsScreen) =
    navigate(route = screen, null)