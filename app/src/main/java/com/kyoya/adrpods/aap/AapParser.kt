package com.kyoya.adrpods.aap

import java.nio.ByteBuffer
import java.nio.ByteOrder

object AapParser {

    private const val HEADER_SIZE = 4
    private const val LENGTH_SIZE = 2
    private const val OPCODE_SIZE = 1

    /**
     * Parses a raw byte array into an [AapPacket].
     *
     * @param bytes The raw byte array received from the BLE connection.
     * @return The parsed [AapPacket], or null if the byte array is malformed.
     */
    fun parse(bytes: ByteArray): AapPacket? {
        if (bytes.size < HEADER_SIZE + LENGTH_SIZE + OPCODE_SIZE) {
            return null
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        val header = ByteArray(HEADER_SIZE)
        buffer.get(header)

        val length = buffer.short

        val opcode = buffer.get()

        val payload = ByteArray(length.toInt())
        buffer.get(payload)

        return AapPacket(header, length, opcode, payload)
    }
}
