package uz.ucell.feature_my_tariff.tariff_list.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import uz.ucell.feature_my_tariff.tariff_list.mock.MockTariffListStateProvider
import uz.ucell.feature_my_tariff.tariff_list.model.Badge
import uz.ucell.feature_my_tariff.tariff_list.model.TariffLimit
import uz.ucell.feature_my_tariff.tariff_list.model.TariffListState
import uz.ucell.feature_my_tariff.tariff_list.model.TariffRatePlanPrice
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.ScrollStrategy
import uz.ucell.ui_kit.components.nav_bar.collapsing.CollapsingToolbarScaffold
import uz.ucell.ui_kit.components.nav_bar.collapsing.rememberCollapsingToolbarScaffoldState
import uz.ucell.ui_kit.theme.UcellTheme

@Composable
fun TariffListScreen(
    uiState: TariffListState,
    onBackClick: () -> Unit,
    onTariffClicked: () -> Unit,
    onAdvantagesClicked: () -> Unit,
    onErrorButtonClicked: () -> Unit
) {
    val collapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.systemBarsPadding(),
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        state = collapsingToolbarScaffoldState,
        navigationAction = NavigationAction.Back(onBackClick = onBackClick),
        title = stringResource(R.string.select_tariff_title)
    ) {
        when {
            uiState.isLoading -> TariffLoading()
            uiState.isError -> TariffListError(
                onButtonClick = onErrorButtonClicked
            )
            else -> TariffList(
                cells = uiState.tariffCells,
                onTariffClick = onTariffClicked,
                onAdvantagesClick = onAdvantagesClicked
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TariffList(
    cells: List<TariffCell>,
    onTariffClick: () -> Unit,
    onAdvantagesClick: () -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn {
            items(cells) { cell ->
                TariffItem(
                    cell = cell,
                    onTariffClick = onTariffClick,
                    onAdvantagesClick = onAdvantagesClick
                )
            }
        }
    }
}

data class TariffCell(
    val title: String,
    val badge: Badge? = null,
    val ratePlanPrice: TariffRatePlanPrice? = null,
    val limits: List<TariffLimit>? = null,
    val advantages: List<Advantage>? = null
)

@Preview(
    name = "tariff list day",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 900
)
@Composable
fun TariffListScreenPreview(
    @PreviewParameter(MockTariffListStateProvider::class) mockTariffListState: TariffListState
) {
    UcellTheme {
        TariffListScreen(
            uiState = mockTariffListState,
            onBackClick = { },
            onTariffClicked = { },
            onAdvantagesClicked = { },
            onErrorButtonClicked = { }
        )
    }
}
