package uz.ucell.ui_kit.components.inputs

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import uz.ucell.ui_kit.R
import uz.ucell.utils.PHONE_NUMBER_LENGTH
import uz.ucell.utils.PhoneFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.phone_number),
    phoneInput: String,
    isTextInputEnabled: Boolean = true,
    needRequestFocus: Boolean = true,
    isError: Boolean = false,
    hint: String = "",
    onPhoneValueChanged: (newTextFiltered: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(needRequestFocus && isTextInputEnabled) {
        if (needRequestFocus && isTextInputEnabled) {
            try {
                focusRequester.requestFocus()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        } else {
            focusManager.clearFocus()
            softwareKeyboardController?.hide()
        }
    }

    TextInputs.SingleLineTextInput(
        modifier = modifier
            .onFocusChanged {
                if (!it.hasFocus) {
                    softwareKeyboardController?.hide()
                }
            },
        title = title,
        value = phoneInput,
        onValueChange = { newText ->
            if (newText.length <= PHONE_NUMBER_LENGTH) {
                val newTextFiltered = newText.filter { it.isDigit() }.take(PHONE_NUMBER_LENGTH)
                onPhoneValueChanged(newTextFiltered)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        focusRequester = focusRequester,
        visualTransformation = remember { PhoneNumberTransformation() },
        enabled = isTextInputEnabled,
        isError = isError,
        hint = hint
    )
}

/**
 * Visual transformation for mask `+998 ## ### ## ##`
 */
private class PhoneNumberTransformation : VisualTransformation {
    private val offsetTranslator = OffsetTranslator()

    override fun filter(text: AnnotatedString): TransformedText {
        val outText = PhoneFormatter.formatPhone(text.text)
        return TransformedText(AnnotatedString(outText), offsetTranslator)
    }

    private inner class OffsetTranslator : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                in 0..2 -> offset + 5
                in 3..5 -> offset + 6
                in 6..7 -> offset + 7
                else -> offset + 8
            }

        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                in 0..4 -> 0
                in 5..6 -> offset - 5
                in 7..10 -> offset - 6
                in 11..13 -> offset - 7
                else -> offset - 8
            }
    }
}
