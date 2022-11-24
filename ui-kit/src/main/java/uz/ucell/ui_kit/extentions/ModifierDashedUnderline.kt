package uz.ucell.ui_kit.extentions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import uz.ucell.ui_kit.theme.primaryText

fun Modifier.drawDashedUnderline(layout: TextLayoutResult?) = drawBehind {
    layout?.let {
        val thickness = 2f
        val dashPath = 4f
        val spacingExtra = 1f
        val offsetY = 8f

        for (i in 0 until it.lineCount) {
            drawPath(
                path = Path().apply {
                    moveTo(
                        it.getLineLeft(i),
                        it.getLineBottom(i) - spacingExtra + offsetY
                    )
                    lineTo(
                        it.getLineRight(i),
                        it.getLineBottom(i) - spacingExtra + offsetY
                    )
                },
                color = primaryText,
                style = Stroke(
                    width = thickness,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(
                            dashPath,
                            dashPath
                        ),
                        0f
                    )
                )
            )
        }
    }
}
