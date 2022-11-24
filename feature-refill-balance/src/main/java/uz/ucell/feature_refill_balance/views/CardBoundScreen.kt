package uz.ucell.feature_refill_balance.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.DialogImages
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary

@Composable
fun CardBoundScreen(
    modifier: Modifier = Modifier,
    cardNumberLastDigits: String,
    onContinueClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .systemBarsPadding()
            .padding(top = 64.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogImages.Success(modifier = Modifier.align(Alignment.CenterHorizontally))
            Texts.H3(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(R.string.card_bound_title),
                textAlign = TextAlign.Center,
                color = colorResource(R.color.fresh_turquoise)
            )
            Texts.ParagraphText(
                modifier = Modifier.padding(top = 8.dp),
                title = stringResource(R.string.card_bound_description, cardNumberLastDigits),
                textAlign = TextAlign.Center,
                wide = true
            )
        }
        ButtonPrimary(
            modifier = Modifier
                .padding(bottom = 28.dp)
                .align(Alignment.BottomCenter),
            text = stringResource(R.string.card_bound_button_continue),
            isFullWidth = false,
            onClick = { onContinueClicked() }
        )
    }
}

@Preview
@Composable
private fun CardBoundScreenPreview() {
    CardBoundScreen(cardNumberLastDigits = "1234") {}
}
