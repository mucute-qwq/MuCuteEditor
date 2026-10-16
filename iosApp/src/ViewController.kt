package io.github.mucute.qwq.mucuteeditor

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.ComposeUIViewController
import io.github.mucute.qwq.mucuteeditor.editor.MuCuteEditor

fun ViewController() = ComposeUIViewController {
    MaterialTheme {
        MuCuteEditor()
    }
}
