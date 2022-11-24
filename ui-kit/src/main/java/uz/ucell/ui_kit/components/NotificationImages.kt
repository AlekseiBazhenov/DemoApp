package uz.ucell.ui_kit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

object NotificationImages {

    @Composable
    fun Warning(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = colorResource(R.color.soft_137c),
                shape = CircleShape,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = "",
                    contentScale = ContentScale.None
                )
            }
        }
    }
}
