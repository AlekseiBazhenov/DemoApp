package uz.ucell.feature_my_tariff.tariff_list.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.extentions.addPlaceholder
import uz.ucell.ui_kit.theme.UcellTheme

@Composable
fun TariffLoading() {
    // TODO: вызов repeat(2) выводит только 1 placeholder
    Column {
        TariffListLoadingPlaceholder(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    bottom = 12.dp
                )
                .fillMaxWidth()
                .wrapContentHeight()
        )
        TariffListLoadingPlaceholder(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    bottom = 12.dp
                )
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
fun TariffListLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp),
        color = colorResource(R.color.sky0),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(24.dp)
                    .addPlaceholder()
            )

            repeat(3) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(40.dp)
                            .height(40.dp)
                            .addPlaceholder()
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                top = 16.dp
                            )
                            .width(123.dp)
                            .height(24.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .addPlaceholder()
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .width(165.dp)
                    .height(14.dp)
                    .addPlaceholder()
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .width(123.dp)
                    .height(24.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .addPlaceholder()
            )
            Spacer(
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        start = 17.dp,
                        end = 17.dp
                    )
                    .fillMaxWidth()
                    .height(40.dp)
                    .addPlaceholder()
            )
        }
    }
}

@Preview(
    name = "my tariff loading day",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 900
)
@Composable
fun MyTariffLoadingPreview() {
    UcellTheme {
        TariffLoading()
    }
}
