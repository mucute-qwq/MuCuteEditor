package io.github.mucute.qwq.mucuteeditor.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import io.github.mucute.qwq.mucuteeditor.render.CodeEditorRenderer

@Composable
fun MuCuteEditor(
    props: MuCuteEditorProps = MuCuteEditorProps(),
    modifier: Modifier = Modifier
) {
    val defaultModifier = Modifier
        .fillMaxSize()

    val renderer = retain { CodeEditorRenderer() }

    Canvas(
        modifier = defaultModifier then modifier
    ) {
        renderer.render()
    }

    LaunchedEffect(Unit) {
        val pieceTable = props.text
        pieceTable.open("Hello")
        pieceTable.append(" ")
        pieceTable.append("World")

        println(pieceTable.listPiece())
        println(pieceTable.content())
    }

}