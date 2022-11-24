package uz.ucell.ui_kit.extentions

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer
import uz.ucell.ui_kit.R

fun Modifier.addPlaceholder() = composed {
    placeholder(
        visible = true,
        color = colorResource(R.color.sky1),
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = colorResource(R.color.sky0),
        )
    )
}
