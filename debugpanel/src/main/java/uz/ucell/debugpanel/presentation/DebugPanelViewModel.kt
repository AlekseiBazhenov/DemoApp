package uz.ucell.debugpanel.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retry
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.debugpanel.presentation.contract.EnterDebugPanelSideEffect
import uz.ucell.debugpanel.presentation.contract.EnterDebugPanelState
import uz.ucell.networking.SetupNetworkInterface
import uz.ucell.utils.CoroutinesOperators.throttleFirst
import javax.inject.Inject

@HiltViewModel
class DebugPanelViewModel @Inject constructor(
    private val storage: CoreStorage,
    private val netSettings: SetupNetworkInterface
) : ContainerHost<EnterDebugPanelState, EnterDebugPanelSideEffect>,
    ViewModel() {

    companion object Event {
        enum class ClickEvents {
            SAVE, CLEAR_TOKENS
        }
    }

    override val container =
        container<EnterDebugPanelState, EnterDebugPanelSideEffect>(EnterDebugPanelState())
    private val clickChannel = Channel<ClickEvents>()

    init {
        intent {
            reduce {
                state.copy(
                    host = netSettings.getBaseUrl() ?: "",
                    devToken = netSettings.getDevToken() ?: "",
                    networkLoggingEnabled = netSettings.getRequestsLoggingEnabled()
                )
            }

            merge(
                storage.getDeviceId()
                    .onEach {
                        reduce {
                            state.copy(deviceId = it)
                        }
                    }
                    .retry(),
                storage.getUserAgent()
                    .onEach {
                        reduce {
                            state.copy(userAgent = it)
                        }
                    }
                    .retry(),
                storage.getMsisdn()
                    .onEach {
                        reduce {
                            state.copy(msisdn = it)
                        }
                    }
                    .retry(),
                clickChannel
                    .receiveAsFlow()
                    .throttleFirst(200)
                    .onEach {
                        when (it) {
                            ClickEvents.CLEAR_TOKENS -> {
                                netSettings.dropTokens()
                            }
                            ClickEvents.SAVE -> {
                                netSettings.setDevToken(state.devToken)
                                netSettings.setBaseUrl(state.host)
                                storage.setDeviceId(state.deviceId)
                                storage.setUserAgent(state.userAgent)
                            }
                        }
                    }
                    .retry()
            ).collect()
        }
    }

    fun onHostChanged(host: String) = intent {
        reduce {
            state.copy(host = host)
        }
    }

    fun onDeviceIdChanged(deviceId: String) = intent {
        reduce {
            state.copy(deviceId = deviceId)
        }
    }

    fun onUserAgentChanged(userAgent: String) = intent {
        reduce {
            state.copy(userAgent = userAgent)
        }
    }

    fun onMsisdnChanged(msisdn: String) = intent {
        reduce {
            state.copy(msisdn = msisdn)
        }
    }

    fun onClick(event: ClickEvents) = intent {
        clickChannel.send(event)
    }

    fun onNetworkLoggingChecked(checked: Boolean) = intent {
        netSettings.setRequestsLoggingEnabled(checked)
        reduce {
            state.copy(networkLoggingEnabled = checked)
        }
    }

    fun onDevTokenChanged(token: String) = intent {
        reduce {
            state.copy(devToken = token)
        }
    }
}
