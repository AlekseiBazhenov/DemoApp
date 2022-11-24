package uz.ucell.feature_my_tariff.my_tariff.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.divider.BlockDivider
import uz.ucell.ui_kit.extentions.clickableWithoutIndication
import uz.ucell.ui_kit.extentions.ucellShadow

@Composable
fun ButtonsBlock(
    onChangeTariffClick: () -> Unit
) {
    BlockDivider()
    Column(
        modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ActionButton(
            stringResource(R.string.my_tariff_button_change),
            R.drawable.ic_my_tariff_change
        ) {
            onChangeTariffClick()
        }
        ActionButton(
            stringResource(R.string.my_tariff_button_available_services),
            R.drawable.ic_my_tariff_services
        ) {
            // TODO
        }
        ActionButton(
            stringResource(R.string.my_tariff_button_order_details),
            R.drawable.ic_my_tariff_order_details
        ) {
            // TODO
        }
    }
}

@Composable
fun ActionButton(title: String, image: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutIndication { onClick() }
            .ucellShadow(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(end = 12.dp),
                painter = painterResource(image),
                contentDescription = null
            )
            Texts.H4(modifier = Modifier.weight(1f), title = title)
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right_14),
                tint = colorResource(R.color.sky2),
                contentDescription = ""
            )
        }
    }
}
