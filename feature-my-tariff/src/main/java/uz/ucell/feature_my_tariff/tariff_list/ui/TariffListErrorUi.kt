package uz.ucell.feature_my_tariff.tariff_list.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uz.ucell.core.constatns.RU
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.common_ui.ServerErrorView
import uz.ucell.ui_kit.components.buttons.ButtonTheme
import uz.ucell.ui_kit.theme.UcellTheme

@Composable
fun TariffListError(
    onButtonClick: () -> Unit
) {
    ServerErrorView(
        title = stringResource(id = R.string.common_error),
        titleImage = painterResource(id = R.drawable.ic_magnifier),
        titleImageColor = colorResource(id = R.color.sky2),
        subtitle = stringResource(id = R.string.tariff_list_error_description),
        buttonTitle = stringResource(id = R.string.button_return_to_ratiffs),
        buttonTheme = ButtonTheme.PURPLE,
        onButtonClick = onButtonClick
    )
}

@Preview(
    name = "server error preview day",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = RU
)
@Composable
fun ServerErrorScreenPreview() {
    UcellTheme {
        TariffListError(
            onButtonClick = { }
        )
    }
}
