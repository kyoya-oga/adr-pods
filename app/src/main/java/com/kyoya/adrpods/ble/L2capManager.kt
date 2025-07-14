package com.kyoya.adrpods.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.IOException

// This class requires BLUETOOTH_CONNECT permission.
@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.Q)
class L2capManager {

    private var bluetoothSocket: BluetoothSocket? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    suspend fun connect(device: BluetoothDevice) {
        withContext(Dispatchers.IO) {
            try {
                // PSM for AAP (Apple Accessory Protocol)
                val psm = 0x1001
                bluetoothSocket = device.createL2capChannel(psm)
                _connectionState.value = ConnectionState.Connecting
                bluetoothSocket?.connect()
                _connectionState.value = ConnectionState.Connected
                // TODO: Start listening for incoming data.
            } catch (e: IOException) {
                _connectionState.value = ConnectionState.Error("L2CAP connection failed: ${e.message}")
                disconnect()
            }
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            try {
                bluetoothSocket?.close()
                bluetoothSocket = null
                _connectionState.value = ConnectionState.Disconnected
            } catch (e: IOException) {
                // Log the exception
            }
        }
    }

    // TODO: Implement methods for sending and receiving data through the socket.
}

sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}
