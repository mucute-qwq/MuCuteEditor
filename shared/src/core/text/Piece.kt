package io.github.mucute.qwq.mucuteeditor.core.text

interface Piece {

    val pos: Long

    val content: ByteArray

    val count: Int
        get() = content.size

    companion object {

        fun of(pos: Long, content: ByteArray) = PieceImpl(
            pos = pos,
            content = content
        )

        fun of(content: ByteArray) = of(0, content)

        fun of() = of(ByteArray(0))

    }

}