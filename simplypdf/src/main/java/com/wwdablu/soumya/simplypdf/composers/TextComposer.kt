package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell

const val BULLET_SPACING = 10

class TextComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    override fun getTypeHandler(): String = "text"

    /**
     * Draws text on the canvas with the provided params
     *
     * @param text Text to draw
     * @param properties TextProperties to use
     */
    fun write(text: String, properties: TextProperties) {
        write(text, properties, simplyPdfDocument.usablePageWidth, 0,
            0, 0, null, true)
    }

    /**
     * Draws text on the canvas with the provided params
     *
     * @param text Text to draw
     * @param properties TextProperties to use
     * @param pageWidth Width to consider when rendering the text
     * @param xMargin Margin to be provided on the X-axis on both the sides
     * @param yMargin Margin to be provided on the Y-axis on both the sides
     */
    fun write(text: String,
                       properties: TextProperties,
                       pageWidth: Int,
                       xMargin: Int,
                       yMargin: Int) {

        write(text, properties, pageWidth, xMargin, yMargin, 0, null, true)
    }

    /**
     * Draws text on the canvas with the provided params
     *
     * @param text Text to draw
     * @param properties TextProperties to use
     * @param pageWidth Width to consider when rendering the text
     * @param xMargin Margin to be provided on the X-axis on both the sides
     * @param yMargin Margin to be provided on the Y-axis on both the sides
     * @param cell Cell object within which the text is rendered. Can be null.
     * @param performDraw Whether actual draw will be called
     */
    internal fun write(text: String,
              properties: TextProperties,
              pageWidth: Int,
              xMargin: Int,
              yMargin: Int,
              xShift: Int,
              cell: Cell?,
              performDraw: Boolean) : Int {

        textPaint.apply {
            color = Color.parseColor(properties.textColor)
            textSize = properties.textSize.toFloat()
            typeface = properties.typeface
        }

        var widthAdjustForProperties = 0
        var bulletMarker: StaticLayout? = null
        val isCellContent: Boolean = cell != null

        if (properties.isBullet) {
            bulletMarker = StaticLayout(
                properties.getBulletSymbol(), textPaint, simplyPdfDocument.usablePageWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
            )
            widthAdjustForProperties += (textPaint.measureText(properties.bulletSymbol) + BULLET_SPACING).toInt()
        }

        val staticLayout = StaticLayout(text, textPaint,
            pageWidth - widthAdjustForProperties - xMargin * 2,
            properties.getAlignment(), 1f, 0f, false)

        val textLineSpacing = getTopSpacing(if (isCellContent) 0 else DEFAULT_SPACING)
        if (performDraw && !canFitContentInPage(textLineSpacing + staticLayout.height)) {
            simplyPdfDocument.newPage()
        }

        pageCanvas.save()
        pageCanvas.translate(
            (if (isCellContent) xShift + xMargin else xShift + xMargin + simplyPdfDocument.startMargin
                .toFloat()).toFloat(), (
                    yMargin + simplyPdfDocument.pageContentHeight + textLineSpacing).toFloat())

        if (performDraw && bulletMarker != null) {
            bulletMarker.draw(pageCanvas)
        }

        setTextPaintProperties(textPaint, Paint.UNDERLINE_TEXT_FLAG, properties.underline)
        setTextPaintProperties(textPaint, Paint.STRIKE_THRU_TEXT_FLAG, properties.strikethrough)

        pageCanvas.translate(widthAdjustForProperties.toFloat(), 0f)

        val finalContentHeight = staticLayout.height + textLineSpacing + yMargin * 2
        if (performDraw) {
            if (!isCellContent) {
                simplyPdfDocument.addContentHeight(finalContentHeight)
            }
            staticLayout.draw(pageCanvas)
        }
        pageCanvas.restore()

        //After every write remove the flags. Will be set again for the next write call
        setTextPaintProperties(textPaint, Paint.UNDERLINE_TEXT_FLAG, false)
        setTextPaintProperties(textPaint, Paint.STRIKE_THRU_TEXT_FLAG, false)

        return finalContentHeight
    }

    private fun setTextPaintProperties(textPaint: TextPaint, flag: Int, enable: Boolean) {
        if (enable) {
            textPaint.flags = textPaint.flags or flag
        } else if (textPaint.flags and flag == flag) {
            textPaint.flags = textPaint.flags xor flag
        }
    }
}