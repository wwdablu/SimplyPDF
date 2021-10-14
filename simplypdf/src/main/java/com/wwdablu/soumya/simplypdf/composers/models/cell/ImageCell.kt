package com.wwdablu.soumya.simplypdf.composers.models.cell

import android.graphics.Bitmap
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties

class ImageCell(val bitmap: Bitmap,
                simplyPdfDocument: SimplyPdfDocument,
                val properties: ImageProperties) : Cell(simplyPdfDocument) {

    override fun getCellHeight(): Int {
        return bitmap.height + (verticalPadding * 2)
    }

    override fun getCellWidth(): Int {
        return bitmap.width + (horizontalPadding * 2)
    }

    override fun render(xTranslate: Int): Boolean {
        simplyPdfDocument.image.drawBitmap(bitmap, properties, verticalPadding, horizontalPadding,
            true, xTranslate)
        return true
    }
}