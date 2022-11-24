package uz.ucell.core.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import uz.ucell.ui_kit.dialogs.AlertDialogUI

class AlertDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val title = requireArguments().getString(KEY_TITLE, "")
        val message = requireArguments().getString(KEY_MESSAGE)
        val primaryButton = requireArguments().getString(KEY_PRIMARY_BUTTON)
        val secondaryButton = requireArguments().getString(KEY_SECONDARY_BUTTON)
        return ComposeView(requireContext()).apply {
            setContent {
                val showDialog = remember { mutableStateOf(true) }
                AlertDialogUI(
                    showDialog = showDialog.value,
                    title = title,
                    text = message,
                    primaryButton = primaryButton,
                    secondaryButton = secondaryButton,
                    onPrimaryClick = {
                        parentFragmentManager.setFragmentResult(
                            REQUEST_KEY,
                            bundleOf(RESULT_KEY to DialogResult.PRIMARY)
                        )
                        closeDialog(showDialog)
                    },
                    onSecondaryClick = {
                        parentFragmentManager.setFragmentResult(
                            REQUEST_KEY,
                            bundleOf(RESULT_KEY to DialogResult.SECONDARY)
                        )
                        closeDialog(showDialog)
                    },
                    onDismissClick = {
                        closeDialog(showDialog)
                    }
                )
            }
        }
    }

    enum class DialogResult {
        PRIMARY,
        SECONDARY
    }

    private fun closeDialog(showDialog: MutableState<Boolean>) {
        showDialog.value = false
        dismiss()
    }

    companion object {
        const val TAG = "AlertDialogFragment"
        const val REQUEST_KEY = "request_key"
        const val RESULT_KEY = "result_key"
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"
        private const val KEY_PRIMARY_BUTTON = "primary_button"
        private const val KEY_SECONDARY_BUTTON = "secondary_button"

        fun newInstance(
            title: String,
            message: String? = null,
            primaryButton: String? = null,
            secondaryButton: String? = null,
        ): AlertDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_MESSAGE, message)
            args.putString(KEY_PRIMARY_BUTTON, primaryButton)
            args.putString(KEY_SECONDARY_BUTTON, secondaryButton)
            val fragment = AlertDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
