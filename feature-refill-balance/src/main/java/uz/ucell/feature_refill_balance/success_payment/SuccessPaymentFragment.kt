package uz.ucell.feature_refill_balance.success_payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import uz.ucell.core.BaseFragment
import uz.ucell.core.R

class SuccessPaymentFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val receiptId = requireArguments().getString(KEY_RECEIPT_ID, "")
        val amount = requireArguments().getString(KEY_RECEIPT_AMOUNT, "")
        val commission = requireArguments().getString(KEY_RECEIPT_COMMISSION, "")
        val payDate = requireArguments().getString(KEY_RECEIPT_PAY_DATE, "")
        val cardInfo = requireArguments().getString(KEY_RECEIPT_CARD_INFO, "")
        val account = requireArguments().getString(KEY_RECEIPT_ACCOUNT, "")

        return ComposeView(requireContext()).apply {
            setContent {
                PaymentSuccessUI(
                    receiptId = receiptId,
                    amount = amount,
                    commission = commission,
                    payDate = payDate,
                    cardInfo = cardInfo,
                    account = account,
                    onButtonClick = {
                        parentFragmentManager.setFragmentResult(
                            REQUEST_KEY,
                            bundleOf(RESULT_KEY to DialogResult.BUTTON)
                        )
                        navigateBack()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.success)
    }

    override fun onStop() {
        super.onStop()
        setStatusBarColor(R.color.white)
    }

    private fun setStatusBarColor(@DrawableRes color: Int) {
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), color)
    }

    enum class DialogResult {
        BUTTON
    }

    companion object {
        private const val KEY_RECEIPT_ID = "receipt_id"
        private const val KEY_RECEIPT_AMOUNT = "receipt_amount"
        private const val KEY_RECEIPT_COMMISSION = "receipt_commission"
        private const val KEY_RECEIPT_PAY_DATE = "receipt_pay_date"
        private const val KEY_RECEIPT_CARD_INFO = "receipt_card_info"
        private const val KEY_RECEIPT_ACCOUNT = "receipt_account"

        const val REQUEST_KEY = "full_screen_dialog_request_key"
        const val RESULT_KEY = "full_screen_dialog_result_key"
    }
}
