package io.github.mucute.qwq.mucuteeditor.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
}