package es.javier.cappcake.utils

import java.text.DecimalFormat

/**
 * Method to format a Float to string
 *
 * @return The formated Float as String
 */
fun Float.toFormattedString() : String {
    val dec = DecimalFormat("##.##")
    return dec.format(this.toDouble())
}

/**
 * Method to format a String to Float
 *
 * @return The formated String as Float
 */
fun String.toFormattedFloat() : Float {
    val dec = DecimalFormat("##.##")
    return if (this.isBlank()) {
        0f
    } else {
        dec.format(this.toDouble()).toFloat()
    }
}