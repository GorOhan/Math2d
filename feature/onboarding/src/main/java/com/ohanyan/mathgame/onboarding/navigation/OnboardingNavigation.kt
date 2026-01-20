package com.ohanyan.mathgame.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ohanyan.mathgame.onboarding.menu.MenuScreen
import com.ohanyan.mathgame.onboarding.selectage.SelectAgeScreen
import com.ohanyan.mathgame.onboarding.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface OnBoardingScreen {
    @Serializable
    data object SplashScreen : OnBoardingScreen

    @Serializable
    data object SelectAgeScreen : OnBoardingScreen

    @Serializable
    data object MenuScreen : OnBoardingScreen
}

fun NavGraphBuilder.onboardingScreens(
    onNavigation: (OnBoardingScreen) -> Unit,
    onLearnNumber: () -> Unit,
    onCountClick:()-> Unit,
    onAdditionClick: () -> Unit,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {

    composable<OnBoardingScreen.SplashScreen> {
        SplashScreen(
            onAnimationEnd = { onNavigation(it) },
        )
    }

    composable<OnBoardingScreen.SelectAgeScreen> {
        SelectAgeScreen(
            onNextClick = { onNavigation(OnBoardingScreen.MenuScreen) }
        )
    }

    composable<OnBoardingScreen.MenuScreen> {
        MenuScreen(
            onLearnNumber = onLearnNumber ,
            onCountClick = onCountClick,
            onAdditionClick = onAdditionClick,
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick,
        )
    }
}

fun NavController.navigateToScreen(screen: OnBoardingScreen) =
    navigate(route = screen, null)
