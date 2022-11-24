package uz.ucell.debugpanel.logging_panel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.debugpanel.logging_panel.contract.ErrorPanelSideEffect
import uz.ucell.debugpanel.logging_panel.contract.ErrorPanelState
import uz.ucell.networking.logging_db.LogsDao
import javax.inject.Inject

@HiltViewModel
class NetworkLogsViewModel @Inject constructor(
    private val logsDao: LogsDao,
) : ContainerHost<ErrorPanelState, ErrorPanelSideEffect>,
    ViewModel() {

    override val container =
        container<ErrorPanelState, ErrorPanelSideEffect>(ErrorPanelState())

    init {
        intent {
            val logs = logsDao.getAll()
            reduce {
                state.copy(logs = logs)
            }
        }
    }

    fun onClearLogsClicked() = intent {
        logsDao.deleteAll()
        reduce {
            state.copy(logs = emptyList())
        }
    }
}
