package com.kyoya.adrpods.ble

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Build
import kotlinx.coroutines.flow.StateFlow

class ConnectionManager(
    private val context: Context,
    private val l2capManager: L2capManager,
    private val gattManager: GattManager
) {

    val connectionState: StateFlow<ConnectionState>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            l2capManager.connectionState
        } else {
            gattManager.connectionState
        }

    suspend fun connect(device: BluetoothDevice) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                l2capManager.connect(device)
            } catch (e: Exception) {
                // Fallback to GATT if L2CAP fails
                gattManager.connect(context, device)
            }
        } else {
            gattManager.connect(context, device)
        }
    }

    suspend fun disconnect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            l2capManager.disconnect()
        } else {
            gattManager.disconnect()
        }
    }
}
