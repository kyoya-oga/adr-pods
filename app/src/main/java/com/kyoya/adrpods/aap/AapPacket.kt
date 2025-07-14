package com.kyoya.adrpods.aap

/**
 * Represents an Apple Accessory Protocol (AAP) packet.
 *
 * @property header The 4-byte header of the packet.
 * @property length The length of the payload in bytes (Little Endian).
 * @property opcode The operation code that identifies the command.
 * @property payload The data associated with the command.
 */
data class AapPacket(
    val header: ByteArray, // 4 bytes
    val length: Short,     // 2 bytes (LE)
    val opcode: Byte,      // 1 byte
    val payload: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AapPacket

        if (!header.contentEquals(other.header)) return false
        if (length != other.length) return false
        if (opcode != other.opcode) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = header.contentHashCode()
        result = 31 * result + length
        result = 31 * result + opcode
        result = 31 * result + payload.contentHashCode()
        return result
    }
}
