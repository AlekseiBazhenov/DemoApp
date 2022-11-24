package uz.ucell.ui_kit.extentions

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.ucellShadow(
    elevation: Dp = 12.dp,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) = then(
    Modifier.shadow(
        elevation = elevation,
        shape = shape,
        spotColor = Color.Black.copy(alpha = 0.08f)
    )
)
