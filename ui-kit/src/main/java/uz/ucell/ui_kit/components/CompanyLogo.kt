package uz.ucell.ui_kit.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import uz.ucell.ui_kit.R

@Composable
fun UcellLogo(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.logo_ucell),
        contentDescription = ""
    )
}
