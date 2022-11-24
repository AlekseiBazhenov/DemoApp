package uz.ucell.ui_kit.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.progress.UcellLoader
import uz.ucell.ui_kit.drawings.ButtonDefaults
import uz.ucell.ui_kit.drawings.ButtonDrawing

@Composable
fun ButtonText(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = ButtonSize.MEDIUM,
    buttonTheme: ButtonTheme = ButtonTheme.PURPLE,
    isFullWidth: Boolean = true,
    withArrow: Boolean = false,
    text: String = "",
    isLoading: Boolean = false,
    enabled: Boolean = true,
    leftImagePainter: Painter? = null,
    onClick: () -> Unit,
) {
    if (enabled) {
        ActiveTextButton(
            modifier = modifier,
            buttonSize = buttonSize,
            buttonTheme = buttonTheme,
            isFullWidth = isFullWidth,
            withArrow = withArrow,
            text = text,
            isLoading = isLoading,
            onClick = onClick,
            leftImagePainter = leftImagePainter,
        )
    } else {
        // todo draw DisabledTextButton
    }
}

@Composable
private fun ActiveTextButton(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize,
    buttonTheme: ButtonTheme,
    isFullWidth: Boolean,
    withArrow: Boolean,
    text: String = "",
    isLoading: Boolean = false,
    leftImagePainter: Painter? = null,
    onClick: () -> Unit
) {
    val dimensions = StandardButtonDimensions()
    val colors = StandardTextButtonColors()
    ButtonDrawing(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = colors.background()),
        loading = isLoading,
        modifier = Modifier
            .then(modifier)
            .then(dimensions.heightModifier(buttonSize))
            .then(dimensions.widthModifier(isFullWidth, buttonSize))
    ) {
        when {
            isLoading -> {
                UcellLoader()
            }
            else -> Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                leftImagePainter?.let {
                    Icon(
                        modifier = Modifier.padding(end = 6.dp),
                        painter = it,
                        tint = colors.content(buttonTheme = buttonTheme),
                        contentDescription = null
                    )
                }
                if (buttonSize == ButtonSize.EXTRA_SMALL) {
                    Texts.CaptionMedium(
                        title = text,
                        color = colors.content(buttonTheme = buttonTheme)
                    )
                } else {
                    Texts.H4(
                        title = text,
                        color = colors.content(buttonTheme = buttonTheme)
                    )
                }
                if (withArrow) {
                    Icon(
                        modifier = Modifier
                            .padding(
                                start = if (buttonSize == ButtonSize.EXTRA_SMALL) 10.dp else 14.dp
                            )
                            .size(if (buttonSize == ButtonSize.EXTRA_SMALL) 12.dp else 14.dp),
                        painter = painterResource(R.drawable.ic_arrow_right_14),
                        tint = colors.content(buttonTheme = buttonTheme),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
