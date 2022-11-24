package uz.ucell.debugpanel.logging_panel.contract

import uz.ucell.networking.logging_db.LogInfo

data class ErrorPanelState(
    var logs: List<LogInfo> = emptyList()
)
