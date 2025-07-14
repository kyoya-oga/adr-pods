package com.kyoya.adrpods.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kyoya.adrpods.ble.ConnectionManager
import com.kyoya.adrpods.ble.GattManager
import com.kyoya.adrpods.ble.L2capManager
import com.kyoya.adrpods.aap.PacketRouter

class AirPodsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AirPodsViewModel::class.java)) {
            // In a real app, you would use a dependency injection framework to provide these dependencies.
            val l2capManager = L2capManager()
            val gattManager = GattManager()
            val connectionManager = ConnectionManager(context, l2capManager, gattManager)
            val packetRouter = PacketRouter()
            @Suppress("UNCHECKED_CAST")
            return AirPodsViewModel(connectionManager, packetRouter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
