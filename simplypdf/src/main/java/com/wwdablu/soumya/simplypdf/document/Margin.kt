package com.wwdablu.soumya.simplypdf.document

import android.print.PrintAttributes
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

class Margin(
    val start: UInt,
    val top: UInt,
    val end: UInt,
    val bottom: UInt) {

    internal lateinit var simplyPdfDocument: SimplyPdfDocument

    internal fun setDocument(simplyPdfDocument: SimplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument
    }

    internal fun getMargin() : PrintAttributes.Margins {
        return PrintAttributes.Margins(start.toInt(), top.toInt(), end.toInt(), bottom.toInt())
    }

    companion object {
        val default by lazy { Margin(20U, 15U, 20U, 15U) }
        val none by lazy { Margin(0U, 0U, 0U, 0U) }
    }
}