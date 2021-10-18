package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties

class ShapeComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private var shapePath: Path = Path()

    fun drawBox(width: Float, height: Float, properties: ShapeProperties) {
        drawBox(simplyPdfDocument.startMargin.toFloat(), simplyPdfDocument.pageContentHeight.toFloat(),
            width, height, properties)
    }

    fun drawBox(x:Float, y:Float, width: Float, height: Float, properties: ShapeProperties) {

        shapePath.apply {
            reset()
            addRect(RectF(x, y, width + x, height + y), Path.Direction.CW)
        }

        freeform(shapePath, properties)
    }

    fun drawCircle(radius: Float, properties: ShapeProperties) {

        val correctedX = when(properties.alignment) {
            Alignment.START -> simplyPdfDocument.startMargin.toFloat() + radius
            Alignment.CENTER -> 0
            Alignment.END -> 0
        }

        drawCircle(correctedX.toFloat(), simplyPdfDocument.pageContentHeight.toFloat() + radius,
            radius, properties)
    }

    fun drawCircle(x:Float, y:Float, radius: Float, properties: ShapeProperties) {

        shapePath.apply {
            reset()
            addCircle(x, y, radius, Path.Direction.CW)
        }

        freeform(shapePath, properties)
    }

    fun drawFreeform(path: FreeformPath, properties: ShapeProperties) {
        freeform(path, properties)
    }

    internal fun freeform(path: Path, properties: ShapeProperties) {

        val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor(properties.lineColor)
            style = if (properties.shouldFill) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
            strokeWidth = properties.lineWidth.toFloat()
        }

        val bounds = RectF()
        path.computeBounds(bounds, true)
        if (!canFitContentInPage(bounds.height().toInt() + spacing)) {
            simplyPdfDocument.newPage()
        }

        pageCanvas.save()
        val shapeSpacing = getTopSpacing(DEFAULT_SPACING)
        val xTranslate = alignmentTranslationX(properties.alignment, bounds.width().toInt())
        pageCanvas.translate((simplyPdfDocument.startMargin + xTranslate).toFloat(),
            (shapeSpacing).toFloat())

        pageCanvas.drawPath(path, painter)
        simplyPdfDocument.addContentHeight(bounds.height().toInt() + shapeSpacing)

        pageCanvas.restore()
    }

    class FreeformPath(val simplyPdfDocument: SimplyPdfDocument) : Path() {
        override fun lineTo(x: Float, y: Float) {
            super.lineTo(x, y + simplyPdfDocument.pageContentHeight)
        }

        override fun moveTo(x: Float, y: Float) {
            super.moveTo(x, y + simplyPdfDocument.pageContentHeight)
        }
    }
}