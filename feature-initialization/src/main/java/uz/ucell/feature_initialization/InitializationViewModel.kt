package uz.ucell.feature_initialization

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_initialization.contract.InitializationSideEffect
import uz.ucell.feature_initialization.contract.InitializationState
import javax.inject.Inject

@HiltViewModel
class InitializationViewModel @Inject constructor(
    private val coreStorage: CoreStorage,
) : ContainerHost<InitializationState, InitializationSideEffect>,
    ViewModel() {

    private var isChecked = false
    override val container =
        container<InitializationState, InitializationSideEffect>(InitializationState())

    fun animationEnded() {
        if (isChecked) return
        isChecked = true
        checkIfPinCreated()
    }

    private fun checkIfPinCreated() = intent {
        val pinCreated = coreStorage.getPinCreated().first()
        val selectedLanguage = coreStorage.getSelectedLanguage().first()
        postSideEffect(
            if (pinCreated)
                InitializationSideEffect.OpenPinAuthScreen
            else if (selectedLanguage.isEmpty())
                InitializationSideEffect.OpenSelectLanguageScreen
            else {
                InitializationSideEffect.OpenEnterPhoneScreen(selectedLanguage)
            }

        )
    }
}
