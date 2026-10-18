package io.github.mucute.qwq.mucuteeditor.core.text

import io.github.mucute.qwq.mucuteeditor.util.ByteArrayUtil

class PieceTableImpl : PieceTable {

    companion object {

        const val DEFAULT_BUFFER_SIZE = 1024

    }

    private var origin = ByteArray(0)

    private var buffer = ByteArray(DEFAULT_BUFFER_SIZE)

    private val pieces = ArrayList<Piece>()

    private var bufferCount: Int = 0

    private val bufferRemaining: Int
        get() = buffer.size - bufferCount

    private var _contentCount = 0

    override val contentCount: Int
        get() = _contentCount

    override fun open(content: CharSequence) =
        open(ByteArray(content.length) { content[it].code.toByte() })

    override fun open(content: ByteArray) {
        origin = content
        pieces.clear()
        bufferCount = 0
        _contentCount = content.size

        pieces.add(Piece(0, 0, content.size, PieceType.Origin))
    }

    override fun clear() {
        origin = ByteArray(0)
        pieces.clear()
        bufferCount = 0
        _contentCount = 0

        pieces.clear()
    }

    override fun insert(pos: Int, content: CharSequence) =
        insert(pos, ByteArray(content.length) { content[it].code.toByte() })

    override fun insert(pos: Int, content: ByteArray) {
        checkPos(pos, allowEqualsCount = true)
        if (pieces.isEmpty()) {
            open(content)
            return
        }

        val iterator = pieces.listIterator()
        var piece: Piece? = null
        while (iterator.hasNext()) {
            val currentPiece = iterator.next()
            val range = currentPiece.pos until currentPiece.pos + currentPiece.count
            if (pos !in range) {
                continue
            }

            piece = currentPiece
            break
        }

        if (piece == null) {
            appendContent(content)
            iterator.add(Piece(pos, bufferCount - content.size, content.size, PieceType.Append))
            return
        }

        if (piece.pos == pos) {
            appendContent(content)
            var index = iterator.previousIndex()
            pieces.add(
                index,
                Piece(pos, bufferCount - content.size, content.size, PieceType.Append)
            )

            index++
            while (index < pieces.size) {
                pieces[index].pos += content.size
                index++
            }
            return
        }

        appendContent(content)
        var index = iterator.nextIndex()
        val leftCount = pos - piece.pos
        val rightCount = piece.count - leftCount
        println(rightCount)
        piece.count = leftCount
        pieces.add(
            index,
            Piece(pos, bufferCount - content.size, content.size, PieceType.Append)
        )
        index++
        pieces.add(
            index,
            Piece(pos + content.size, piece.bufferPos + leftCount, rightCount, piece.type)
        )

        index++
        while (index < pieces.size) {
            pieces[index].pos += content.size
            index++
        }
    }

    override fun append(content: CharSequence) =
        append(ByteArray(content.length) { content[it].code.toByte() })

    override fun append(content: ByteArray) = insert(contentCount, content)

    override fun delete(startPos: Int, endPos: Int) {
        checkPos(startPos)
        checkPos(endPos, allowEqualsCount = true)


    }

    override fun get(pos: Int): Byte {
        checkPos(pos)

        for ((piecePos, bufferPos, count, type) in pieces) {
            val range = piecePos until piecePos + count
            if (pos !in range) {
                continue
            }

            val offset = pos - piecePos
            return when (type) {
                PieceType.Origin -> origin[bufferPos + offset]
                PieceType.Append -> buffer[bufferPos + offset]
            }
        }

        error("Unreachable code")
    }

    override fun getString(startPos: Int, endPos: Int) = get(startPos, endPos).decodeToString()

    override fun get(startPos: Int, endPos: Int): ByteArray {
        check(startPos < endPos) {
            "Start index $startPos >= end index $endPos"
        }
        checkPos(startPos)
        checkPos(endPos, allowEqualsCount = true)

        val content = ByteArray(endPos - startPos)
        get(content, 0, startPos, endPos)
        return content
    }

