package uz.ucell.core

import androidx.fragment.app.Fragment
import uz.ucell.core.dialogs.ErrorDialogFragment
import uz.ucell.navigation.FullscreenError
import uz.ucell.navigation.NavigationDestination
import uz.ucell.navigation.NavigationHost

open class BaseFragment : Fragment() {

    fun showError(
        title: String? = null,
        message: String,
        code: String? = null,
        primaryButton: String = getString(R.string.button_ok),
        secondaryButton: String? = null,
        requestKey: String = ErrorDialogFragment.DEFAULT_REQUEST_KEY
    ) {
        // todo use navigation component
        ErrorDialogFragment.newInstance(
            title,
            message,
            code,
            primaryButton,
            secondaryButton,
            requestKey
        ).show(childFragmentManager, ErrorDialogFragment.TAG)
    }

    fun showFullscreenError(
        title: String? = null,
        message: String,
        button: String,
    ) {
        navigateTo(
            FullscreenError(
                title,
                message,
                button,
            )
        )
    }

    fun navigateTo(destination: NavigationDestination) {
        (requireActivity() as NavigationHost).navigateTo(destination)
    }

    fun navigateBack() {
        (requireActivity() as NavigationHost).navigateBack()
    }
}
