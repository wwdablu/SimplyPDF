package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties

const val BULLET_SPACING = 10

class TextComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    fun write(text: String, properties: TextProperties) {
        write(text, properties, simplyPdfDocument.usablePageWidth, 0,
            false, 0, 0, true)
    }

    /**
     * Draws text on the canvas with the provided params
     * @param text Text to draw
     * @param properties TextProperties
     * @param pageWidth Width of the page
     * @param padding Vertical padding (added to the top only)
     * @param isHorizontalDraw Is it being drawn in a cell side by side.
     * @param hAxis The location in x-axis where text will be drawn
     * @param hPadding Horizontal padding (added to the start only)
     * @param performDraw Should it actually perform the draw on canvas.
     * @return The height of the content that can be drawn.
     */
    fun write(text: String, properties: TextProperties, pageWidth: Int, padding: Int,
        isHorizontalDraw: Boolean, hAxis: Int, hPadding: Int, performDraw: Boolean) : Int {

        textPaint.apply {
            color = Color.parseColor(properties.textColor)
            textSize = properties.textSize.toFloat()
            typeface = properties.typeface
        }

        var widthAdjustForProperties = 0
        var bulletMarker: StaticLayout? = null

        if (properties.isBullet) {
            bulletMarker = StaticLayout(
                properties.getBulletSymbol(), textPaint, simplyPdfDocument.usablePageWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
            )
            widthAdjustForProperties += (textPaint.measureText(properties.bulletSymbol) + BULLET_SPACING).toInt()
        }

        val staticLayout = StaticLayout(text, textPaint,
            pageWidth - widthAdjustForProperties - hPadding * 2,
            properties.getAlignment(), 1f, 0f, false)

        val textLineSpacing = getTopSpacing(if (isHorizontalDraw) 0 else DEFAULT_SPACING)
        if (performDraw && !canFitContentInPage(textLineSpacing + staticLayout.height)) {
            simplyPdfDocument.newPage()
        }

        pageCanvas.save()
        pageCanvas.translate(
            (if (isHorizontalDraw) hAxis + hPadding else hAxis + hPadding + simplyPdfDocument.leftMargin
                .toFloat()).toFloat(), (
                    padding + simplyPdfDocument.pageContentHeight + textLineSpacing).toFloat())

        if (performDraw && bulletMarker != null) {
            bulletMarker.draw(pageCanvas)
        }

        setTextPaintProperties(textPaint, Paint.UNDERLINE_TEXT_FLAG, properties.underline)
        setTextPaintProperties(textPaint, Paint.STRIKE_THRU_TEXT_FLAG, properties.strikethrough)

        pageCanvas.translate(widthAdjustForProperties.toFloat(), 0f)

        val finalContentHeight = staticLayout.height + textLineSpacing + padding * 2
        if (performDraw) {
            if (!isHorizontalDraw) {
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