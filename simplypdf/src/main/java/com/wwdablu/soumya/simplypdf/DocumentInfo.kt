package com.wwdablu.soumya.simplypdf

import android.print.PrintAttributes

class DocumentInfo internal constructor() {
    enum class ColorMode {
        MONO, COLOR
    }

    enum class Margins {
        NONE, NARROW, DEFAULT
    }

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    var paperSize = PrintAttributes.MediaSize.ISO_A4
     get() {
         return if (orientation == Orientation.PORTRAIT) field else field.asLandscape()
     }

    var colorMode: ColorMode? = ColorMode.COLOR
    var margins: Margins? = Margins.DEFAULT
    var orientation = Orientation.PORTRAIT

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
            else -> PrintAttributes.COLOR_MODE_MONOCHROME
        }
    }

    fun resolveMargin(): PrintAttributes.Margins {
        return when (margins) {
            Margins.NONE -> PrintAttributes.Margins(
                0,
                0,
                0,
                0
            )
            Margins.NARROW -> PrintAttributes.Margins(
                10,
                10,
                10,
                10
            )
            Margins.DEFAULT -> PrintAttributes.Margins(
                20,
                15,
                20,
                15
            )
            else -> PrintAttributes.Margins(20, 15, 20, 15)
        }
    }
}