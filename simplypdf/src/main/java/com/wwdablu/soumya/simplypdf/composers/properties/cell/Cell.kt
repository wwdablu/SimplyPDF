package com.wwdablu.soumya.simplypdf.composers.properties.cell

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

abstract class Cell internal constructor(
    internal val width: Int
) {

    protected lateinit var simplyPdfDocument: SimplyPdfDocument

    var xMargin = 10
    var yMargin = 10

    abstract fun getCellWidth() : Int
    abstract fun getCellHeight() : Int
    abstract fun getContentWidth() : Int
    abstract fun render(xTranslate: Int) : Boolean

    internal fun setDocument(simplyPdfDocument: SimplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument
    }

    internal fun isDocumentSet() : Boolean = this::simplyPdfDocument.isInitialized

    companion object {
        const val MATCH_PARENT: Int = Int.MAX_VALUE
    }
}