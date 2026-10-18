package io.github.mucute.qwq.mucuteeditor.core.text

data class Piece(
    var pos: Int,
    var bufferPos: Int,
    var count: Int,
    var type: PieceType,
)