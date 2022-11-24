package uz.ucell.ui_kit.common_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.inputs.PinCodeInput
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.UcellAppBar

@Composable
fun PinCodeScreen(
    title: String,
    input: String,
    isError: Boolean,
    errorMessage: String = "",
    isFirstAuthorization: Boolean = false,
    showBiometry: Boolean = false,
    showLogoutButton: Boolean,
    onBackClick: () -> Unit = {},
    onCodeValueChanged: (String) -> Unit,
    removeSymbolClick: () -> Unit,
    logoutClick: () -> Unit = {},
    biometryClick: () -> Unit = {}
) {
    Column(modifier = Modifier.systemBarsPadding()) {
        UcellAppBar(
            navigationAction = if (isFirstAuthorization)
                NavigationAction.Back(onBackClick = { onBackClick() })
            else
                NavigationAction.Empty
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Texts.H2(
                modifier = Modifier.padding(top = 26.dp, bottom = 46.dp),
                title = title
            )

            PinCodeInput(
                input = input,
                codeLength = 4,
            )

            if (isError) {
                Texts.Caption(
                    modifier = Modifier.padding(top = 26.dp),
                    title = errorMessage,
                    color = colorResource(R.color.warm_red)
                )
            }

            if (isFirstAuthorization) {
                Texts.Caption(
                    modifier = Modifier
                        .padding(top = 26.dp)
                        .weight(1f),
                    title = stringResource(R.string.auth_create_pin_caption),
                    color = colorResource(R.color.sky3),
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            PinInputKeyboard(
                modifier = Modifier.padding(bottom = 48.dp),
                showRemoveButton = input.isNotEmpty(),
                showLogoutButton = showLogoutButton,
                showBiometry = showBiometry,
                keyboardClick = onCodeValueChanged,
                removeSymbolClick = removeSymbolClick,
                logoutClick = logoutClick,
                biometryClick = biometryClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinInputKeyboard(
    modifier: Modifier = Modifier,
    showRemoveButton: Boolean,
    showLogoutButton: Boolean,
    showBiometry: Boolean,
    keyboardClick: (text: String) -> Unit,
    removeSymbolClick: () -> Unit,
    logoutClick: () -> Unit,
    biometryClick: () -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(BUTTONS) {
                if (it.isDigit()) {
                    KeyboardDigitButton(
                        char = it,
                        modifier = Modifier
                            .clickable { keyboardClick(it.toString()) }
                            .then(buttonModifier)
                    )
                } else {
                    when (it) {
                        LEFT_BOTTOM_BUTTON -> if (showLogoutButton)
                            KeyboardLeftButton(
                                modifier = Modifier
                                    .clickable { logoutClick() }
                                    .then(buttonModifier)
                            )
                        RIGHT_BOTTOM_BUTTON -> if (showRemoveButton) {
                            KeyboardRightButton(
                                modifier = Modifier
                                    .clickable { removeSymbolClick() }
                                    .then(buttonModifier),
                                icon = painterResource(R.drawable.ic_remove),
                            )
                        } else if (showBiometry) {
                            KeyboardRightButton(
                                modifier = Modifier
                                    .clickable { biometryClick() }
                                    .then(buttonModifier),
                                icon = painterResource(R.drawable.ic_fingerprint),
                            )
                        }
                    }
                }
            }
        }
    }
}

private val buttonModifier: Modifier = Modifier
    .fillMaxSize()
    .defaultMinSize(minHeight = 52.dp)

@Composable
fun KeyboardDigitButton(
    char: Char,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Texts.H1(
            modifier = Modifier.fillMaxSize(),
            title = char.toString(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun KeyboardRightButton(
    icon: Painter,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = icon,
            contentScale = ContentScale.None,
            alignment = Alignment.Center,
            contentDescription = null
        )
    }
}

@Composable
fun KeyboardLeftButton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Texts.H4(
            modifier = Modifier.fillMaxSize(),
            title = stringResource(R.string.button_logout),
            textAlign = TextAlign.Center
        )
    }
}

private const val LEFT_BOTTOM_BUTTON = 'L'
private const val RIGHT_BOTTOM_BUTTON = 'R'

private val BUTTONS = listOf(
    '1', '2', '3', '4', '5', '6', '7', '8', '9',
    LEFT_BOTTOM_BUTTON, '0', RIGHT_BOTTOM_BUTTON
)
