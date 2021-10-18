package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Canvas
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument

const val DEFAULT_SPACING = 10

abstract class Composer(protected val simplyPdfDocument: SimplyPdfDocument) {

    enum class Alignment {
        START, CENTER, END
    }

    val composerName: String = this::class.java.simpleName

    var spacing: Int = DEFAULT_SPACING
        set(spacing) {
            if (spacing <= -1) field = DEFAULT_SPACING
        }

    protected fun insertEmptyLine() {
        simplyPdfDocument.addContentHeight(spacing)
    }

    protected val pageCanvas: Canvas
        get() = simplyPdfDocument.currentPage.canvas

    protected fun canFitContentInPage(contentHeight: Int): Boolean {
        return simplyPdfDocument.usablePageHeight >=
                contentHeight + simplyPdfDocument.pageContentHeight
    }

    protected fun getTopSpacing(intendedSpace: Int): Int {
        return if (simplyPdfDocument.pageContentHeight == simplyPdfDocument.topMargin) 0 else intendedSpace
    }

    protected fun alignmentTranslationX(alignment: Alignment = Alignment.START, width: Int): Int {
        return when (alignment) {
            Alignment.CENTER -> (simplyPdfDocument.usablePageWidth - width) / 2
            Alignment.END -> simplyPdfDocument.usablePageWidth - width
            Alignment.START -> 0
        }
    }

    open fun clean() {
        //
    }
}