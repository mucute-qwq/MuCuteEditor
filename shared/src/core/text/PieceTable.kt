package io.github.mucute.qwq.mucuteeditor.core.text

interface PieceTable {

    val count: Long

    fun open(content: ByteArray)

    fun insert(pos: Long, content: ByteArray)

    fun delete(pos: Long, count: Int)

}