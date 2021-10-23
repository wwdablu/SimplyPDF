package com.wwdablu.soumya.simplypdf.composers

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

abstract class GroupComposer(simplyPdfDocument: SimplyPdfDocument) : Composer(simplyPdfDocument) {

    fun resolveCellWidth(widthPercent: Int): Int {
        return simplyPdfDocument.usablePageWidth / (100 / widthPercent)
    }
}