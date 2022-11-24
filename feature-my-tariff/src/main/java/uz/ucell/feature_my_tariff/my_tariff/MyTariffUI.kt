package uz.ucell.feature_my_tariff.my_tariff

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import uz.ucell.feature_my_tariff.my_tariff.contract.MyTariffState
import uz.ucell.feature_my_tariff.my_tariff.views.ButtonsBlock
import uz.ucell.feature_my_tariff.my_tariff.views.Faq
import uz.ucell.feature_my_tariff.my_tariff.views.IncludedInTariff
import uz.ucell.feature_my_tariff.my_tariff.views.MyTariffAdditionalTerms
import uz.ucell.feature_my_tariff.my_tariff.views.MyTariffLoading
import uz.ucell.feature_my_tariff.my_tariff.views.TariffInfoCard
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.common_ui.ServerErrorView
import uz.ucell.ui_kit.components.buttons.ButtonTheme
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.ScrollStrategy
import uz.ucell.ui_kit.components.nav_bar.collapsing.CollapsingToolbarScaffold
import uz.ucell.ui_kit.components.nav_bar.collapsing.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyTariffScreen(
    state: MyTariffState,
    onBackClick: () -> Unit,
    onMoreAboutTariffClicked: () -> Unit,
    onChangeTariffClick: () -> Unit,
    faqLinkClicked: (String) -> Unit,
    onRetryRequestClick: () -> Unit,
) {

    val toolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
    val scrollState = rememberScrollState()

    CollapsingToolbarScaffold(
        modifier = Modifier.systemBarsPadding(),
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        state = toolbarScaffoldState,
        navigationAction = NavigationAction.Back(onBackClick = { onBackClick() }),
        title = stringResource(R.string.my_tariff_title)
    ) {
        if (state.isLoading) {
            MyTariffLoading()
        } else if (state.isServerError) {
            MyTariffServerError(onRetryRequestClick)
        } else {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    state.tariffInfo?.let {
                        TariffInfoCard(
                            isFinancialBlock = state.isFinancialBlock,
                            tariffInfo = it,
                            onRetryRequestClick = onRetryRequestClick
                        )
                    }

                    state.tariffLimits?.let {
                        IncludedInTariff(
                            isFinancialBlock = state.isFinancialBlock,
                            limits = it
                        )
                    }

                    state.pricesOverLimit?.let {
                        MyTariffAdditionalTerms(
                            title = stringResource(R.string.my_tariff_over_limit_price),
                            info = it,
                        )
                    }

                    state.internationalCommunication?.let {
                        MyTariffAdditionalTerms(
                            title = stringResource(R.string.my_tariff_international_communications),
                            info = it,
                        )
                    }

                    state.faq?.let {
                        Faq(
                            items = it,
                            onMoreAboutTariffClicked = onMoreAboutTariffClicked,
                            linkClicked = faqLinkClicked
                        )
                    }

                    ButtonsBlock(onChangeTariffClick)
                }
            }
        }
    }
}

@Composable
fun MyTariffServerError(
    onRetryRequestClick: () -> Unit
) {
    ServerErrorView(
        title = stringResource(R.string.common_error),
        titleImage = painterResource(R.drawable.ic_magnifier),
        titleImageColor = colorResource(id = R.color.sky1),
        subtitle = stringResource(R.string.common_error_description),
        buttonTitle = stringResource(R.string.button_refresh),
        buttonTheme = ButtonTheme.SOFT_PURPLE,
        buttonImage = painterResource(R.drawable.ic_refresh_16),
        onButtonClick = onRetryRequestClick
    )
}
