package uz.ucell.feature_refill_balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.ucell.core.BaseFragment
import uz.ucell.feature_refill_balance.contract.RefillBalanceSideEffect
import uz.ucell.navigation.Refill
import uz.ucell.ui_kit.theme.setUcellContent

@AndroidEntryPoint
class RefillBalanceFragment : BaseFragment() {

    private val viewModel: RefillBalanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setUcellContent {
                LaunchedEffect(viewModel) {
                    viewModel.container.sideEffectFlow.collect(::handleSideEffect)
                }

                val state = viewModel.container.stateFlow.collectAsState().value

                RefillBalanceScreen(
                    state = state,
                    onBackClick = ::navigateBack,
                    onPhoneValueChanged = { viewModel.onNewPhoneValue(it) },
                    onSumValueChanged = viewModel::onNewAmountValue,
                    onRecommendedSumClicked = viewModel::onNewAmountValue,
                    onBankCardSelected = viewModel::onBankCardSelected,
                    onSuccessPayment = {
                        navigateTo(
                            Refill.ToSuccessPaymentFromRefill(
                                receiptId = it.id,
                                amount = it.amount.toString(),
                                commission = it.commission.toString(),
                                payDate = it.payDate,
                                cardInfo = it.maskedPan, // todo + Humo или Uzcard по первым 4 знакам "maskedPan"
                                account = it.accounts.first().value
                            )
                        )
                    }
                )
            }
        }
    }

    private fun handleSideEffect(sideEffect: RefillBalanceSideEffect) {
        when (sideEffect) {
            is RefillBalanceSideEffect.ShowError -> showError(
                message = sideEffect.text,
                code = sideEffect.code
            )
        }
    }
}
