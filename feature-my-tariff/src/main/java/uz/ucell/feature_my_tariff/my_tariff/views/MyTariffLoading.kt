package uz.ucell.feature_my_tariff.my_tariff.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.divider.SmallDivider
import uz.ucell.ui_kit.extentions.addPlaceholder
import uz.ucell.ui_kit.theme.UcellTheme

@Composable
fun MyTariffLoading() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Surface(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
                .height(152.dp),
            color = colorResource(R.color.sky0),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = 24.dp,
                        horizontal = 16.dp
                    ),
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(217.dp)
                        .height(24.dp)
                        .addPlaceholder()
                )
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .width(188.dp)
                        .height(12.dp)
                        .addPlaceholder()
                )
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(169.dp)
                        .height(12.dp)
                        .addPlaceholder()
                )
                Spacer(
                    modifier = Modifier
                        .width(155.dp)
                        .height(24.dp)
                        .addPlaceholder()
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .addPlaceholder()
            )

            Spacer(
                modifier = Modifier
                    .width(155.dp)
                    .height(24.dp)
                    .addPlaceholder()
            )
        }

        repeat(4) {
            SmallDivider(modifier = Modifier.padding(vertical = 16.dp))
            TariffItemPlaceholder()
        }
    }
}

@Composable
private fun TariffItemPlaceholder() {
    Row {
        Column {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .width(120.dp)
                    .height(14.dp)
                    .addPlaceholder()
            )
            Spacer(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(165.dp)
                    .height(14.dp)
                    .addPlaceholder()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 14.dp)
                    .width(62.dp)
                    .height(8.dp)
                    .addPlaceholder()
            )
            Spacer(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(136.dp)
                    .height(14.dp)
                    .addPlaceholder()
            )
        }
    }
}

@Preview(name = "my tariff loading day", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MyTariffLoadingPreview() {
    UcellTheme {
        MyTariffLoading()
    }
}
