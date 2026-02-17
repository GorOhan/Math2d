package com.ohanyan.mathgame.onboarding.menu

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ohanyan.common.utils.Utils.openInternetSettingsSafely
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.onboarding.R
import com.ohanyan.ui.component.corebutton.CoreButton
import com.ohanyan.ui.component.mainhero.MainHero
import com.ohanyan.ui.component.mathaction.MathLoading
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
internal fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onLearnNumber: () -> Unit = {},
    onCountClick: () -> Unit = {},
    onAdditionClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (!uiState.isOnline) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = stringResource(R.string.feature_onboarding_no_internet_connection_title)) },
            text = { Text(text = stringResource(R.string.feature_onboarding_no_internet_connection_text)) },
            confirmButton = {
                TextButton(onClick = {
                    context.openInternetSettingsSafely()
                }) {
                    Text(stringResource(R.string.feature_onboarding_ok))
                }
            }
        )
    }

    MenuScreenUI(
        onLearnNumber = onLearnNumber,
        onCountClick = onCountClick,
        onAdditionClick = onAdditionClick,
        onSettingsClick = onSettingsClick,
    )
}

@Composable
private fun MenuScreenUI(
    onLearnNumber: () -> Unit = {},
    onCountClick: () -> Unit = {},
    onAdditionClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MathAppTheme.colors.coreBlue.copy(0.4f),
                        MathAppTheme.colors.darkPurpleGray90.copy(0.1f)
                    )
                )
            )
    ) {
        ActionButton(
            modifier = Modifier.align(Alignment.TopEnd),
            actionType = ActionType.SETTINGS,
            onClick = onSettingsClick
        )

        var offsetXSpace by remember { mutableStateOf(0.dp) }

        LaunchedEffect(Unit) {
            offsetXSpace += 1200.dp
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoreButton(
                buttonTitle = stringResource(id = R.string.feature_onboarding_learn_numbers),
                onClick = onLearnNumber
            )

//            CoreButton(
//                buttonTitle = stringResource(id = R.string.feature_onboarding_counting),
//                subtitle = stringResource(id = R.string.feature_onboarding_soon),
//                enable = false,
//                onClick = onCountClick
//            )
//
            CoreButton(
                buttonTitle = stringResource(id = R.string.feature_onboarding_adding),
                subtitle = stringResource(id = R.string.feature_onboarding_soon),
                enable = true,
                onClick = onAdditionClick
            )
//
//            CoreButton(
//                buttonTitle = stringResource(id = R.string.feature_onboarding_subtracting),
//                subtitle = stringResource(id = R.string.feature_onboarding_soon),
//                enable = false,
//                onClick = { }
//            )

            MathLoading(
                durationOfEachAnimation = 200
            )
        }

        val configuration = LocalConfiguration.current
        val fractionWidth = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            .5f
        } else 1f

        val fractionHeight = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            1f
        } else .5f


        Box(
            modifier = Modifier
                .fillMaxWidth(fractionWidth)
                .fillMaxHeight(fractionHeight)
        ) {
            MainHero(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(112.dp)
            )

        }
    }
}

@Composable
@MathPreview
fun MenuScreenPreview() {
    MenuScreenUI()
}