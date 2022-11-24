package uz.ucell.ui_kit.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.DialogImages
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonClose
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.components.buttons.ButtonText
import uz.ucell.ui_kit.theme.UcellTheme

@Preview(locale = "ru", name = "day ru", showBackground = true)
@Composable
fun PreviewDialog() {
    UcellTheme {
        PopupDialog(
            showDialog = true,
            title = "Some title",
            message = "some message for preview",
            code = "ad404",
            primaryButton = "Some primary button",
            secondaryButton = "Some secondary button",
            dialogType = DialogType.ERROR,
            onPrimaryClick = {},
            onSecondaryClick = {}
        )
    }
}
@Composable
fun PopupDialog(
    showDialog: Boolean,
    title: String?,
    message: String?,
    code: String?,
    primaryButton: String?,
    secondaryButton: String?,
    dialogType: DialogType,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
) {
    if (!showDialog) {
        return
    }

    Dialog(
        onDismissRequest = {
            onSecondaryClick()
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = colorResource(R.color.white)
        ) {
            Box {
                ButtonClose(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    onSecondaryClick()
                }

                Column(
                    modifier = Modifier
                        .padding(
                            top = 40.dp,
                            bottom = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val imageModifier = Modifier.align(Alignment.CenterHorizontally)
                    when (dialogType) {
                        DialogType.ERROR -> DialogImages.Error(modifier = imageModifier)
                        else -> {}
                    }

                    title?.let {
                        Texts.H3(
                            modifier = Modifier.padding(top = 8.dp),
                            title = it,
                            textAlign = TextAlign.Center
                        )
                    }

                    message?.let {
                        Texts.ParagraphText(
                            modifier = Modifier.padding(top = 8.dp),
                            title = it,
                            textAlign = TextAlign.Center,
                            color = colorResource(R.color.sky3)
                        )
                    }

                    if (!code.isNullOrEmpty()) {
                        Texts.ParagraphText(
                            modifier = Modifier.padding(top = 8.dp),
                            title = stringResource(R.string.error_dialog_code, code),
                            textAlign = TextAlign.Center,
                            color = colorResource(R.color.sky3)
                        )
                    }

                    primaryButton?.let {
                        ButtonPrimary(
                            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                            text = it,
                            onClick = { onPrimaryClick() }
                        )
                    }

                    secondaryButton?.let {
                        ButtonText(
                            text = it,
                            onClick = { onSecondaryClick() }
                        )
                    }
                }
            }
        }
    }
}

enum class DialogType {
    ERROR, WARNING, SUCCESS
}
