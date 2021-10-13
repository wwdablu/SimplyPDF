package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties

open class ImageComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private var properties: ImageProperties = ImageProperties()
    private var bitmapPainter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun drawBitmap(bitmap: Bitmap, properties: ImageProperties?) {

        //If recycled, hence nothing to do
        if (bitmap.isRecycled) {
            return
        }

        val bitmapProperties = properties ?: this.properties
        val bmpSpacing = getTopSpacing(DEFAULT_SPACING)
        val scaledBitmap = scaleToFit(bitmap, simplyPdfDocument.pageContentHeight + bmpSpacing)
        val xTranslate = alignmentCanvasTranslation(bitmapProperties.alignment, scaledBitmap.width)
        if (!canFitContentInPage(scaledBitmap.height + DEFAULT_SPACING) &&
            simplyPdfDocument.pageContentHeight != simplyPdfDocument.topMargin
        ) {
            simplyPdfDocument.newPage()
        }
        val canvas = pageCanvas
        canvas.save()
        canvas.translate(
            (simplyPdfDocument.leftMargin + xTranslate).toFloat(),
            (simplyPdfDocument.pageContentHeight + bmpSpacing).toFloat()
        )
        canvas.drawBitmap(scaledBitmap, Matrix(), bitmapPainter)
        simplyPdfDocument.addContentHeight(scaledBitmap.height + bmpSpacing)
        scaledBitmap.recycle()
        canvas.restore()
    }

    override val composerName: String
        get() = ImageComposer::class.java.name

    private fun scaleToFit(bitmap: Bitmap, topSpacing: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val useableWidth = simplyPdfDocument.usablePageWidth
        val useableHeight = simplyPdfDocument.usablePageHeight - topSpacing
        val heightFactor = originalHeight.toFloat() / useableHeight.toFloat()
        val widthFactor = originalWidth.toFloat() / useableWidth.toFloat()
        var useFactor = heightFactor
        if (widthFactor >= heightFactor) {
            useFactor = widthFactor
        }
        val scaleWidth = (originalWidth / useFactor).toInt()
        val scaleHeight = (originalHeight / useFactor).toInt()
        return Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true)
    }
}