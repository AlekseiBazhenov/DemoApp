package uz.ucell.feature_otp.enter_phone

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.ucell.core.models.Language
import uz.ucell.feature_otp.R
import uz.ucell.feature_otp.enter_phone.contract.EnterPhoneState
import uz.ucell.feature_otp.enter_phone.views.ConsentText
import uz.ucell.feature_otp.enter_phone.views.SelectLanguage
import uz.ucell.ui_kit.components.RadioButton
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.UcellLogo
import uz.ucell.ui_kit.components.UcellModalBottomSheet
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.components.inputs.PhoneInput
import uz.ucell.ui_kit.extentions.ucellShadow
import uz.ucell.ui_kit.theme.UcellTheme

@Preview(name = "day", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun EnterPhoneScreenPreview() {
    UcellTheme {
        EnterPhoneScreen(
            state = EnterPhoneState(),
            languages = listOf(),
            onPhoneValueChanged = { },
            onButtonClicked = { },
            onOfferLinkClicked = { },
            onLanguageClicked = { }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EnterPhoneScreen(
    state: EnterPhoneState,
    languages: List<Language>,
    onPhoneValueChanged: (String) -> Unit,
    onButtonClicked: () -> Unit,
    onOfferLinkClicked: (String) -> Unit,
    onLanguageClicked: (Language) -> Unit,
    onAvailableLanguageClick: (() -> Unit)? = null,
    onModalBottomSheetExpanded: (() -> Unit)? = null,
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    LaunchedEffect(modalBottomSheetState.currentValue) {
        if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
            onModalBottomSheetExpanded?.invoke()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isPhoneInputFocused by remember { mutableStateOf(true) }

    LaunchedEffect(modalBottomSheetState.currentValue) {
        isPhoneInputFocused = modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden
    }

    UcellModalBottomSheet(
        modalBottomSheetState,
        content = {
            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 46.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UcellLogo()
                    Spacer(Modifier.weight(1f))
                    state.selectedLanguage?.let {
                        SelectLanguage(
                            language = it,
                            onSelectCLick = {
                                onAvailableLanguageClick?.invoke()
                                isPhoneInputFocused = false
                                coroutineScope.launch {
                                    modalBottomSheetState.show()
                                }
                            }
                        )
                    }
                }
                Texts.H2(
                    modifier = Modifier.padding(top = 16.dp),
                    title = stringResource(R.string.auth_enter_phone_number_title)
                )
                PhoneInput(
                    modifier = Modifier.padding(top = 24.dp),
                    phoneInput = state.phoneInput,
                    onPhoneValueChanged = onPhoneValueChanged,
                    isError = state.hasPhoneError,
                    hint = state.phoneErrorMessage,
                    needRequestFocus = isPhoneInputFocused
                )
                ButtonPrimary(
                    modifier = Modifier.padding(top = 24.dp),
                    text = stringResource(id = R.string.button_continue),
                    isLoading = state.isLoading,
                    enabled = state.isButtonEnabled,
                    onClick = {
                        keyboardController?.hide()
                        onButtonClicked.invoke()
                    }
                )
                ConsentText(
                    modifier = Modifier.padding(top = 24.dp),
                    viewState = state,
                    onOfferClick = onOfferLinkClicked
                )
            }
        },
        sheetContent = {
            Texts.H3(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                title = stringResource(R.string.select_language),
                textAlign = TextAlign.Center
            )
            LanguageSelectableList(
                languages = languages,
                defaultSelectedLanguage = state.selectedLanguage,
                onLanguageClicked = {
                    coroutineScope.launch {
                        modalBottomSheetState.snapTo(ModalBottomSheetValue.Hidden)
                    }
                    onLanguageClicked.invoke(it)
                }
            )
        }
    )
}

@Composable
fun LanguageSelectableList(
    languages: List<Language>,
    defaultSelectedLanguage: Language? = languages.firstOrNull(),
    onLanguageClicked: (Language) -> Unit
) {
    val selectedValue = remember { mutableStateOf(defaultSelectedLanguage) }
    LazyColumn {
        items(languages) { language: Language ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedValue.value == language),
                        onClick = {
                            selectedValue.value = language
                            onLanguageClicked(language)
                        },
                        role = Role.RadioButton
                    )
                    .padding(top = 4.dp, bottom = 8.dp)
                    .ucellShadow(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(language.flag),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        text = stringResource(language.title),
                        style = MaterialTheme.typography.body1
                    )
                    RadioButton(isSelected = selectedValue.value == language, isEnabled = true) {
                        selectedValue.value = language
                        onLanguageClicked(language)
                    }
                }
            }
        }
    }
}
