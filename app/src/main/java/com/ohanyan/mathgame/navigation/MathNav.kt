package com.ohanyan.mathgame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ohanyan.mathgame.onboarding.navigation.OnBoardingScreen
import com.ohanyan.mathgame.onboarding.navigation.navigateToScreen
import com.ohanyan.mathgame.onboarding.navigation.onboardingScreens
import com.ohanyan.mathgame.playground.navigation.PlaygroundScreen
import com.ohanyan.mathgame.playground.navigation.navigateToPlaygroundScreen
import com.ohanyan.mathgame.playground.navigation.playgroundScreens
import com.ohanyan.mathgame.settings.navigation.SettingsScreen
import com.ohanyan.mathgame.settings.navigation.navigateToSettingsScreen
import com.ohanyan.mathgame.settings.navigation.settingsScreens


@Composable
fun MathNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OnBoardingScreen.SplashScreen,
        modifier = modifier,
    ) {
        onboardingScreens(
            onNavigation = { navController.navigateToScreen(it) },
            onCountClick = { navController.navigateToPlaygroundScreen(PlaygroundScreen.LearnCountScreen) },
            onBackClick = { navController.popBackStack() },
            onLearnNumber = { navController.navigateToPlaygroundScreen(PlaygroundScreen.LearnNumberScreen) },
            onAdditionClick = { navController.navigateToPlaygroundScreen(PlaygroundScreen.AdditionScreen) },
            onSettingsClick = { navController.navigateToSettingsScreen(SettingsScreen.SettingsMain)}
        )

        playgroundScreens(
            onNavigation = { navController.navigateToPlaygroundScreen(it) },
            onBackClick = { navController.popBackStack() }
        )

        settingsScreens(
            onNavigation = { navController.navigateToSettingsScreen(it) },
            onBackClick = { navController.popBackStack() },
            onSelectAgeClick = { navController.navigateToScreen(OnBoardingScreen.SelectAgeScreen) }
        )
    }
}
