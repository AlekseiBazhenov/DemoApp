package uz.ucell.ui_kit.components.badges

import androidx.annotation.ColorInt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts

@Composable
fun BadgePromo(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = color,
                RoundedCornerShape(8.dp)
            )
    ) {
        Texts.CaptionMedium(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            title = text,
            color = colorResource(R.color.white)
        )
    }
}

@Composable
fun BadgePromo(
    text: String,
    @ColorInt badgeBackground: Int,
    modifier: Modifier = Modifier,
) {
    BadgePromo(
        text = text,
        color = Color(badgeBackground),
        modifier = modifier
    )
}

@Composable
fun BadgePromo(
    text: String,
    badgeStyle: BadgeStyle
) {
    BadgePromo(
        text = text,
        color = colorResource(id = badgeStyle.background)
    )
}

enum class BadgeStyle(
    val background: Int
) {
    GREEN(R.color.fresh_turquoise),
    ORANGE(R.color.light_secondary_137c),
    PURPLE(R.color.rich_purple),
    MINT(R.color.secondary_311c)
}
