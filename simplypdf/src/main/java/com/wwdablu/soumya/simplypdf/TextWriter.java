package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextWriter extends Composer {

    private TextPaint textPaint;

    TextWriter(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    public void write(@NonNull String text, int textSize) {
        write(text, Color.BLACK, textSize, null, Layout.Alignment.ALIGN_NORMAL);
    }

    public void write(@NonNull String text, int textColor, int textSize) {
        write(text, textColor, textSize, null, Layout.Alignment.ALIGN_NORMAL);
    }

    public void write(@NonNull String text, int textColor, int textSize, @Nullable Typeface typeface,
                      @NonNull Layout.Alignment alignment) {

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);

        StaticLayout staticLayout = new StaticLayout(text, textPaint, simplyPdfDocument.getUsablePageWidth(),
            alignment, 1F, 0F, false);

        if(!canFitContentInPage(DEFAULT_SPACING + staticLayout.getHeight())) {
            simplyPdfDocument.newPage();
        }

        Canvas canvas = getPageCanvas();
        canvas.save();

        final int textLineSpacing = (simplyPdfDocument.getPageContentHeight() ==
            simplyPdfDocument.getTopMargin() ? 0 : DEFAULT_SPACING);

        canvas.translate(simplyPdfDocument.getLeftMargin(), simplyPdfDocument.getPageContentHeight() + textLineSpacing);
        simplyPdfDocument.addContentHeight(staticLayout.getHeight() + textLineSpacing);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    void clean() {
        simplyPdfDocument = null;
        textPaint = null;
    }
}
