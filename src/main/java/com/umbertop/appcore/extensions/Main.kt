package com.umbertop.appcore.extensions

val <T> T.exhaustive: T
    get() = this

/**
 * Returns a full 6 digit hex color from a 3 digit hex color.
 */
fun String.shortHexToFullHexColor(): String {
    if (this[0] != '#') {
        throw Exception("This is not a valid color. It must start with a \'#\' character.")
    }

    if (this.length > 4) return this

    var tempColor = "#"
    this.substring(1).forEach { tempColor += "$it$it" }

    return tempColor
}