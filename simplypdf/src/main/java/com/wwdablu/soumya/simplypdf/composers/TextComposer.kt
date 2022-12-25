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
const val BOTTOM_SPACING = 5

class TextComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

    override fun getTypeHandler(): String = "text"

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
              pageWidth: Int = simplyPdfDocument.usablePageWidth,
              xMargin: Int = 0,
              yMargin: Int = 0) {

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

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor(properties.textColor)
            textSize = properties.textSize.toFloat()
            typeface = properties.typeface
        }

        var widthAdjustForProperties = 0
        var bulletMarker: StaticLayout? = null
        val isCellContent: Boolean = cell != null

        if (properties.bulletSymbol?.let { it.isNotBlank() && it.isNotEmpty() } == true) {
            bulletMarker = StaticLayout.Builder.obtain(properties.getBulletSymbol(), 0, 0, textPaint,
                simplyPdfDocument.usablePageWidth)
                .setText(properties.getBulletSymbol())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setIncludePad(false)
                .build()

            widthAdjustForProperties += (textPaint.measureText(properties.bulletSymbol) + BULLET_SPACING).toInt()
        }

        val staticLayout = StaticLayout.Builder.obtain(text, 0, 0, textPaint,
            pageWidth - widthAdjustForProperties - (xMargin * 2))
            .setText(text)
            .setAlignment(properties.alignment ?: Layout.Alignment.ALIGN_NORMAL)
            .setIncludePad(false)
            .build()

        val textLineSpacing = getTopSpacing(if (isCellContent) 0 else DEFAULT_SPACING)
        if (performDraw && !canFitContentInPage(textLineSpacing + staticLayout.height)) {
            simplyPdfDocument.newPage()
            simplyPdfDocument.insertEmptyLines(1)
        }

        pageCanvas.save()
        pageCanvas.translate(if(isCellContent) (xShift + xMargin).toFloat() else
            (xShift + xMargin).toFloat() + simplyPdfDocument.startMargin,
            (if(isCellContent) yMargin.toFloat()/2f else yMargin.toFloat()) +
                    (simplyPdfDocument.pageContentHeight + textLineSpacing).toFloat())

        if (performDraw && bulletMarker != null) {
            bulletMarker.draw(pageCanvas)
        }

        setTextPaintProperties(textPaint, Paint.UNDERLINE_TEXT_FLAG, properties.underline)
        setTextPaintProperties(textPaint, Paint.STRIKE_THRU_TEXT_FLAG, properties.strikethrough)

        pageCanvas.translate(widthAdjustForProperties.toFloat(), 0f)

        val finalContentHeight = staticLayout.height + textLineSpacing + yMargin
        if (performDraw) {
            staticLayout.draw(pageCanvas)
        }
        pageCanvas.restore()

        if(performDraw && !isCellContent) {
            simplyPdfDocument.addContentHeight(finalContentHeight + BOTTOM_SPACING)
        }

        //After every write remove the flags. Will be set again for the next write call
        setTextPaintProperties(textPaint, Paint.UNDERLINE_TEXT_FLAG, false)
        setTextPaintProperties(textPaint, Paint.STRIKE_THRU_TEXT_FLAG, false)

        return finalContentHeight
    }

    /**
     * Draws text on the canvas with the provided params
     *
     * @param text Text to draw
     * @param properties TextProperties to use
     * @param pageWidth Width to consider when rendering the text
     * @param xShift offset from the start of the page on the X-axis
     * @param yShift offset from the top of the page on the Y-axis
     */
    fun writeAtPosition(text: String,
                        properties: TextProperties,
                        pageWidth: Int = simplyPdfDocument.usablePageWidth,
                        xShift: Int = 0,
                        yShift: Int = 0
    ) : Int {

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor(properties.textColor)
            textSize = properties.textSize.toFloat()
            typeface = properties.typeface
        }

        val staticLayout = StaticLayout.Builder.obtain(text, 0, 0, textPaint, pageWidth)
            .setText(text)
            .setAlignment(properties.alignment ?: Layout.Alignment.ALIGN_NORMAL)
            .setIncludePad(false)
            .build()

        pageCanvas.save()
        pageCanvas.translate(xShift.toFloat(), yShift.toFloat())

        setTextPaintProperties(textPaint, Paint.UNDERLINE_TEXT_FLAG, properties.underline)
        setTextPaintProperties(textPaint, Paint.STRIKE_THRU_TEXT_FLAG, properties.strikethrough)

        staticLayout.draw(pageCanvas)
        pageCanvas.restore()
        return staticLayout.height
    }

    private fun setTextPaintProperties(textPaint: TextPaint, flag: Int, enable: Boolean) {
        if (enable) {
            textPaint.flags = textPaint.flags or flag
        } else if (textPaint.flags and flag == flag) {
            textPaint.flags = textPaint.flags xor flag
        }
    }
}