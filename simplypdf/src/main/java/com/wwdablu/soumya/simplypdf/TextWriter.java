package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.print.PrintAttributes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextWriter {

    private TextPaint textPaint;
    private PdfDocument pdfDocument;

    private int actualPageWidth;

    TextWriter(@NonNull PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
        PrintAttributes printAttributes = pdfDocument.getPrintAttributes();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        actualPageWidth = pdfDocument.getPdfDocument().getPageWidth();
        if(printAttributes.getMinMargins() != null) {
            actualPageWidth -= (printAttributes.getMinMargins().getLeftMils() +
                printAttributes.getMinMargins().getRightMils());
        }
    }

    public void write(@NonNull String text, int textSize) {
        write(text, Color.BLACK, textSize, null);
    }

    public void write(@NonNull String text, int textColor, int textSize, @Nullable Typeface typeface) {

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);

        StaticLayout staticLayout = new StaticLayout(text, textPaint, actualPageWidth,
            Layout.Alignment.ALIGN_NORMAL, 1F, 0F, false);

        Canvas canvas = pdfDocument.getCurrentPage().getCanvas();
        if(pdfDocument.getPageHeight() >= (pdfDocument.getPageContentHeight() + staticLayout.getHeight())) {
            pdfDocument.newPage();
        }

        canvas.save();
        pdfDocument.addPageContentHeight(staticLayout.getHeight());
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
