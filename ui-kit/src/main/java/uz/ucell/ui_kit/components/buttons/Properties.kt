package uz.ucell.ui_kit.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

enum class ButtonSize {
    LARGE, MEDIUM, SMALL, EXTRA_SMALL
}

enum class ButtonTheme {
    GREEN, SOFT_GREEN, PURPLE, SOFT_PURPLE, WHITE, BLACK
}

interface PrimaryButtonColors {
    @Composable
    fun background(buttonTheme: ButtonTheme): Color

    @Composable
    fun content(buttonTheme: ButtonTheme): Color
}

interface TextButtonColors {
    @Composable
    fun background(): Color

    @Composable
    fun content(buttonTheme: ButtonTheme): Color
}

class StandardPrimaryButtonColors : PrimaryButtonColors {

    @Composable
    override fun background(buttonTheme: ButtonTheme): Color {
        val colorId = when (buttonTheme) {
            ButtonTheme.GREEN -> R.color.fresh_turquoise
            ButtonTheme.SOFT_GREEN -> R.color.soft_green
            ButtonTheme.PURPLE -> R.color.rich_purple
            ButtonTheme.SOFT_PURPLE -> R.color.soft_purple
            ButtonTheme.WHITE -> R.color.white
            else -> R.color.rich_purple
        }
        return colorResource(colorId)
    }

    @Composable
    override fun content(buttonTheme: ButtonTheme): Color {
        val colorId = when (buttonTheme) {
            ButtonTheme.GREEN, ButtonTheme.PURPLE -> R.color.white
            ButtonTheme.SOFT_GREEN -> R.color.fresh_turquoise
            ButtonTheme.SOFT_PURPLE -> R.color.rich_purple
            ButtonTheme.WHITE -> R.color.base_black
            else -> R.color.white
        }
        return colorResource(colorId)
    }
}

class StandardTextButtonColors : TextButtonColors {

    @Composable
    override fun background() = Color.Transparent

    @Composable
    override fun content(buttonTheme: ButtonTheme): Color {
        val colorId = when (buttonTheme) {
            ButtonTheme.GREEN -> R.color.fresh_turquoise
            ButtonTheme.PURPLE -> R.color.rich_purple
            ButtonTheme.WHITE -> R.color.white
            ButtonTheme.BLACK -> R.color.base_black
            else -> R.color.rich_purple
        }
        return colorResource(colorId)
    }
}

interface ButtonDimensions {
    @Composable
    fun heightModifier(buttonSize: ButtonSize): Modifier

    @Composable
    fun widthModifier(isFullWidth: Boolean, buttonSize: ButtonSize): Modifier

    fun cornerRadius(buttonSize: ButtonSize): Dp

    fun horizontalPadding(buttonSize: ButtonSize): Dp
}

class StandardButtonDimensions : ButtonDimensions {
    @Composable
    override fun heightModifier(buttonSize: ButtonSize): Modifier {
        return Modifier
            .height(
                when (buttonSize) {
                    ButtonSize.LARGE -> 60.dp
                    ButtonSize.MEDIUM -> 52.dp
                    ButtonSize.SMALL -> 40.dp
                    ButtonSize.EXTRA_SMALL -> 24.dp
                }
            )
    }

    @Composable
    override fun widthModifier(isFullWidth: Boolean, buttonSize: ButtonSize): Modifier =
        if (isFullWidth)
            Modifier.fillMaxWidth()
        else
            Modifier.wrapContentWidth()

    override fun cornerRadius(buttonSize: ButtonSize): Dp = when (buttonSize) {
        ButtonSize.LARGE, ButtonSize.MEDIUM -> 12.dp
        ButtonSize.SMALL -> 8.dp
        ButtonSize.EXTRA_SMALL -> 4.dp
    }

    override fun horizontalPadding(buttonSize: ButtonSize): Dp = when (buttonSize) {
        ButtonSize.LARGE, ButtonSize.MEDIUM -> 32.dp
        ButtonSize.SMALL -> 24.dp
        ButtonSize.EXTRA_SMALL -> 16.dp
    }
}

interface CircleButtonDimensions {
    @Composable
    fun sizeModifier(buttonSize: ButtonSize): Modifier
}

class StandardCircleButtonDimensions : CircleButtonDimensions {
    @Composable
    override fun sizeModifier(buttonSize: ButtonSize): Modifier {
        return Modifier.size(
            when (buttonSize) {
                ButtonSize.LARGE -> 60.dp
                ButtonSize.MEDIUM -> 52.dp
                ButtonSize.SMALL -> 40.dp
                ButtonSize.EXTRA_SMALL -> 24.dp
            }
        )
    }
}
