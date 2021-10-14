package com.wwdablu.soumya.simplypdf.composers.models.cell

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

abstract class Cell internal constructor(
    var simplyPdfDocument: SimplyPdfDocument
) {

    var horizontalPadding = 10
    var verticalPadding = 10

    abstract fun getCellWidth() : Int
    abstract fun getCellHeight() : Int
    abstract fun render(xTranslate: Int) : Boolean
}