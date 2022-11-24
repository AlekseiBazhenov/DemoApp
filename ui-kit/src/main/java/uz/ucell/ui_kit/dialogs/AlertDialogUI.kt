package uz.ucell.ui_kit.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import uz.ucell.ui_kit.components.Texts

@Composable
fun AlertDialogUI(
    showDialog: Boolean,
    title: String,
    text: String?,
    primaryButton: String?,
    secondaryButton: String?,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    if (!showDialog) {
        return
    }

    AlertDialog(
        onDismissRequest = {
            onDismissClick()
        },
        title = { Texts.H3(title = title) },
        text = { text?.let { Texts.ParagraphText(title = it) } },
        confirmButton = {
            primaryButton?.let {
                TextButton(onClick = { onPrimaryClick() }) {
                    Text(
                        text = it.uppercase(),
                    )
                }
            }
        },
        dismissButton = {
            secondaryButton?.let {
                TextButton(onClick = { onSecondaryClick() }) {
                    Text(
                        text = it.uppercase(),
                    )
                }
            }
        }
    )
}
