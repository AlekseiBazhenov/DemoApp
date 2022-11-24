package uz.ucell.core.constatns

import uz.ucell.core.R
import uz.ucell.core.models.Language

const val DEFAULT_LANGUAGE = "uz"

const val UZ = "uz"
const val RU = "ru"
const val EN = "en"

val allLanguages = mapOf(
    Pair(
        UZ,
        Language(R.drawable.flag_uz, R.drawable.flag_uz_small, R.string.language_uz, UZ, true)
    ),
    Pair(
        RU,
        Language(R.drawable.flag_ru, R.drawable.flag_ru_small, R.string.language_ru, RU, true)
    ),
    Pair(
        EN,
        Language(R.drawable.flag_ru, R.drawable.flag_ru_small, R.string.language_en, EN, false)
    )
)
