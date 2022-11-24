package uz.ucell.core.dialogs

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
import uz.ucell.ui_kit.dialogs.FullScreenDialogUI

class FullScreenDialogFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val title = requireArguments().getString(KEY_TITLE)
        val message = requireArguments().getString(KEY_MESSAGE)
        val button = requireArguments().getString(KEY_BUTTON)
        val isError = requireArguments().getBoolean(KEY_ERROR)

        return ComposeView(requireContext()).apply {
            setContent {
                FullScreenDialogUI(
                    isError = isError,
                    title = title,
                    message = message,
                    primaryButton = button,
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
        setStatusBarColor(
            if (requireArguments().getBoolean(KEY_ERROR)) R.color.error else R.color.warning
        )
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
        private const val KEY_ERROR = "is_error"
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"
        private const val KEY_BUTTON = "button"

        const val REQUEST_KEY = "full_screen_dialog_request_key"
        const val RESULT_KEY = "full_screen_dialog_result_key"
    }
}
