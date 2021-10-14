package com.wwdablu.soumya.simplypdf.composers

import android.graphics.*
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties

class ShapeComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private var shapePath: Path = Path()

    fun drawBox(x:Float, y:Float, width: Float, height: Float, properties: ShapeProperties) {

        shapePath.apply {
            reset()
            addRect(RectF(x, y, width + x, height + y), Path.Direction.CW)
        }

        draw(x, y, shapePath, properties)
    }

    fun drawCircle(x:Float, y:Float, radius: Float, properties: ShapeProperties) {

        shapePath.apply {
            reset()
            addCircle(x, y, radius, Path.Direction.CW)
        }

        draw(x, y, shapePath, properties)
    }

    fun freeform(path: Path, properties: ShapeProperties) {
        draw(0f, 0f, path, properties)
    }

    private fun draw(x:Float, y:Float, path: Path, properties: ShapeProperties) {

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
        val xTranslate = alignmentCanvasTranslation(properties.alignment, bounds.width().toInt())
        pageCanvas.translate((simplyPdfDocument.leftMargin + xTranslate).toFloat(),
            (shapeSpacing + simplyPdfDocument.pageContentHeight).toFloat())

        pageCanvas.drawPath(path, painter)
        simplyPdfDocument.addContentHeight(bounds.height().toInt() + shapeSpacing + y.toInt())

        pageCanvas.restore()
    }
}