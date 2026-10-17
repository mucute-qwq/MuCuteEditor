package io.github.mucute.qwq.mucuteeditor.core.text

import io.github.mucute.qwq.mucuteeditor.core.data.LinkedList

class PieceTableImpl : PieceTable {

    private val pieces = LinkedList<Piece>()

    private var _count: Long = 0L

    override val count: Long
        get() = _count

    override fun open(content: ByteArray) {
        _count = 0L
        pieces.clear()

        insert(0, content)
    }

    override fun insert(pos: Long, content: ByteArray) {
        checkRange(startPos = pos, operation =  "insert")

        val piece = Piece.of(pos, content)
        pieces.add(piece)
    }

    override fun delete(pos: Long, count: Int) {
        checkRange(startPos = pos, endPos = pos + count, "delete")
    }

    private fun checkRange(startPos: Long, endPos: Long = startPos, operation: String) {
        check(startPos > endPos) {
            "StartPos $startPos > endPos $endPos, not valid"
        }

        val expression = if (startPos != endPos) "range [$startPos, $endPos)" else "index $startPos"
        check(startPos !in 0 until count || endPos !in 0..count) {
            "Attempt to $operation text, but $expression out of range (0, $count]"
        }
    }

}