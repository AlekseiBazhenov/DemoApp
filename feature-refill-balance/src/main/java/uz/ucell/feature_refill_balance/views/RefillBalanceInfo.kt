package uz.ucell.feature_refill_balance.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.feature_refill_balance.contract.RefillBalanceState
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.inputs.MoneyInput
import uz.ucell.ui_kit.components.inputs.PhoneInput

@Composable
fun RefillBalanceInfo(
    state: RefillBalanceState,
    onPhoneValueChanged: (String) -> Unit,
    onAmountValueChanged: (String) -> Unit,
    onRecommendedAmountClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        PhoneInput(
            modifier = Modifier.padding(bottom = 16.dp),
            title = if (state.isOtherMsisdn || state.userBalance.isEmpty()) {
                stringResource(R.string.phone_number)
            } else {
                stringResource(R.string.my_phone_number_with_balance, state.userBalance)
            },
            phoneInput = state.phoneInput,
            onPhoneValueChanged = onPhoneValueChanged,
            isError = false,
            needRequestFocus = false
        )
        MoneyInput(
            input = state.amountInput,
            recommendations = state.paymentRecommendations,
            onAmountValueChanged = onAmountValueChanged,
            onRecommendedAmountClicked = onRecommendedAmountClicked
        )
        state.paymentLimits?.let { (minSum, maxSum, commission) ->
            Texts.Caption(
                modifier = Modifier.padding(bottom = 12.dp),
                title = stringResource(
                    R.string.refill_balance_limits, minSum, maxSum, commission
                )
            )
        }
    }
}
