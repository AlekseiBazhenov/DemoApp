package uz.ucell.debugpanel.logging_panel

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uz.ucell.debugpanel.logging_panel.contract.ErrorPanelState
import uz.ucell.networking.logging_db.LogInfo
import uz.ucell.ui_kit.components.Texts.ParagraphText
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.UcellAppBar

@Composable
fun ErrorPanelScreen(
    state: ErrorPanelState,
    onClearClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.systemBarsPadding()) {
        UcellAppBar(
            actions = {
                Text(
                    text = "Clear",
                    modifier = Modifier.clickable {
                        onClearClicked()
                    }
                )
            },
            navigationAction = NavigationAction.Back(onBackClick = onBackClicked),
            elevation = animateDpAsState(if (scrollState.value == 0) 0.dp else 8.dp).value
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = state.logs
            ) {
                ErrorListItem(error = it)
                Divider(color = Color.Black, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ErrorListItem(error: LogInfo) {
    val isCollapsed = remember {
        mutableStateOf(true)
    }
    Row(
        modifier = Modifier.clickable {
            isCollapsed.value = !isCollapsed.value
        }
    ) {
        if (isCollapsed.value) {
            ParagraphText(title = error.base ?: "")
        } else {
            ParagraphText(title = error.detail ?: "")
        }
    }
}
