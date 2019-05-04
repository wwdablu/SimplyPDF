package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextComposer extends UnitComposer {

    private final int BULLET_SPACING = 10;

    private TextPaint textPaint;
    private Properties properties;

    TextComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        properties = new Properties();
    }

    public int write(@NonNull String text, @Nullable Properties properties) {

        return write(text, properties, simplyPdfDocument.getUsablePageWidth(), 0, false, 0, 0, true);
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
    int write(@NonNull String text, @Nullable Properties properties, int pageWidth, int padding,
              boolean isHorizontalDraw, int hAxis, int hPadding, boolean performDraw) {

        final Properties textProperties = properties == null ? this.properties : properties;

        textPaint.setColor(textProperties.textColor);
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
                pageWidth - widthAdjustForProperties + hPadding,
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

        int finalContentHeight = staticLayout.getHeight() + textLineSpacing + padding;

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
    String getComposerName() {
        return TextComposer.class.getName();
    }

    private void setTextPaintProperties(int flag, boolean enable) {

        if(enable) {
            textPaint.setFlags(textPaint.getFlags() | flag);
        } else if((textPaint.getFlags() & flag) == flag) {
            textPaint.setFlags(textPaint.getFlags() ^ flag);
        }
    }

    public static class Properties extends UnitComposer.Properties {

        public int textColor;
        public int textSize;
        public Typeface typeface;
        public Layout.Alignment alignment;
        public boolean isBullet;
        public String bulletSymbol;
        public boolean underline;
        public boolean strikethrough;

        public Properties() {
            textColor = Color.BLACK;
            textSize = 10;
            typeface = null;
            alignment = Layout.Alignment.ALIGN_NORMAL;
            isBullet = false;
            bulletSymbol = "";
            underline = false;
            strikethrough = false;
        }

        Layout.Alignment getAlignment() {

            if(alignment == null) {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            }

            return this.alignment;
        }

        String getBulletSymbol() {

            if(bulletSymbol == null || TextUtils.isEmpty(bulletSymbol)) {
                this.bulletSymbol = "";
                return "";
            }

            if(bulletSymbol.length() >= 2) {
                bulletSymbol = bulletSymbol.substring(0, 1);
            }

            return this.bulletSymbol;
        }

        @Override
        public String getPropId() {
            return TextComposer.class.getName() + "Properties";
        }
    }
}
