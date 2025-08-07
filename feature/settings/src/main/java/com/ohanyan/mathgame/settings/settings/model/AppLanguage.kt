package com.ohanyan.mathgame.settings.settings.model

enum class AppLanguage(
    val languageCode: String,
    val languageFlag: Int
) {
    EN("en", com.ohanyan.mathgame.ui.R.drawable.flag_gb),
    HY("hy",com.ohanyan.mathgame.ui.R.drawable.flag_am),
    RU("ru",com.ohanyan.mathgame.ui.R.drawable.flag_ru)
}