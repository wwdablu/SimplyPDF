package com.wwdablu.soumya.simplypdf.document

import android.print.PrintAttributes

class Margin(
    private val start: UInt,
    private val top: UInt,
    private val end: UInt,
    private val bottom: UInt) {

    internal fun getMargin() : PrintAttributes.Margins {
        return PrintAttributes.Margins(start.toInt(), top.toInt(), end.toInt(), bottom.toInt())
    }

    companion object {
        val default by lazy { Margin(20U, 15U, 20U, 15U) }
        val none by lazy { Margin(0U, 0U, 0U, 0U) }
    }
}