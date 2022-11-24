package uz.ucell.feature_my_tariff.tariff_list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.feature_my_tariff.tariff_list.model.TariffLimit
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts

@Composable
fun LimitCell(
    cell: TariffLimit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(40.dp)
                .background(
                    color = colorResource(R.color.sky0),
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(cell.icon),
                contentDescription = null
            )
        }

        Texts.H2(title = cell.limitStringValue)
    }
}
