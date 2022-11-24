package uz.ucell.ui_kit.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

fun ComposeView.setUcellContent(content: @Composable () -> Unit) =
    setContent {
        UcellTheme {
            content()
        }
    }