    override fun get(destination: ByteArray, destinationOffset: Int, startPos: Int, endPos: Int) {
        check(destinationOffset in destination.indices) {
            "Destination offset $destinationOffset out of range [0, ${destination.size})"
        }
        check(startPos < endPos) {
            "Start index $startPos >= end index $endPos"
        }
        checkPos(startPos)
        checkPos(endPos, allowEqualsCount = true)

        var offset = destinationOffset
        for ((piecePos, bufferPos, count, type) in pieces) {
            val source = when (type) {
                PieceType.Origin -> origin
                PieceType.Append -> buffer
            }

            if (startPos >= piecePos && endPos <= piecePos + count) {
                val startIndex = bufferPos + (startPos - piecePos)
                val endIndex = startIndex + (endPos - startPos)
                if (startIndex < endIndex) {
                    ByteArrayUtil.arrayCopy(source, destination, offset, startIndex, endIndex)
                }
                return
            }

            if (startPos >= piecePos && endPos > piecePos + count) {
                val startIndex = bufferPos + (startPos - piecePos)
                val endIndex = bufferPos + count
                if (startIndex < endIndex) {
                    ByteArrayUtil.arrayCopy(source, destination, offset, startIndex, endIndex)
                    offset += endIndex - startIndex
                }
                continue
            }

            if (startPos <= piecePos && endPos <= piecePos + count) {
                val startIndex = bufferPos
                val endIndex = bufferPos + (endPos - piecePos)
                if (startIndex < endIndex) {
                    ByteArrayUtil.arrayCopy(source, destination, offset, startIndex, endIndex)
                    offset += endIndex - startIndex
                }
                continue
            }

            if (startPos <= piecePos && endPos > piecePos + count) {
                val startIndex = bufferPos
                val endIndex = bufferPos + count
                if (startIndex < endIndex) {
                    ByteArrayUtil.arrayCopy(source, destination, offset, startIndex, endIndex)
                    offset += endIndex - startIndex
                }
                continue
            }

        }

    }

    override fun content() = getString(0, contentCount)

    private fun appendContent(content: ByteArray) {
        if (content.size <= bufferRemaining) {
            ByteArrayUtil.arrayCopy(content, buffer, bufferCount)
            bufferCount += content.size
            _contentCount += content.size
            return
        }

        val newBuffer = ByteArray(buffer.size + content.size + DEFAULT_BUFFER_SIZE)
        ByteArrayUtil.arrayCopy(buffer, newBuffer)
        ByteArrayUtil.arrayCopy(content, newBuffer, bufferCount)
        bufferCount += content.size
        _contentCount += content.size
        buffer = newBuffer
    }

    private fun checkPos(pos: Int, allowEqualsCount: Boolean = false) {
        val range = if (allowEqualsCount) 0..contentCount else 0 until contentCount
        val expression = if (allowEqualsCount) "[0, $contentCount]" else "[0, $contentCount)"
        check(pos in range) {
            "Position $pos out of range $expression"
        }
    }

    override fun listPiece() = buildString {
        append("PieceTable(bufferTotalCount=${buffer.size}, bufferCount=${bufferCount}, bufferRemaining=$bufferRemaining, contentCount=$contentCount, pieces=")
        append("[")
        appendLine()

        for ((index, piece) in pieces.withIndex()) {
            val pos = piece.pos
            val bufferPos = piece.bufferPos
            val count = piece.count
            val type = piece.type
            val content = when (piece.type) {
                PieceType.Origin -> origin.decodeToString(bufferPos, bufferPos + count)
                PieceType.Append -> buffer.decodeToString(bufferPos, bufferPos + count)
            }
            append("Piece(pos=${pos}, bufferPos=${bufferPos}, count=${count}, type=${type}, content=${content})")
            if (index < pieces.size - 1) {
                append(", ")
            }
            appendLine()
        }

        append("]")
        append(")")
    }

}