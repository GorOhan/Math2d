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
            onLearnNumber = { navController.navigateToPlaygroundScreen(PlaygroundScreen.LearnNumberScreen) }
        )

        playgroundScreens(
            onNavigation = { navController.navigateToPlaygroundScreen(it) },
            onBackClick = { navController.popBackStack() }
        )
    }
}
