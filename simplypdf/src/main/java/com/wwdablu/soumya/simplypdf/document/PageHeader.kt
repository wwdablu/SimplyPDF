package com.wwdablu.soumya.simplypdf.document

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell

class PageHeader(private val entries: List<Cell>) {

    internal fun render(simplyPdfDocument: SimplyPdfDocument) {

        entries.forEach {
            it.apply {
                setDocument(simplyPdfDocument)
                xMargin = 0
                yMargin = 2
                render(0)
                simplyPdfDocument.addContentHeight(getCellHeight())
            }
        }
    }
}