package com.ohanyan.mathgame.onboarding.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ohanyan.mathgame.onboarding.selectage.SelectAgeScreen
import com.ohanyan.mathgame.onboarding.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface OnBoardingScreen {
    @Serializable
    data object SplashScreen

    @Serializable
    data object SelectAgeScreen
}

fun NavController.navigateToSelectAgeScreen() =
    navigate(route = OnBoardingScreen.SelectAgeScreen, null)

fun NavGraphBuilder.onboardingScreens(
    onAnimationEnd: () -> Unit,
) {

    composable<OnBoardingScreen.SplashScreen> {
        SplashScreen(
            onAnimationEnd = onAnimationEnd
        )
    }

    composable<OnBoardingScreen.SelectAgeScreen>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(600)
            )
        },
    ) {
        SelectAgeScreen()
    }
}
