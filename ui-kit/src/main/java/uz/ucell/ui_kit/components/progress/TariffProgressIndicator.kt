package uz.ucell.ui_kit.components.progress

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

@Composable
fun TariffProgressIndicator(
    modifier: Modifier = Modifier,
    availableValue: Float,
    totalValue: Float,
    isEnabled: Boolean,
    backgroundColor: Color = colorResource(R.color.sky0)
) {
    val indicatorProgress = availableValue / totalValue
    val progress = if (indicatorProgress > 1f)
        1f
    else if (indicatorProgress > 0 && indicatorProgress < 0.01f)
        0.01f
    else
        indicatorProgress

    val fillColor = when (progress) {
        in 0f..0.1f -> {
            colorResource(R.color.warm_red)
        }
        in 0.11f..0.5f -> {
            colorResource(R.color.warning)
        }
        in 0.51f..1f -> {
            colorResource(if (isEnabled) R.color.fresh_turquoise else R.color.sky3)
        }
        else -> {
            colorResource(R.color.sky3)
        }
    }
    val linearIndicatorHeight = 12.dp

    var progressState by remember { mutableStateOf(0f) }
    val progressAnimDuration = 1000
    val progressAnimation by animateFloatAsState(
        targetValue = progressState,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
    )

    Canvas(
        modifier
            .progressSemantics(progressState)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(linearIndicatorHeight)
            .focusable()
    ) {
        val strokeWidth = size.height
        drawRoundedLinearIndicatorBackground(backgroundColor, strokeWidth)
        if (availableValue > 0) {
            drawRoundedLinearIndicator(0f, progressAnimation, fillColor, strokeWidth)
        }
    }

    LaunchedEffect(indicatorProgress) {
        progressState = progress
    }
}

private fun DrawScope.drawRoundedLinearIndicatorBackground(
    color: Color,
    strokeWidth: Float
) = drawRoundedLinearIndicator(0f, 1f, color, strokeWidth)

private fun DrawScope.drawRoundedLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    drawLine(
        color,
        Offset(barStart, yOffset),
        Offset(barEnd, yOffset),
        strokeWidth,
        StrokeCap.Round
    )
}
