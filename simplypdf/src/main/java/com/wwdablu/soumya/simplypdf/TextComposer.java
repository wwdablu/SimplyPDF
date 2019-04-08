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
        if(textProperties.isBullet()) {
            bulletMarker = new StaticLayout(textProperties.bulletSymbol, textPaint,
                    simplyPdfDocument.getUsablePageWidth(), Layout.Alignment.ALIGN_NORMAL,
                    1F, 0F, false);
            widthAdjustForProperties += (textPaint.measureText(textProperties.bulletSymbol) + BULLET_SPACING);
        }

        final StaticLayout staticLayout = new StaticLayout(text, textPaint,
                simplyPdfDocument.getUsablePageWidth() - widthAdjustForProperties,
                textProperties.alignment, 1F, 0F, false);

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

        canvas.translate(widthAdjustForProperties, 0);
        simplyPdfDocument.addContentHeight(staticLayout.getHeight() + textLineSpacing);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    void clean() {
        super.clean();
        textPaint = null;
    }

    public static class Properties {

        private int textColor;
        private int textSize;
        private Typeface typeface;
        private Layout.Alignment alignment;
        private boolean bullet;
        private int indentSpace;
        private String bulletSymbol;

        public Properties() {
            textColor = Color.BLACK;
            textSize = 10;
            typeface = null;
            alignment = Layout.Alignment.ALIGN_NORMAL;
            bullet = false;
            indentSpace = 0;
            bulletSymbol = "";
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public Typeface getTypeface() {
            return typeface;
        }

        public void setTypeface(@Nullable Typeface typeface) {
            this.typeface = typeface;
        }

        public Layout.Alignment getAlignment() {
            return alignment;
        }

        public void setAlignment(@Nullable Layout.Alignment alignment) {

            if(alignment == null) {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            }

            this.alignment = alignment;
        }

        public boolean isBullet() {
            return bullet;
        }

        public void setBullet(boolean bullet) {
            this.bullet = bullet;
        }

        public int getIndentSpace() {
            return indentSpace;
        }

        public void setIndentSpace(int indentSpace) throws UnsupportedOperationException {
            this.indentSpace = indentSpace;
            throw new UnsupportedOperationException("This feature has not yet been supported.");
        }

        public String getBulletSymbol() {
            return bulletSymbol;
        }

        public void setBulletSymbol(@Nullable String bulletSymbol) {

            if(bulletSymbol == null || TextUtils.isEmpty(bulletSymbol)) {
                this.bulletSymbol = "";
                return;
            }

            if(bulletSymbol.length() >= 2) {
                bulletSymbol = bulletSymbol.substring(0, 1);
            }

            this.bulletSymbol = bulletSymbol;
        }
    }
}
