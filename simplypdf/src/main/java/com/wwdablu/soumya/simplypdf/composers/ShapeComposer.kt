package com.wwdablu.soumya.simplypdf.composers

import android.graphics.*
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties

class ShapeComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private var shapePath: Path = Path()
    private var properties: ShapeProperties = ShapeProperties()

    fun drawBox(x:Float, y:Float, width: Float, height: Float, properties: ShapeProperties?) {

        shapePath.apply {
            reset()
            addRect(RectF(x, y, width + x, height + y), Path.Direction.CW)
        }

        freeform(x, y, shapePath, properties)
    }

    fun drawCircle(x:Float, y:Float, radius: Float, properties: ShapeProperties?) {

        shapePath.apply {
            reset()
            addCircle(x, y, radius, Path.Direction.CW)
        }

        freeform(x, y, shapePath, properties)
    }

    fun freeform(x:Float, y:Float, path: Path, properties: ShapeProperties?) {
        val shapeProperties = properties ?: this.properties

        val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor(shapeProperties.lineColor)
            style = if (shapeProperties.shouldFill) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
            strokeWidth = shapeProperties.lineWidth.toFloat()
        }

        val bounds = RectF()
        path.computeBounds(bounds, true)
        if (!canFitContentInPage(bounds.height().toInt() + spacing)) {
            simplyPdfDocument.newPage()
        }

        pageCanvas.save()
        val shapeSpacing = getTopSpacing(DEFAULT_SPACING)
        val xTranslate = alignmentCanvasTranslation(shapeProperties.alignment, bounds.width().toInt())
        pageCanvas.translate((simplyPdfDocument.leftMargin + xTranslate).toFloat(),
            (shapeSpacing + simplyPdfDocument.pageContentHeight).toFloat())

        pageCanvas.drawPath(path, painter)
        simplyPdfDocument.addContentHeight(bounds.height().toInt() + shapeSpacing + y.toInt())

        pageCanvas.restore()
    }

    override val composerName: String
        get() = ShapeComposer::class.java.name
}