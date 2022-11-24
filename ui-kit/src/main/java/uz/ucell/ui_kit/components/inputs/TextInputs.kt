package uz.ucell.ui_kit.components.inputs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonClose
import uz.ucell.ui_kit.components.buttons.ButtonStyle

object TextInputs {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun SingleLineTextInput(
        modifier: Modifier = Modifier,
        style: TextFieldStyle = TextFieldStyle.DEFAULT,
        value: String = "",
        title: String = "",
        hint: String = "",
        imagePainter: Painter? = null,
        placeholder: String = "",
        onValueChange: (String) -> Unit = {},
        focusedState: MutableState<Boolean> = remember { mutableStateOf(false) },
        isObligatorily: Boolean = false,
        isError: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        enabled: Boolean = true,
        focusRequester: FocusRequester = remember { FocusRequester() },
        visualTransformation: VisualTransformation = VisualTransformation.None
    ) {
        val hintColor = if (isError) {
            colorResource(R.color.fury)
        } else {
            colorResource(R.color.sky3)
        }

        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()

        TextInput(
            style = style,
            isShowHint = hint.isNotEmpty(),
            modifier = modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            value = value,
            title = title,
            hint = hint,
            imagePainter = imagePainter,
            placeholder = placeholder,
            onValueChange = onValueChange,
            focusedState = focusedState,
            isObligatorily = isObligatorily,
            isError = isError,
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            hintColor = hintColor,
            enabled = enabled,
            focusRequester = focusRequester,
            visualTransformation = visualTransformation,
        )
    }

    @Composable
    fun MultilineLineTextInput(
        modifier: Modifier = Modifier,
        style: TextFieldStyle = TextFieldStyle.DEFAULT,
        value: String = "",
        title: String = "",
        countText: String = "",
        isShowCountText: Boolean = countText.isNotEmpty(),
        isObligatorily: Boolean = false,
        maxSymbolsReached: Boolean,
        placeholder: String = "",
        onValueChange: (String) -> Unit = {},
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        enabled: Boolean = true,
        focusRequester: FocusRequester = remember { FocusRequester() },
        visualTransformation: VisualTransformation = VisualTransformation.None
    ) {
        val hintColor = if (maxSymbolsReached) {
            colorResource(R.color.sky3)
        } else {
            colorResource(R.color.rich_purple)
        }

        TextInput(
            style = style,
            isShowHint = isShowCountText,
            modifier = modifier,
            value = value,
            title = title,
            hint = countText,
            placeholder = placeholder,
            onValueChange = onValueChange,
            isObligatorily = isObligatorily,
            isError = false,
            maxLines = Int.MAX_VALUE,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            hintColor = hintColor,
            enabled = enabled,
            focusRequester = focusRequester,
            visualTransformation = visualTransformation
        )
    }

