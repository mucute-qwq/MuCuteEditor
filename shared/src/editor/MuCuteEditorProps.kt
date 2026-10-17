package io.github.mucute.qwq.mucuteeditor.editor

import androidx.compose.runtime.Immutable
import io.github.mucute.qwq.mucuteeditor.core.text.PieceTable
import io.github.mucute.qwq.mucuteeditor.core.text.PieceTableImpl

@Immutable
data class MuCuteEditorProps(
    val text: PieceTable = PieceTableImpl(),
)