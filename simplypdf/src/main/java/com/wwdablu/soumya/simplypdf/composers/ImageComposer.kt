package com.wwdablu.soumya.simplypdf.composers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import com.bumptech.glide.Glide
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ImageComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private var bitmapPainter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun drawBitmap(bmp: Bitmap, properties: ImageProperties) {
        drawBitmap(bmp, properties, 0, 0, false, 0)
    }

    fun drawFromUrl(url: String, context: Context, properties: ImageProperties) {

        val bitmap  = runBlocking {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()
            }
        }

        drawBitmap(bitmap, properties, 0, 0, false, 0)
    }

    internal fun drawBitmap(bmp: Bitmap, properties: ImageProperties, vMargin:Int, hMargin: Int,
                            isHorizontalDraw: Boolean, hPadding: Int) {

        var bitmap = bmp

        //If recycled, do nothing
        if (bmp.isRecycled) {
            return
        }

        val bmpSpacing = getTopSpacing(DEFAULT_SPACING)
        bitmap = if(bitmap.width >= simplyPdfDocument.usablePageWidth ||
                bitmap.height >= simplyPdfDocument.usablePageHeight) {
            scaleToFit(bmp, simplyPdfDocument.pageContentHeight + bmpSpacing)
        } else {
            bmp
        }

        val xTranslate = alignmentTranslationX(properties.alignment, bitmap.width)
        if (!canFitContentInPage(bitmap.height + DEFAULT_SPACING) &&
            simplyPdfDocument.pageContentHeight != simplyPdfDocument.topMargin) {
            simplyPdfDocument.newPage()
        }

        val canvas = pageCanvas
        canvas.save()
        canvas.translate((if(isHorizontalDraw) 0 else simplyPdfDocument.startMargin) +
                (xTranslate + hPadding + hMargin).toFloat(),
            (if(isHorizontalDraw) 0 else simplyPdfDocument.startMargin) +
                    (simplyPdfDocument.pageContentHeight + vMargin).toFloat())

        canvas.drawBitmap(bitmap, Matrix(), bitmapPainter)
        if(!isHorizontalDraw) {
            simplyPdfDocument.addContentHeight(bitmap.height + bmpSpacing)
        }

        if(bitmap != bmp) {
            bitmap.recycle()
        }
        canvas.restore()
    }

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