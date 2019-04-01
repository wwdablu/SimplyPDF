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

public class TextWriter {

    private static final int TEXT_LINE_SPACING = 10;

    private TextPaint textPaint;
    private PdfDocument pdfDocument;

    TextWriter(@NonNull PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
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

        StaticLayout staticLayout = new StaticLayout(text, textPaint, pdfDocument.getUsablePageWidth(),
            alignment, 1F, 0F, false);

        if(pdfDocument.getUsablePageHeight() < (pdfDocument.getPageContentHeight() + TEXT_LINE_SPACING
                + staticLayout.getHeight())) {
            pdfDocument.newPage();
        }

        Canvas canvas = pdfDocument.getCurrentPage().getCanvas();
        canvas.save();

        final int textLineSpacing = (pdfDocument.getPageContentHeight() ==
            pdfDocument.getTopMargin() ? 0 : TEXT_LINE_SPACING);

        canvas.translate(pdfDocument.getLeftMargin(), pdfDocument.getPageContentHeight() + textLineSpacing);
        pdfDocument.addPageContentHeight(staticLayout.getHeight() + textLineSpacing);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    void clean() {
        pdfDocument = null;
        textPaint = null;
    }
}
