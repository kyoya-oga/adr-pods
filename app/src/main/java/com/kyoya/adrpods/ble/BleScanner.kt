package com.kyoya.adrpods.ble

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// This class requires BLUETOOTH_SCAN permission.
// The permission check will be handled in the UI layer before this class is used.
@SuppressLint("MissingPermission")
class BleScanner(
    private val bluetoothLeScanner: BluetoothLeScanner
) {

    private val _scannedDevices = MutableStateFlow<List<ScanResult>>(emptyList())
    val scannedDevices: StateFlow<List<ScanResult>> = _scannedDevices

    private val scanFilters: List<ScanFilter> = listOf(
        // Filter for Apple devices using the company ID.
        // The plan mentions filtering by Apple VID 0x004C.
        ScanFilter.Builder()
            .setManufacturerData(0x004C, byteArrayOf())
            .build()
    )

    private val scanSettings: ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let {
                val currentList = _scannedDevices.value.toMutableList()
                if (currentList.none { it.device.address == result.device.address }) {
                    currentList.add(result)
                    _scannedDevices.value = currentList
                }
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>?) {
            results?.let {
                val currentList = _scannedDevices.value.toMutableList()
                it.forEach { result ->
                    if (currentList.none { it.device.address == result.device.address }) {
                        currentList.add(result)
                    }
                }
                _scannedDevices.value = currentList
            }
        }

        override fun onScanFailed(errorCode: Int) {
            // TODO: Implement proper error handling (e.g., log the error, notify the user).
        }
    }

    fun startScan() {
        _scannedDevices.value = emptyList()
        bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)
    }

    fun stopScan() {
        bluetoothLeScanner.stopScan(scanCallback)
    }
}
