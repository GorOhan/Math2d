package com.ohanyan.mathgame.settings.settings

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ohanyan.common.languagemanager.LocaleHelper
import com.ohanyan.common.utils.Utils
import com.ohanyan.mathgame.designsystem.preview.MathPreview
import com.ohanyan.mathgame.designsystem.theme.MathAppTheme
import com.ohanyan.mathgame.settings.R
import com.ohanyan.mathgame.settings.settings.model.AppLanguage
import com.ohanyan.ui.component.chalkoard.ChalkBoard
import com.ohanyan.ui.component.mathaction.MathLoading
import com.ohanyan.ui.component.nextbutton.ActionButton
import com.ohanyan.ui.component.nextbutton.ActionType

@Composable
internal fun SettingsScreen(
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
    val configuration = LocalConfiguration.current
    val isHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
            actionType = ActionType.PREVIOUS,
            onClick = onBackClick
        )

        MathLoading(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(if (isHorizontal) 0.dp else 48.dp),
            durationOfEachAnimation = 200,
        )

        ChalkBoard(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
        ) {
            MainContent(
                modifier = Modifier
                    .align(Alignment.Center),
                settingScreenUIState = settingScreenUIState,
                onMusicOnChane = onMusicOnChane,
                onSelectLanguageClick = onSelectLanguageClick
            )
        }

    }
}

@Composable
fun MainContent(
    modifier: Modifier,
    settingScreenUIState: SettingScreenUIState,
    onMusicOnChane: (Boolean) -> Unit,
    onSelectLanguageClick: () -> Unit,
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 24.dp),
                text = stringResource(id = R.string.feature_settings_music_on),
                style = MathAppTheme.typography.h1Bee,
                color = MathAppTheme.colors.coreWhite
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

//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                modifier = Modifier.padding(end = 24.dp),
//                text = stringResource(id = R.string.feature_settings_select_language),
//                style = MathAppTheme.typography.h1Bee,
//                color = MathAppTheme.colors.coreWhite
//            )
//
//            Image(
//                modifier = Modifier
//                    .size(42.dp)
//                    .clip(RoundedCornerShape(80f))
//                    .clickable {
//                        onSelectLanguageClick()
//                    },
//                painter = painterResource(settingScreenUIState.selectedLanguage.languageFlag),
//                contentDescription = "select language icon"
//            )
//        }

        val title = stringResource(id = R.string.feature_settings_checkout_app)

        Row(
            modifier = Modifier.clickable {
                Utils.shareApp(
                    context = context,
                    title = title
                )
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 24.dp),
                text = stringResource(id = R.string.feature_settings_share_app),
                style = MathAppTheme.typography.h1Bee,
                color = MathAppTheme.colors.coreWhite
            )

            Image(
                modifier = Modifier.size(42.dp),
                painter = painterResource(id = com.ohanyan.mathgame.ui.R.drawable.ic_share),
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
            AppLanguage.entries.forEach {
                Image(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(16.dp))
                        .size(48.dp)
                        .border(
                            border = BorderStroke(
                                if (uiState.selectedLanguage.languageCode == it.languageCode) 4.dp else 0.dp,
                                color = MathAppTheme.colors.coreYellow,
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onLanguageClick(it) },
                    contentDescription = it.languageCode,
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(it.languageFlag)
                )
            }
        }
    }
}

@Composable
@MathPreview
fun SettingsScreenPreview() {
    SettingsScreenUI()
}