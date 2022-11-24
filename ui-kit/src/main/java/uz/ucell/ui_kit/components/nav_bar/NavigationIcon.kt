package uz.ucell.ui_kit.components.nav_bar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import uz.ucell.ui_kit.R

@Composable
fun NavigationIcon(
    navigationAction: NavigationAction
): @Composable (() -> Unit)? = when (navigationAction) {
    is NavigationAction.Back -> {
        {
            IconButton(
                onClick = navigationAction.onBackClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = colorResource(R.color.base_black)
                )
            }
        }
    }
    is NavigationAction.Close -> {
        {
            IconButton(
                onClick = navigationAction.onCloseClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left), // TODO поменять когда будет в дизайне
                    contentDescription = null,
                    tint = colorResource(R.color.base_black)
                )
            }
        }
    }
    NavigationAction.Empty -> null
}
