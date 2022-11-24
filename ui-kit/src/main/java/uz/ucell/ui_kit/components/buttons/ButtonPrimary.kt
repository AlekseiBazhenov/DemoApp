package uz.ucell.ui_kit.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.progress.UcellLoader
import uz.ucell.ui_kit.drawings.ButtonDefaults
import uz.ucell.ui_kit.drawings.ButtonDrawing

@Composable
fun ButtonPrimary(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = ButtonSize.MEDIUM,
    buttonTheme: ButtonTheme = ButtonTheme.PURPLE,
    isFullWidth: Boolean = true,
    withArrow: Boolean = false,
    text: String,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    leftImagePainter: Painter? = null,
    onClick: () -> Unit,
) {
    if (enabled) {
        ActivePrimaryButton(
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
        DisabledPrimaryButton(
            modifier = modifier,
            buttonSize = buttonSize,
            isFullWidth = isFullWidth,
            withArrow = withArrow,
            text = text,
            leftImagePainter = leftImagePainter,
        )
    }
}

@Composable
private fun ActivePrimaryButton(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize,
    buttonTheme: ButtonTheme,
    isFullWidth: Boolean,
    withArrow: Boolean,
    text: String,
    isLoading: Boolean = false,
    leftImagePainter: Painter? = null,
    onClick: () -> Unit
) {
    val dimensions = StandardButtonDimensions()
    val colors = StandardPrimaryButtonColors()
    ButtonDrawing(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.background(buttonTheme = buttonTheme),
        ),
        loading = isLoading,
        shape = RoundedCornerShape(dimensions.cornerRadius(buttonSize)),
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
                modifier = Modifier.padding(horizontal = dimensions.horizontalPadding(buttonSize)),
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
                Texts.H4(
                    title = text,
                    color = colors.content(buttonTheme = buttonTheme)
                )
                if (withArrow) {
                    Icon(
                        modifier = Modifier.padding(start = 14.dp),
                        painter = painterResource(R.drawable.ic_arrow_right_14),
                        tint = colors.content(buttonTheme = buttonTheme),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun DisabledPrimaryButton(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize,
    isFullWidth: Boolean,
    withArrow: Boolean,
    text: String = "",
    leftImagePainter: Painter? = null,
) {
    val dimensions = StandardButtonDimensions()
    ButtonDrawing(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.sky1),
            contentColor = colorResource(R.color.white),
            disabledBackgroundColor = colorResource(R.color.sky1),
            disabledContentColor = colorResource(R.color.white)
        ),
        shape = RoundedCornerShape(dimensions.cornerRadius(buttonSize)),
        enabled = false,
        modifier = Modifier
            .then(modifier)
            .then(dimensions.heightModifier(buttonSize))
            .then(dimensions.widthModifier(isFullWidth, buttonSize))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = dimensions.horizontalPadding(buttonSize)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftImagePainter?.let {
                Icon(
                    modifier = Modifier.padding(end = 6.dp),
                    painter = it,
                    tint = colorResource(R.color.sky3),
                    contentDescription = null
                )
            }
            Texts.H4(
                title = text,
                color = colorResource(R.color.sky3)
            )
        }
    }
}

@Preview
@Composable
fun LargePrimaryPurpleButtonPreview() {
    ButtonPrimary(
        buttonSize = ButtonSize.LARGE,
        buttonTheme = ButtonTheme.PURPLE,
        text = "Button"
    ) {}
}

@Preview
@Composable
fun MediumPrimaryGreenButtonPreview() {
    ButtonPrimary(
        buttonSize = ButtonSize.MEDIUM,
        buttonTheme = ButtonTheme.GREEN,
        text = "Button"
    ) {}
}

@Preview
@Composable
fun MediumPrimarySoftPurpleButtonPreview() {
    ButtonPrimary(
        buttonSize = ButtonSize.MEDIUM,
        buttonTheme = ButtonTheme.SOFT_PURPLE,
        text = "Button"
    ) {}
}

@Preview
@Composable
fun SmallPrimarySoftGreenButtonPreview() {
    ButtonPrimary(
        buttonSize = ButtonSize.SMALL,
        buttonTheme = ButtonTheme.SOFT_GREEN,
        text = "Button"
    ) {}
}

@Preview
@Composable
fun ExtraSmallPrimaryWhiteButtonPreview() {
    ButtonPrimary(
        buttonSize = ButtonSize.EXTRA_SMALL,
        buttonTheme = ButtonTheme.WHITE,
        text = "Button"
    ) {}
}
