package com.wwdablu.soumya.simplypdf.composers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;

public class TextComposer extends UnitComposer {

    private final int BULLET_SPACING = 10;

    private TextPaint textPaint;
    private TextProperties properties;

    public TextComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        properties = new TextProperties();
    }

    public void write(@NonNull String text, @Nullable TextProperties properties) {
        write(text, properties, simplyPdfDocument.getUsablePageWidth(), 0, false, 0, 0, true);
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
    int write(@NonNull String text, @Nullable TextProperties properties, int pageWidth, int padding,
              boolean isHorizontalDraw, int hAxis, int hPadding, boolean performDraw) {

        final TextProperties textProperties = properties == null ? this.properties : properties;

        textPaint.setColor(Color.parseColor(textProperties.textColor));
        textPaint.setTextSize(textProperties.textSize);
        textPaint.setTypeface(textProperties.typeface);

        int widthAdjustForProperties = 0;

        StaticLayout bulletMarker = null;
        if(textProperties.isBullet) {
            bulletMarker = new StaticLayout(textProperties.getBulletSymbol(), textPaint,
                    simplyPdfDocument.getUsablePageWidth(), Layout.Alignment.ALIGN_NORMAL,
                    1F, 0F, false);
            widthAdjustForProperties += (textPaint.measureText(textProperties.bulletSymbol) + BULLET_SPACING);
        }

        final StaticLayout staticLayout = new StaticLayout(text, textPaint,
                (pageWidth - widthAdjustForProperties) - (hPadding * 2),
                textProperties.getAlignment(), 1F, 0F, false);

        final int textLineSpacing = getTopSpacing(isHorizontalDraw ? 0 : DEFAULT_SPACING);
        if(performDraw && !canFitContentInPage(textLineSpacing + staticLayout.getHeight())) {
            simplyPdfDocument.newPage();
        }

        Canvas canvas = getPageCanvas();
        canvas.save();

        canvas.translate(isHorizontalDraw ? hAxis + hPadding : hAxis + hPadding + simplyPdfDocument.getLeftMargin(),
            padding + simplyPdfDocument.getPageContentHeight() + textLineSpacing);

        if(performDraw && bulletMarker != null) {
            bulletMarker.draw(canvas);
        }

        setTextPaintProperties(Paint.UNDERLINE_TEXT_FLAG, textProperties.underline);
        setTextPaintProperties(Paint.STRIKE_THRU_TEXT_FLAG, textProperties.strikethrough);

        canvas.translate(widthAdjustForProperties, 0);

        int finalContentHeight = staticLayout.getHeight() + textLineSpacing + (padding * 2);

        if(performDraw) {

            if(!isHorizontalDraw) {
                simplyPdfDocument.addContentHeight(finalContentHeight);
            }
            staticLayout.draw(canvas);
        }

        canvas.restore();

        //After every write remove the flags. Will be set again for the next write call
        setTextPaintProperties(Paint.UNDERLINE_TEXT_FLAG, false);
        setTextPaintProperties(Paint.STRIKE_THRU_TEXT_FLAG, false);

        return finalContentHeight;
    }

    @Override
    void clean() {
        super.clean();
        textPaint = null;
        properties = null;
    }

    @Override
    public String getComposerName() {
        return TextComposer.class.getName();
    }

    private void setTextPaintProperties(int flag, boolean enable) {

        if(enable) {
            textPaint.setFlags(textPaint.getFlags() | flag);
        } else if((textPaint.getFlags() & flag) == flag) {
            textPaint.setFlags(textPaint.getFlags() ^ flag);
        }
    }
}
