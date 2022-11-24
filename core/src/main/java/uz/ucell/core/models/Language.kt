package uz.ucell.core.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Language(
    @DrawableRes val flag: Int,
    @DrawableRes val flagSmall: Int,
    @StringRes val title: Int,
    val code: String,
    val enabled: Boolean,
)