    @Composable
    private fun TextInput(
        style: TextFieldStyle,
        isShowHint: Boolean,
        modifier: Modifier = Modifier,
        value: String = "",
        title: String = "",
        hint: String = "",
        imagePainter: Painter? = null,
        hintColor: Color = colorResource(R.color.sky3),
        placeholder: String = "",
        onValueChange: (String) -> Unit = {},
        focusedState: MutableState<Boolean> = remember { mutableStateOf(false) },
        isObligatorily: Boolean = false,
        isError: Boolean = false,
        maxLines: Int = 1,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        enabled: Boolean = true,
        focusRequester: FocusRequester,
        visualTransformation: VisualTransformation
    ) {
        Surface(
            color = Color.Unspecified,
            contentColor = Color.Unspecified,
            shape = RoundedCornerShape(12.dp),
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.CenterVertically,
                ),
                modifier = Modifier.background(Color.Unspecified)
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = colorResource(R.color.white),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = enabled,
                            onClick = { focusRequester.requestFocus() }
                        )
                        .then(heightModifier(maxLines = maxLines))
                        .then(
                            borderModifier(
                                style = style,
                                inputFocused = focusedState.value,
                                isError = isError
                            )
                        )
                ) {
                    Title(
                        inputFocused = focusedState.value,
                        title = getTitle(title, isObligatorily),
                        input = value,
                    )
                    Input(
                        value = value,
                        onValueChange = onValueChange,
                        maxLines = maxLines,
                        focusRequester = focusRequester,
                        focusedState = focusedState,
                        enabled = enabled,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        visualTransformation = visualTransformation
                    )
                    if (value.isNotEmpty()) {
                        Box(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            if (focusedState.value) {
                                ButtonClose(
                                    buttonStyle = if (isError) ButtonStyle.RED else ButtonStyle.GREY
                                ) {
                                    onValueChange("")
                                }
                            } else {
                                imagePainter?.let {
                                    Image(
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .size(32.dp),
                                        painter = imagePainter,
                                        contentScale = ContentScale.FillWidth,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                    Placeholder(
                        placeholder = placeholder,
                        input = value,
                        inputFocused = focusedState.value,
                    )
                }
                if (isShowHint) {
                    Hint(
                        hint = hint,
                        hintColor = hintColor
                    )
                }
            }
        }
    }

    @Composable
    private fun BoxScope.Title(
        inputFocused: Boolean,
        title: CharSequence,
        input: String,
    ) {
        val color = colorResource(R.color.sky3)

        if (inputFocused || input.isNotEmpty()) {
            Texts.Caption(
                title = title.toString(),
                overflow = TextOverflow.Ellipsis,
                color = color,
                modifier = Modifier
                    .padding(start = 12.dp, top = 10.dp)
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
            )
        } else {
            Texts.ParagraphText(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(),
                title = title.toString(),
                color = colorResource(R.color.sky3)
            )
        }
    }

    @Composable
    private fun BoxScope.Input(
        value: String,
        onValueChange: (String) -> Unit,
        maxLines: Int,
        focusRequester: FocusRequester,
        focusedState: MutableState<Boolean>,
        enabled: Boolean,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        visualTransformation: VisualTransformation
    ) {
        var textFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                )
            )
        }
        val textFieldValue = textFieldValueState.copy(text = value)
        val transformation = if (!focusedState.value && value.isEmpty()) {
            VisualTransformation.None
        } else {
            visualTransformation
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                val previousValue = textFieldValue.text
                textFieldValueState = it
                if (previousValue != it.text) {
                    onValueChange(it.text)
                }
            },
            maxLines = maxLines,
            singleLine = maxLines == 1,
            textStyle = MaterialTheme.typography.subtitle1,
            cursorBrush = SolidColor(colorResource(R.color.rich_purple)),
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 10.dp, top = 26.dp)
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged {
                    focusedState.value = it.isFocused
                },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = transformation
        )
    }

    @Composable
    private fun BoxScope.Placeholder(
        placeholder: String,
        input: String,
        inputFocused: Boolean,
    ) {
        if (placeholder.isNotEmpty() && inputFocused && input.isEmpty())
            Text(
                text = placeholder,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = colorResource(R.color.sky3),
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            )
    }

    @Composable
    private fun Hint(
        hint: CharSequence,
        hintColor: Color,
    ) {
        Texts.Caption(
            modifier = Modifier
                .padding(start = 2.dp)
                .fillMaxWidth(),
            title = hint.toString(),
            color = hintColor,
            overflow = TextOverflow.Ellipsis
        )
    }

    @Composable
    private fun getTitle(title: String, isObligatorily: Boolean): AnnotatedString =
        buildAnnotatedString {
            append(title)
            if (isObligatorily) {
                withStyle(style = SpanStyle(color = colorResource(R.color.fury))) {
                    append(ASTERISKS)
                }
            }
        }

    @Composable
    private fun borderModifier(style: TextFieldStyle, inputFocused: Boolean, isError: Boolean) =
        Modifier.border(
            width = 1.dp,
            color = if (isError) colorResource(R.color.fury)
            else if (inputFocused) colorResource(R.color.sky3)
            else if (style == TextFieldStyle.DEFAULT) colorResource(R.color.sky2)
            else Color.Unspecified,
            shape = RoundedCornerShape(12.dp)
        )

    @Composable
    private fun heightModifier(maxLines: Int) =
        if (maxLines == 1) {
            Modifier.height(60.dp)
        } else {
            Modifier.heightIn(min = 60.dp, max = Dp.Infinity)
        }

    private const val ASTERISKS = "*"
}

enum class TextFieldStyle {
    DEFAULT,
    WHITE
}
