package io.github.mucute.qwq.mucuteeditor.core.text

data class PieceImpl(
    override val pos: Long,
    override val content: ByteArray
) : Piece {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PieceImpl

        if (pos != other.pos) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pos.hashCode()
        result = 31 * result + content.contentHashCode()
        return result
    }

}