package com.kyoya.adrpods.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyoya.adrpods.ble.ConnectionManager
import com.kyoya.adrpods.ble.ConnectionState
import com.kyoya.adrpods.aap.PacketRouter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirPodsViewModel(
    private val connectionManager: ConnectionManager,
    private val packetRouter: PacketRouter
) : ViewModel() {

    val connectionState: StateFlow<ConnectionState> = connectionManager.connectionState

    val batteryLevelLeft: StateFlow<Int?> = packetRouter.batteryLevelLeft
    val batteryLevelRight: StateFlow<Int?> = packetRouter.batteryLevelRight
    val batteryLevelCase: StateFlow<Int?> = packetRouter.batteryLevelCase

    // TODO: Add methods to be called from the UI, e.g., to initiate a connection or change noise control settings.

    fun disconnect() {
        viewModelScope.launch {
            connectionManager.disconnect()
        }
    }
}
