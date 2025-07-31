package com.ohanyan.mathgame.settings.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ohanyan.common.languagemanager.LocaleHelper
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.settings.R
import com.ohanyan.mathgame.settings.settings.model.AppLanguage
import com.ohanyan.ui.component.mathaction.MathLoading
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    var showSelectLanguage by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    SettingsScreenUI(
        settingScreenUIState = uiState,
        onMusicOnChane = viewModel::onMusicOnChange,
        onBackClick = onBackClick,
        onSelectLanguageClick = {
            showSelectLanguage = true
        }
    )

    if (showSelectLanguage) {
        SelectLanguageAlert(
            uiState = uiState,
            onDismissRequest = {
                showSelectLanguage = false
            },
            onLanguageClick = {
                LocaleHelper.setAppLanguage(context, it.languageCode)
                viewModel.selectLanguage(it)
                showSelectLanguage = false
            }
        )
    }
}

@Composable
fun SettingsScreenUI(
    settingScreenUIState: SettingScreenUIState = SettingScreenUIState(),
    onMusicOnChane: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSelectLanguageClick: () -> Unit = {},
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
            onMusicOnChane = onMusicOnChane,
            onSelectLanguageClick = onSelectLanguageClick
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
    onSelectLanguageClick: () -> Unit,

    ) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 24.dp),
                text = stringResource(id = R.string.feature_settings_music_on),
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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 24.dp),
                text = stringResource(id = R.string.feature_settings_select_language),
                style = MathAppTheme.typography.h1Bee,
                color = MathAppTheme.colors.red
            )

            Image(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(80f))
                    .clickable {
                        onSelectLanguageClick()
                    },
                painter = painterResource(settingScreenUIState.selectedLanguage.languageFlag),
                contentDescription = "select language icon"
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLanguageAlert(
    uiState: SettingScreenUIState,
    onDismissRequest: () -> Unit = {},
    onLanguageClick: (AppLanguage) -> Unit = {}
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(.5f)
                .clip(
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    border = BorderStroke(
                        2.dp, color = MathAppTheme.colors.blue30,
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MathAppTheme.colors.coreBlue.copy(0.91f),
                            MathAppTheme.colors.coreBlue.copy(1f)
                        )
                    )
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .size(48.dp)
                    .border(
                        border = BorderStroke(
                            if (uiState.selectedLanguage.languageCode == "en") 4.dp else 0.dp,
                            color = MathAppTheme.colors.coreYellow,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onLanguageClick(AppLanguage.EN) },
                contentDescription = "en",
                contentScale = ContentScale.FillBounds,
                painter = painterResource(com.ohanyan.mathgame.ui.R.drawable.flag_gb)
            )
            Image(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .size(48.dp)
                    .border(
                        border = BorderStroke(
                            if (uiState.selectedLanguage.languageCode == "ru") 4.dp else 0.dp,
                            color = MathAppTheme.colors.coreYellow,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onLanguageClick(AppLanguage.RU) },
                contentScale = ContentScale.FillBounds,
                contentDescription = "ru",
                painter = painterResource(com.ohanyan.mathgame.ui.R.drawable.flag_ru)
            )
            Image(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .size(48.dp)
                    .border(
                        border = BorderStroke(
                            if (uiState.selectedLanguage.languageCode == "hy") 4.dp else 0.dp,
                            color = MathAppTheme.colors.coreYellow,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onLanguageClick(AppLanguage.HY) },
                contentScale = ContentScale.FillBounds,
                contentDescription = "eng",
                painter = painterResource(com.ohanyan.mathgame.ui.R.drawable.flag_am)
            )
        }
    }
}
