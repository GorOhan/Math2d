package com.ohanyan.mathgame.settings.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.ui.component.mathaction.MathLoading
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()

    SettingsScreenUI(
        settingScreenUIState = uiState,
        onMusicOnChane = viewModel::onMusicOnChange,
        onBackClick = onBackClick
    )
}

@Composable
fun SettingsScreenUI(
    settingScreenUIState: SettingScreenUIState = SettingScreenUIState(),
    onMusicOnChane: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {}
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
            modifier = Modifier.align(Alignment.TopStart),
            visible = true,
            actionType = ActionType.PREVIOUS,
            onClick = onBackClick
        )

        MathLoading(
            modifier = Modifier.align(Alignment.TopCenter),
            durationOfEachAnimation = 200,
        )

        MainContent(
            modifier = Modifier.align(Alignment.Center),
            settingScreenUIState = settingScreenUIState,
            onMusicOnChane = onMusicOnChane
        )

    }
}

@Composable
@MathPreview
fun SettingsScreenPreview() {
    SettingsScreenUI()
}

@Composable
fun MainContent(
    modifier: Modifier,
    settingScreenUIState: SettingScreenUIState,
    onMusicOnChane: (Boolean) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 24.dp),
                text = "Music On",
                style = MathAppTheme.typography.h1Bee,
                color = MathAppTheme.colors.red
            )
            Switch(
                enabled = true,
                checked = settingScreenUIState.isMusicPlaying,
                onCheckedChange = onMusicOnChane,
                colors = SwitchDefaults.colors().copy(
                    checkedIconColor = MathAppTheme.colors.coreYellow,
                    checkedThumbColor = MathAppTheme.colors.coreBlue,
                    checkedTrackColor = MathAppTheme.colors.coreYellow
                )
            )
        }
    }
}