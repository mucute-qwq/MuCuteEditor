package io.github.mucute.qwq.mucuteeditor

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.ComposeUIViewController

fun ViewController() = ComposeUIViewController {
    MaterialTheme {
        MuCuteEditor()
    }
}
