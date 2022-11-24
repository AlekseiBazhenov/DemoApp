package uz.ucell.ui_kit.common_ui

import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.components.buttons.ButtonSize
import uz.ucell.ui_kit.components.buttons.ButtonTheme
import uz.ucell.ui_kit.theme.UcellTheme

@Composable
fun ServerErrorView(
    title: String,
    titleImage: Painter,
    @ColorRes titleImageColor: Color,
    subtitle: String,
    buttonTitle: String,
    buttonTheme: ButtonTheme,
    buttonImage: Painter? = null,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 54.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(bottom = 28.dp),
            painter = titleImage,
            contentDescription = null,
            colorFilter = ColorFilter.tint(titleImageColor)
        )
        Texts.H3(
            modifier = Modifier.padding(bottom = 12.dp),
            title = title
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(
                start = 8.5.dp,
                end = 8.5.dp,
                bottom = 16.dp
            ),
            title = subtitle,
            color = colorResource(R.color.sky3),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        ButtonPrimary(
            buttonSize = ButtonSize.SMALL,
            buttonTheme = buttonTheme,
            text = buttonTitle,
            isFullWidth = false,
            leftImagePainter = buttonImage,
            onClick = onButtonClick
        )
    }
}

@Preview(
    name = "server error preview day",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru"
)
@Composable
fun ServerErrorScreenPreview() {
    UcellTheme {
        ServerErrorView(
            title = stringResource(R.string.common_error),
            titleImage = painterResource(R.drawable.ic_magnifier),
            titleImageColor = colorResource(id = R.color.sky2),
            subtitle = stringResource(R.string.common_error_description),
            buttonTitle = stringResource(R.string.button_refresh),
            buttonTheme = ButtonTheme.SOFT_PURPLE,
            buttonImage = painterResource(R.drawable.ic_refresh_16),
            onButtonClick = { }
        )
    }
}
