package io.github.mucute.qwq.mucuteeditor.util

object ByteArrayUtil {

    fun arrayCopy(
        source: ByteArray,
        destination: ByteArray,
        destinationOffset: Int = 0,
        startIndex: Int = 0,
        endIndex: Int = source.size
    ) {
        check(destinationOffset in destination.indices) {
            "Destination offset $destinationOffset out of range [0, ${destination.size})"
        }
        check(startIndex < endIndex) {
            "Start index $startIndex >= end index $endIndex"
        }
        check(startIndex in source.indices) {
            "Start index out of range [0, ${source.size})"
        }
        check(endIndex in 0..source.size) {
            "End index out of range [0, ${source.size}]"
        }

        var destinationIndex = destinationOffset
        for (sourceIndex in startIndex until endIndex) {
            destination[destinationIndex] = source[sourceIndex]
            destinationIndex++
        }
    }

}