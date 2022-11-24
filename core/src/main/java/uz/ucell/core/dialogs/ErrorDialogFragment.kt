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
import uz.ucell.ui_kit.dialogs.DialogType
import uz.ucell.ui_kit.dialogs.PopupDialog

class ErrorDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val title = requireArguments().getString(KEY_TITLE)
        val message = requireArguments().getString(KEY_MESSAGE)
        val code = requireArguments().getString(KEY_CODE)
        val primaryButton = requireArguments().getString(KEY_PRIMARY_BUTTON)
        val secondaryButton = requireArguments().getString(KEY_SECONDARY_BUTTON)
        val requestKey = requireArguments().getString(KEY_REQUEST) ?: DEFAULT_REQUEST_KEY
        return ComposeView(requireContext()).apply {
            setContent {
                val showDialog = remember { mutableStateOf(true) }
                PopupDialog(
                    showDialog = showDialog.value,
                    title = title,
                    message = message,
                    code = code,
                    primaryButton = primaryButton,
                    secondaryButton = secondaryButton,
                    dialogType = DialogType.ERROR,
                    onPrimaryClick = {
                        parentFragmentManager.setFragmentResult(
                            requestKey,
                            bundleOf(RESULT_KEY to DialogResult.PRIMARY)
                        )
                        closeDialog(showDialog)
                    },
                    onSecondaryClick = {
                        parentFragmentManager.setFragmentResult(
                            requestKey,
                            bundleOf(RESULT_KEY to DialogResult.SECONDARY)
                        )
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
        const val TAG = "ErrorDialogFragment"
        const val DEFAULT_REQUEST_KEY = "default_request_key"
        const val RESULT_KEY = "result_key"
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"
        private const val KEY_CODE = "code"
        private const val KEY_PRIMARY_BUTTON = "primary_button"
        private const val KEY_SECONDARY_BUTTON = "secondary_button"
        private const val KEY_REQUEST = "key_request"

        fun newInstance(
            title: String? = null,
            message: String,
            code: String? = null,
            primaryButton: String? = null,
            secondaryButton: String? = null,
            requestKey: String
        ): ErrorDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_MESSAGE, message)
            args.putString(KEY_CODE, code)
            args.putString(KEY_PRIMARY_BUTTON, primaryButton)
            args.putString(KEY_SECONDARY_BUTTON, secondaryButton)
            args.putString(KEY_REQUEST, requestKey)
            val fragment = ErrorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
