package uz.ucell.debugpanel.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.debugpanel.R
import uz.ucell.debugpanel.presentation.contract.EnterDebugPanelState
import uz.ucell.ui_kit.components.LabeledCheckBox
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.UcellAppBar

@Composable
fun DebugPanelScreen(
    state: EnterDebugPanelState,
    onHostChanged: (String) -> Unit,
    onDeviceIdChanged: (String) -> Unit,
    onUserAgentChanged: (String) -> Unit,
    onMsisdnChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNetworkLoggingChecked: (Boolean) -> Unit,
    onDevTokenChanged: (String) -> Unit,
    onNetworkLogsButtonClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.systemBarsPadding()) {
        UcellAppBar(
            navigationAction = NavigationAction.Back(onBackClick = onBackClicked),
            elevation = animateDpAsState(if (scrollState.value == 0) 0.dp else 8.dp).value
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp)
        ) {

            OutlinedTextField(
                value = state.host,
                onValueChange = onHostChanged,
                label = { Text(stringResource(id = R.string.label_host)) }
            )

            OutlinedTextField(
                value = state.deviceId,
                onValueChange = onDeviceIdChanged,
                label = { Text(stringResource(id = R.string.label_device_id)) }
            )

            OutlinedTextField(
                value = state.userAgent,
                onValueChange = onUserAgentChanged,
                label = { Text(stringResource(id = R.string.label_user_agent)) }
            )

            OutlinedTextField(
                value = state.msisdn,
                onValueChange = onMsisdnChanged,
                label = { Text(stringResource(id = R.string.label_msisdn)) }
            )

            OutlinedTextField(
                value = state.devToken,
                onValueChange = onDevTokenChanged,
                label = { Text(stringResource(id = R.string.dev_token)) }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            Button(
                onClick = onClearClicked,
                contentPadding = PaddingValues(
                    vertical = 20.dp,
                    horizontal = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(id = R.string.btn_clear_tokens))
            }

            LabeledCheckBox(
                modifier = Modifier.padding(vertical = 8.dp),
                label = stringResource(R.string.label_network_logging),
                checked = state.networkLoggingEnabled,
                onCheckedChange = onNetworkLoggingChecked
            )
            Button(
                onClick = onNetworkLogsButtonClicked,
                contentPadding = PaddingValues(
                    vertical = 20.dp,
                    horizontal = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(id = R.string.btn_open_network_logs))
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .padding(32.dp)
        ) {
            Button(
                onClick = onSaveClicked,
                contentPadding = PaddingValues(
                    vertical = 20.dp,
                    horizontal = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(id = R.string.btn_save))
            }
        }
    }
}
