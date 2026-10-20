package io.github.mucute.qwq.mucuteeditor.core.text

interface PieceTable {

    val contentCount: Int

    fun open(content: CharSequence)

    fun open(content: ByteArray)

    fun clear()

    fun insert(pos: Int, content: CharSequence)

    fun insert(pos: Int, content: ByteArray)

    fun append(content: CharSequence)

    fun append(content: ByteArray)

    fun deleteAt(pos: Int)

    fun delete(startPos: Int, endPos: Int)

    operator fun get(pos: Int): Byte

    fun getString(startPos: Int, endPos: Int): String

    fun get(startPos: Int, endPos: Int): ByteArray

    fun get(destination: ByteArray, destinationOffset: Int = 0, startPos: Int, endPos: Int)

    fun content(): String

    fun listPiece(): String

}