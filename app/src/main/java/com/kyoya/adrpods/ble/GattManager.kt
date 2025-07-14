package com.kyoya.adrpods.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

// This class requires BLUETOOTH_CONNECT permission.
@SuppressLint("MissingPermission")
class GattManager {

    private var bluetoothGatt: BluetoothGatt? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    // Standard Battery Service UUID
    private val BATTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    _connectionState.value = ConnectionState.Connected
                    gatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _connectionState.value = ConnectionState.Disconnected
                    disconnect()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // TODO: Subscribe to battery level characteristic notifications.
            }
        }

        // TODO: Implement onCharacteristicRead and onCharacteristicChanged callbacks.
    }

    fun connect(context: Context, device: BluetoothDevice) {
        _connectionState.value = ConnectionState.Connecting
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        _connectionState.value = ConnectionState.Disconnected
    }
}
