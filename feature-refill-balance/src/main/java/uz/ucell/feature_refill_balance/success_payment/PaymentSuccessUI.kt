package uz.ucell.feature_refill_balance.success_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.DialogImages
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.components.buttons.ButtonText
import uz.ucell.ui_kit.components.expandable.ExpandableItem

@Composable
fun PaymentSuccessUI(
    receiptId: String,
    amount: String,
    commission: String,
    payDate: String,
    cardInfo: String,
    account: String,
    onButtonClick: () -> Unit,
) {
    val gradientBackground = Brush.verticalGradient(
        0f to colorResource(R.color.success),
        0.3f to colorResource(R.color.white),
        tileMode = TileMode.Decal
    )

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .background(gradientBackground)
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogImages.Success(modifier = Modifier.align(Alignment.CenterHorizontally))
            Texts.H3(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(R.string.payment_success_title),
                textAlign = TextAlign.Center,
                color = colorResource(R.color.fresh_turquoise)
            )
            Texts.ParagraphText(
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                title = stringResource(R.string.payment_success_description, account),
                textAlign = TextAlign.Center,
                wide = true
            )

            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .weight(1f)
            ) {
                ExpandableItem(
                    title = stringResource(R.string.details),
                    content = {
                        ReceiptDetails(receiptId, amount, commission, payDate, cardInfo, account)
                    }
                )
            }

            ButtonPrimary(
                modifier = Modifier.padding(bottom = 12.dp),
                text = stringResource(R.string.back_to_main_screen),
                onClick = { onButtonClick() }
            )

            ButtonText(
                text = stringResource(R.string.get_check),
                onClick = {
                    // TODO Будет запрашиваться ссылка на офд чек (будет реализована в будущем)
                }
            )
        }
    }
}

@Composable
private fun ReceiptDetails(
    receiptId: String,
    amount: String,
    commission: String,
    payDate: String,
    cardInfo: String,
    account: String,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ReceiptDetailsRow(
            stringResource(R.string.payment_success_status),
            stringResource(R.string.done)
        )
        ReceiptDetailsRow(stringResource(R.string.payment_success_date_time), payDate)
        ReceiptDetailsRow(stringResource(R.string.payment_success_from), cardInfo)
        ReceiptDetailsRow(stringResource(R.string.payment_success_to), account)
        ReceiptDetailsRow(stringResource(R.string.amount), amount)
        ReceiptDetailsRow(stringResource(R.string.commission), commission)
        ReceiptDetailsRow(stringResource(R.string.payment_success_receipt_number), receiptId)
    }
}

@Composable
private fun ReceiptDetailsRow(header: String, text: String) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Texts.ParagraphText(
            title = header,
            color = colorResource(R.color.sky3)
        )
        Texts.ParagraphText(title = text)
    }
}
