package com.wwdablu.soumya.simplypdf.composers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import com.bumptech.glide.Glide
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ImageComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private var bitmapPainter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun drawBitmap(bmp: Bitmap, properties: ImageProperties) {
        drawBitmap(bmp, properties, 0, 0, 0, null)
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

        drawBitmap(bitmap, properties, 0, 0, 0, null)
    }

    internal fun drawBitmap(bmp: Bitmap,
                            properties: ImageProperties,
                            xMargin: Int,
                            yMargin:Int,
                            xShift: Int,
                            cell: Cell?) {

        //If recycled, do nothing
        if (bmp.isRecycled) {
            return
        }

        var bitmap = bmp
        val bmpSpacing = getTopSpacing(DEFAULT_SPACING)
        val isCellContent: Boolean = cell != null

        bitmap = if(cell == null) {
            scaleIfNeeded(bitmap, simplyPdfDocument.usablePageWidth, simplyPdfDocument.usablePageHeight - simplyPdfDocument.pageContentHeight)
        } else {
            scaleIfNeeded(bitmap, cell.getCellWidth() - (xMargin * 2), cell.getCellHeight())
        }

        val xTranslate = if(cell == null) {
            alignmentTranslationX(properties.alignment, bitmap.width)
        } else {
            cellAlignmentTranslateX(properties.alignment, cell, xMargin)
        }

        if (!canFitContentInPage(bitmap.height + DEFAULT_SPACING) &&
            simplyPdfDocument.pageContentHeight != simplyPdfDocument.topMargin) {
            simplyPdfDocument.newPage()
        }

        val canvas = pageCanvas
        canvas.save()
        canvas.translate((if(isCellContent) 0 else simplyPdfDocument.startMargin) +
                (xTranslate + xShift + xMargin).toFloat(),
            (if(isCellContent) 0 else simplyPdfDocument.startMargin) +
                    (simplyPdfDocument.pageContentHeight + yMargin).toFloat())

        canvas.drawBitmap(bitmap, Matrix(), bitmapPainter)
        if(!isCellContent) {
            simplyPdfDocument.addContentHeight(bitmap.height + bmpSpacing)
        }

        if(bitmap != bmp) {
            bitmap.recycle()
        }
        canvas.restore()
    }

    internal fun getScaledDimension(bitmap: Bitmap, width: Int, height: Int) : Pair<Int, Int> {

        //Check whether the original bitmap is within the bounds needed
        if(bitmap.width <= width && bitmap.height <= height) return Pair(bitmap.width, bitmap.height)

        val widthFactor: Float = bitmap.width.toFloat() / width
        val heightFactor: Float = bitmap.height.toFloat() / height

        val useFactor = widthFactor.coerceAtLeast(heightFactor)

        return Pair((bitmap.width / useFactor).toInt(),
            (bitmap.height / useFactor).toInt())
    }

    private fun scaleIfNeeded(bitmap: Bitmap, width: Int, height: Int) : Bitmap {

        val dimension = getScaledDimension(bitmap, width, height)

        //Check whether scaling is needed or not
        if(bitmap.width <= width && bitmap.height <= height) return bitmap

        return Bitmap.createScaledBitmap(bitmap, dimension.first, dimension.second, true)
    }
}