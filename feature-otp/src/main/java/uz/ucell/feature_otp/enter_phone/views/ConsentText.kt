package uz.ucell.feature_otp.enter_phone.views

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import uz.ucell.feature_otp.R
import uz.ucell.feature_otp.enter_phone.contract.EnterPhoneState
import uz.ucell.ui_kit.theme.book

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ConsentText(
    modifier: Modifier = Modifier,
    viewState: EnterPhoneState,
    onOfferClick: (String) -> Unit
) {
    val text = getConsentAnnotatedText()
    val keyboardController = LocalSoftwareKeyboardController.current

    ClickableText(
        modifier = modifier,
        text = text,
        onClick = { offset ->
            if (viewState.isLoading) return@ClickableText

            text.getStringAnnotations(
                tag = OFFER_URL_TAG,
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                keyboardController?.hide()
                onOfferClick(it.item)
            }
        }
    )
}

@Composable
private fun getConsentAnnotatedText() = buildAnnotatedString {
    val spanText = stringResource(R.string.auth_enter_phone_number_offer)
    val text = stringResource(R.string.auth_enter_phone_number_offer_text)
    val textParts = text.split(spanText)

    pushStyle(
        SpanStyle(
            fontFamily = book.toFontFamily(),
            fontSize = 13.sp,
            letterSpacing = 0.sp,
            color = colorResource(R.color.sky3)
        )
    )
    pushStyle(ParagraphStyle(lineHeight = 18.sp))

    append(textParts.first())
    pushStringAnnotation(
        tag = OFFER_URL_TAG,
        annotation = OFFER_URL
    )
    withStyle(SpanStyle(color = colorResource(R.color.rich_purple))) {
        append(spanText)
    }
    pop()

    if (textParts.size == 2) {
        append(textParts.last())
    }
}

private const val OFFER_URL_TAG = "offer_url"
private const val OFFER_URL = "https://" // TODO
