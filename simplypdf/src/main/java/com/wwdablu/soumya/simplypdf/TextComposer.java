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

public class TextComposer extends Composer {

    private final int BULLET_SPACING = 10;

    private TextPaint textPaint;
    private Properties properties;

    TextComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        properties = new Properties();
    }

    public void write(@NonNull String text, @Nullable Properties properties) {

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
                simplyPdfDocument.getUsablePageWidth() - widthAdjustForProperties,
                textProperties.getAlignment(), 1F, 0F, false);

        if(!canFitContentInPage(DEFAULT_SPACING + staticLayout.getHeight())) {
            simplyPdfDocument.newPage();
        }

        Canvas canvas = getPageCanvas();
        canvas.save();

        final int textLineSpacing = (simplyPdfDocument.getPageContentHeight() ==
            simplyPdfDocument.getTopMargin() ? 0 : DEFAULT_SPACING);

        canvas.translate(simplyPdfDocument.getLeftMargin(), simplyPdfDocument.getPageContentHeight() + textLineSpacing);
        if(bulletMarker != null) {
            bulletMarker.draw(canvas);
        }

        setTextPaintProperties(Paint.UNDERLINE_TEXT_FLAG, textProperties.underline);
        setTextPaintProperties(Paint.STRIKE_THRU_TEXT_FLAG, textProperties.strikethrough);

        canvas.translate(widthAdjustForProperties, 0);
        simplyPdfDocument.addContentHeight(staticLayout.getHeight() + textLineSpacing);
        staticLayout.draw(canvas);
        canvas.restore();

        //After every write remove the flags. Will be set again for the next write call
        setTextPaintProperties(Paint.UNDERLINE_TEXT_FLAG, false);
        setTextPaintProperties(Paint.STRIKE_THRU_TEXT_FLAG, false);
    }

    @Override
    void clean() {
        super.clean();
        textPaint = null;
    }

    private void setTextPaintProperties(int flag, boolean enable) {

        if(enable) {
            textPaint.setFlags(textPaint.getFlags() | flag);
        } else if((textPaint.getFlags() & flag) == flag) {
            textPaint.setFlags(textPaint.getFlags() ^ flag);
        }
    }

    public static class Properties {

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
    }
}
