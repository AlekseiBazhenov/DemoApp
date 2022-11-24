package uz.ucell.appmetrica.model

enum class UserType(val type: String) {
    PHYSICAL(PHYSICAL_STRING_VALUE),
    JURIDICAL(JURIDICAL_STRING_VALUE),
    NONE(NONE_STRING_VALUE)
}

private const val PHYSICAL_STRING_VALUE: String = "b2c"
private const val JURIDICAL_STRING_VALUE: String = "b2b"
private const val NONE_STRING_VALUE: String = "none"
