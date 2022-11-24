package uz.ucell.ui_kit.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CodeInput(
    modifier: Modifier = Modifier,
    input: String,
    inputEnabled: Boolean = true,
    codeLength: Int,
    isError: Boolean = false,
    hint: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChanged: (String) -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        val focusRequester = remember { FocusRequester() }
        val softwareKeyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val clickableModifier = remember(inputEnabled) {
            if (inputEnabled) {
                Modifier.clickableWithoutIndication {
                    focusRequester.requestFocus()
                    softwareKeyboardController?.show()
                }
            } else {
                Modifier
            }
        }

        LaunchedEffect(inputEnabled) {
            if (inputEnabled) {
                focusRequester.requestFocus()
            } else {
                focusManager.clearFocus()
                softwareKeyboardController?.hide()
            }
        }
        DisposableEffect(true) {
            onDispose {
                focusManager.clearFocus()
                softwareKeyboardController?.hide()
            }
        }

        BasicTextField(
            value = input,
            onValueChange = { newValue ->
                val code = newValue.filter { it.isLetter().or(it.isDigit()) }.take(codeLength)
                if (code != input) {
                    onValueChanged(code)
                }
            },
            textStyle = TextStyle(color = Color.Transparent),
            cursorBrush = SolidColor(Color.Transparent),
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            enabled = inputEnabled
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                modifier = clickableModifier
            ) {
                for (i in 0 until codeLength) {
                    if (inputEnabled) {
                        CodeCell(
                            value = input.getOrNull(i),
                            isFocused = i == input.length,
                            isError = isError
                        )
                    } else {
                        DisabledCodeCell()
                    }
                }
            }
            if (isError) {
                Texts.Caption(
                    modifier = Modifier.padding(top = 8.dp),
                    title = hint,
                    color = colorResource(R.color.fury),
                )
            }
        }
    }
}

@Composable
private fun CodeCell(
    value: Char?,
    isFocused: Boolean,
    isError: Boolean
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isError)
                colorResource(R.color.fury)
            else if (isFocused)
                colorResource(R.color.base_black)
            else
                colorResource(R.color.sky2)
        ),
        backgroundColor = Color.White,
        modifier = Modifier.requiredSize(width = 40.dp, height = 44.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (value != null) {
                Texts.ParagraphText(title = value.toString())
            }
        }
    }
}

@Composable
private fun DisabledCodeCell() {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = colorResource(R.color.sky1),
        modifier = Modifier.requiredSize(width = 40.dp, height = 44.dp)
    ) {}
}
