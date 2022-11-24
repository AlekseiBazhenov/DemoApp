package uz.ucell.ui_kit.components.nav_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

@Composable
fun UcellAppBar(
    modifier: Modifier = Modifier,
    titleText: String = "",
    navigationAction: NavigationAction,
    backgroundColor: Color = colorResource(R.color.white),
    elevation: Dp = 0.dp,
    actions: @Composable RowScope.() -> Unit = {},
    contentColor: Color = contentColorFor(backgroundColor),
    bottomContent: @Composable BoxScope.() -> Unit = {},
    needBottomDivider: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TopAppBar(
            title = {
                Text(text = titleText)
            },
            navigationIcon = NavigationIcon(navigationAction),
            backgroundColor = backgroundColor,
            modifier = modifier,
            actions = actions,
            contentColor = contentColor,
            elevation = elevation
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            bottomContent()
        }

        if (needBottomDivider) BottomDivider()
    }
}

@Composable
fun CustomTitleAppBar(
    modifier: Modifier = Modifier,
    customTitle: @Composable (() -> Unit),
    navigationAction: NavigationAction,
    backgroundColor: Color = colorResource(R.color.white),
    actions: @Composable RowScope.() -> Unit = {},
    contentColor: Color = contentColorFor(backgroundColor),
    needBottomDivider: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TopAppBar(
            title = customTitle,
            navigationIcon = NavigationIcon(navigationAction),
            backgroundColor = backgroundColor,
            modifier = modifier,
            actions = actions,
            contentColor = contentColor,
            elevation = 0.dp
        )

        if (needBottomDivider) BottomDivider()
    }
}

@Composable
private fun BottomDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.sky2))
    )
}
