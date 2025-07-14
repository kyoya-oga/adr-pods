package com.kyoya.adrpods.aap

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PacketRouter {

    // Example state for battery levels
    private val _batteryLevelLeft = MutableStateFlow<Int?>(null)
    val batteryLevelLeft: StateFlow<Int?> = _batteryLevelLeft

    private val _batteryLevelRight = MutableStateFlow<Int?>(null)
    val batteryLevelRight: StateFlow<Int?> = _batteryLevelRight

    private val _batteryLevelCase = MutableStateFlow<Int?>(null)
    val batteryLevelCase: StateFlow<Int?> = _batteryLevelCase

    /**
     * Routes an incoming [AapPacket] to the appropriate handler based on its opcode.
     *
     * @param packet The packet to route.
     */
    fun route(packet: AapPacket) {
        when (packet.opcode) {
            // Example: Opcode for battery status (replace with actual opcode)
            0x07.toByte() -> handleBatteryStatus(packet.payload)
            // TODO: Add more handlers for other opcodes (e.g., noise control).
        }
    }

    private fun handleBatteryStatus(payload: ByteArray) {
        // TODO: Implement the actual logic to parse the battery status payload.
        // This is a placeholder implementation.
        if (payload.size >= 3) {
            _batteryLevelLeft.value = payload[0].toInt()
            _batteryLevelRight.value = payload[1].toInt()
            _batteryLevelCase.value = payload[2].toInt()
        }
    }
}
