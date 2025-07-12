package com.ohanyan.mathgame.designsystem.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "armenian",
    showBackground = true,
    locale = "hy",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)

@Preview(
    name = "english",
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
annotation class MathPreview