package uz.ucell.ui_kit.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.ucell.ui_kit.R

val book = Font(R.font.pragmatica_book, FontWeight.W400)
val medium = Font(R.font.pragmatica_medium, FontWeight.W500)

private val fonts = FontFamily(book, medium)

val primaryText = Color(0xFF333333)
val secondaryText = Color(0xFF999999)
val accentText = Color(0xFF7220A3)

// TODO когда появятся в дизайне поставить корректные параметры
//  для h5, h6, body2, overline
val typography = Typography(
    h1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 30.sp,
        color = primaryText
    ),
    h2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 22.sp,
        color = primaryText
    ),
    h3 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        color = primaryText
    ),
    h4 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 15.sp,
        color = primaryText
    ),
    h5 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold
    ),
    h6 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        lineHeight = 28.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        color = primaryText
    ),
    subtitle2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        color = secondaryText
    ),
    body1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 15.sp,
        color = primaryText
    ),
    body2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    ),
    button = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 15.sp,
    ),
    caption = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        color = secondaryText
    ),
    overline = TextStyle(
        letterSpacing = 0.08.em
    )
)
