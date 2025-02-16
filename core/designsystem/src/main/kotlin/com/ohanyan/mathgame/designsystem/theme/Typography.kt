package com.ohanyan.mathgame.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography(
    /*Main - M2*/
    val display: TextStyle,
    val h1: TextStyle,
    val h2: TextStyle,
    val h1Bee: TextStyle,
//    val h2: TextStyle,
//    val h3: TextStyle,
//    val h4: TextStyle,
//    val h4Large: TextStyle,
//    val h5: TextStyle,
//    val h6Bold: TextStyle,
//    val h6: TextStyle,
//    val btn1: TextStyle,
//    val btn2: TextStyle,
//    val caption: TextStyle,
//    val captionBold: TextStyle,
//    val subtitle1: TextStyle,
//    val subtitle2: TextStyle,
//    val subtitle3: TextStyle,
//    val subtitle4: TextStyle,
//    val subtitle5: TextStyle,
//    val body1: TextStyle,
//    val body2: TextStyle,
//    val body3: TextStyle,
//    val num1: TextStyle,
//    val num2: TextStyle,
//    val num3: TextStyle,
) {

    fun toMaterialTypography() = Typography(
        displayLarge = display,
        displayMedium = h1
    )
}

internal val defaultTypography = AppTypography(
    display = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Normal,
        lineHeight = 72.sp,
        fontSize = 44.sp,
    ),
    h1 = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Bold,
        lineHeight = 48.sp,
        fontSize = 40.sp,
    ),
    h2 = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    h1Bee = TextStyle(
        fontFamily = BeeFont,
        fontWeight = FontWeight.Bold,
        lineHeight = 48.sp,
        fontSize = 24.sp,
    ),
)