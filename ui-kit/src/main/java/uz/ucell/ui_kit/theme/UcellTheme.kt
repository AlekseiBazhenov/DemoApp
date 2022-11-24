package uz.ucell.ui_kit.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun UcellTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = typography
    ) {
        content()
    }
}
