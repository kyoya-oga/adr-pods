package com.kyoya.adrpods.aap

import java.nio.ByteBuffer
import java.nio.ByteOrder

object AapBuilder {

    private val DEFAULT_HEADER = byteArrayOf(0x2a.toByte(), 0xff.toByte(), 0x00.toByte(), 0x00.toByte())

    /**
     * Builds a raw byte array from an opcode and payload for sending over BLE.
     *
     * @param opcode The operation code for the command.
     * @param payload The data associated with the command.
     * @return The raw byte array representation of the AAP packet.
     */
    fun build(opcode: Byte, payload: ByteArray): ByteArray {
        val length = payload.size.toShort()
        val buffer = ByteBuffer.allocate(DEFAULT_HEADER.size + 2 + 1 + payload.size)
            .order(ByteOrder.LITTLE_ENDIAN)

        buffer.put(DEFAULT_HEADER)
        buffer.putShort(length)
        buffer.put(opcode)
        buffer.put(payload)

        return buffer.array()
    }
}
