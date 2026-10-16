package io.github.mucute.qwq.mucuteeditor

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.mucute.qwq.mucuteeditor.editor.MuCuteEditor

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "MuCuteEditor") {
        MaterialTheme {
            MuCuteEditor()
        }
    }
}
