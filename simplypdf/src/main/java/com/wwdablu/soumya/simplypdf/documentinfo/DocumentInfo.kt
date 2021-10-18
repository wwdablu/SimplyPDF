package com.wwdablu.soumya.simplypdf.documentinfo

import android.print.PrintAttributes

class DocumentInfo internal constructor() {
    enum class ColorMode {
        MONO, COLOR
    }

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    var paperSize: PrintAttributes.MediaSize = PrintAttributes.MediaSize.ISO_A4
        get() {
            return if (orientation == Orientation.PORTRAIT) field else field.asLandscape()
        }

    var colorMode: ColorMode = ColorMode.COLOR
    var margins: Margin = Margin.default
    var orientation: Orientation = Orientation.PORTRAIT

    /*
     *
     * --- STOP ---
     *     ANYTHING BELOW IS MAINTAINED INTERNALLY BY SIMPLY PDF AND IS NOT EXPOSED
     * --- STOP ---
     *
     */
    fun resolveColorMode(): Int {
        return when (colorMode) {
            ColorMode.COLOR -> PrintAttributes.COLOR_MODE_COLOR
            ColorMode.MONO -> PrintAttributes.COLOR_MODE_MONOCHROME
        }
    }
}