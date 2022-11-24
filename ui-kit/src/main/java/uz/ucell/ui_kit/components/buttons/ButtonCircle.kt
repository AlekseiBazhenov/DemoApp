package uz.ucell.ui_kit.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.progress.UcellLoader
import uz.ucell.ui_kit.drawings.ButtonDefaults
import uz.ucell.ui_kit.drawings.ButtonDrawing

@Composable
fun ButtonCircle(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = ButtonSize.MEDIUM,
    buttonTheme: ButtonTheme = ButtonTheme.PURPLE,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    imagePainter: Painter,
    onClick: () -> Unit,
) {
    if (enabled) {
        ActiveCircleButton(
            modifier = modifier,
            buttonSize = buttonSize,
            buttonTheme = buttonTheme,
            isLoading = isLoading,
            onClick = onClick,
            imagePainter = imagePainter,
        )
    } else {
        DisabledCircleButton(
            modifier = modifier,
            buttonSize = buttonSize,
            imagePainter = imagePainter,
        )
    }
}

@Composable
private fun ActiveCircleButton(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize,
    buttonTheme: ButtonTheme,
    isLoading: Boolean = false,
    imagePainter: Painter,
    onClick: () -> Unit
) {
    val dimensions = StandardCircleButtonDimensions()
    val colors = StandardPrimaryButtonColors()
    ButtonDrawing(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.background(buttonTheme = buttonTheme),
        ),
        loading = isLoading,
        shape = CircleShape,
        modifier = Modifier
            .then(modifier)
            .then(dimensions.sizeModifier(buttonSize))
    ) {
        when {
            isLoading -> {
                UcellLoader()
            }
            else -> Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(ICON_SIZE),
                    painter = imagePainter,
                    tint = colors.content(buttonTheme = buttonTheme),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun DisabledCircleButton(
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize,
    imagePainter: Painter,
) {
    val dimensions = StandardCircleButtonDimensions()
    ButtonDrawing(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.sky1),
            contentColor = colorResource(R.color.white),
            disabledBackgroundColor = colorResource(R.color.sky1),
            disabledContentColor = colorResource(R.color.white)
        ),
        shape = CircleShape,
        enabled = false,
        modifier = Modifier
            .then(modifier)
            .then(dimensions.sizeModifier(buttonSize))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(ICON_SIZE),
                painter = imagePainter,
                tint = colorResource(R.color.sky3),
                contentDescription = null
            )
        }
    }
}

private val ICON_SIZE = 24.dp
