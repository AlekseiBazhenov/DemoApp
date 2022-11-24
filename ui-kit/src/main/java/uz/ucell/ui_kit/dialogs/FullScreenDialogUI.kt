package uz.ucell.ui_kit.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.DialogImages
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.theme.UcellTheme

@Preview(name = "day error", showBackground = true, locale = "ru")
@Composable
fun FullScreenErrorPreview() {
    UcellTheme {
        FullScreenDialogUI(
            isError = true,
            title = "Some test title",
            message = "Some test message",
            primaryButton = "Button",
            onButtonClick = {}
        )
    }
}

@Preview(name = "day warning", showBackground = true, locale = "ru")
@Composable
fun FullScreenWarningPreview() {
    UcellTheme {
        FullScreenDialogUI(
            isError = false,
            title = "Some test title",
            message = "Some test message",
            primaryButton = "Button",
            onButtonClick = {}
        )
    }
}

@Composable
fun FullScreenDialogUI(
    isError: Boolean,
    title: String?,
    message: String?,
    primaryButton: String?,
    onButtonClick: () -> Unit,
) {
    val gradientBackground = Brush.verticalGradient(
        0f to if (isError) colorResource(R.color.error) else colorResource(R.color.warning),
        0.3f to colorResource(R.color.white),
        tileMode = TileMode.Decal
    )

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .background(gradientBackground)
            .padding(all = 24.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(top = 144.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isError) {
                DialogImages.Error(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                DialogImages.Warning(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            title?.let {
                Texts.H3(
                    modifier = Modifier.padding(top = 16.dp),
                    title = it,
                    textAlign = TextAlign.Center,
                    color = if (isError)
                        colorResource(R.color.fury)
                    else
                        colorResource(R.color.warning)
                )
            }

            message?.let {
                Texts.ParagraphText(
                    modifier = Modifier.padding(top = 8.dp),
                    title = it,
                    textAlign = TextAlign.Center
                )
            }
        }

        primaryButton?.let {
            ButtonPrimary(
                modifier = Modifier
                    .padding(bottom = 28.dp)
                    .align(Alignment.BottomCenter),
                text = it,
                isFullWidth = false,
                onClick = { onButtonClick() }
            )
        }
    }
}
