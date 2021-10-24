package com.wwdablu.soumya.simplypdf.composers.properties.cell

import android.graphics.Bitmap
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties

class ImageCell(private val bitmap: Bitmap,
                private val properties: ImageProperties,
                width: Int) : Cell(width) {

    override fun getCellHeight(): Int {
        return if(!isDocumentSet()) {
            0
        } else {
            simplyPdfDocument.image.getScaledDimension(bitmap, width, bitmap.height).second + (yMargin * 2)
        }
    }

    override fun getCellWidth() = if(!isDocumentSet()) 0
        else if (width == MATCH_PARENT) simplyPdfDocument.usablePageWidth
        else width

    override fun getContentWidth(): Int = bitmap.width

    override fun render(xTranslate: Int): Boolean {
        if(!isDocumentSet()) return false
        simplyPdfDocument.image.drawBitmap(bitmap, properties, yMargin, xMargin,
            xTranslate, this)
        return true
    }
}