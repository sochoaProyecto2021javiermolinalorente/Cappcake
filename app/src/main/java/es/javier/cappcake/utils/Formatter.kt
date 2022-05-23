package es.javier.cappcake.presentation.utils

import java.text.DecimalFormat

fun Float.toFormattedString() : String {
    val dec = DecimalFormat("##.##")
    return dec.format(this.toDouble())
}

fun String.toFormattedFloat() : Float {
    val dec = DecimalFormat("##.##")
    return if (this.isBlank()) {
        0f
    } else {
        dec.format(this.toDouble()).toFloat()
    }
}