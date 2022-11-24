package uz.ucell.ui_kit.components

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

object DialogImages {

    @Composable
    fun Error(modifier: Modifier) {
        DialogImage(
            modifier = modifier,
            icon = R.drawable.ic_error,
            spotColor = R.color.fury_50
        )
    }

    @Composable
    fun Warning(modifier: Modifier) {
        DialogImage(
            modifier = modifier,
            icon = R.drawable.ic_warning,
            spotColor = R.color.warning_50
        )
    }

    @Composable
    fun Success(modifier: Modifier) {
        DialogImage(
            modifier = modifier,
            icon = R.drawable.ic_success,
            spotColor = R.color.fresh_turquoise_50
        )
    }

    @Composable
    private fun DialogImage(modifier: Modifier, @DrawableRes icon: Int, @ColorRes spotColor: Int) {
        Box(
            modifier = modifier
                .size(100.dp)
                .shadow(
                    elevation = 4.dp, shape = CircleShape,
                    ambientColor = colorResource(R.color.white),
                    spotColor = colorResource(spotColor)
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.None
                )
            }
        }
    }
}
