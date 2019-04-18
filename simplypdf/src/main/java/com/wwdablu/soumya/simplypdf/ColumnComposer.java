package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ColumnComposer extends GroupComposer {

    private Paint bitmapPainter;
    private Properties colProperties;

    ColumnComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        this.bitmapPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.colProperties = new Properties(1, Color.BLACK);
    }

    public Composed addTextCell(@NonNull String text, @Nullable TextComposer.Properties properties, int cellWidth) {

        return simplyPdfDocument.getTextComposer().getComposed(text, properties,
            cellWidth - (2 * this.colProperties.borderWidth));
    }

    public void draw(@NonNull Composed... composedArray) {

        if(composedArray.length == 0) {
            return;
        }

        int largestHeight = composedArray[0].getComposedBitmap().getHeight();
        for(Composed composed : composedArray) {
            if(largestHeight < composed.getComposedBitmap().getHeight()) {
                largestHeight = composed.getComposedBitmap().getHeight();
            }
        }

        if(!canFitContentInPage(largestHeight)) {
            simplyPdfDocument.newPage();
        }

        Canvas canvas = getPageCanvas();
        canvas.save();

        //Translate and fix the Y-axis
        canvas.translate(0, simplyPdfDocument.getPageContentHeight());

        int bitmapXTranslate = simplyPdfDocument.getLeftMargin() - composedArray[0].getComposedBitmap().getWidth();
        for(Composed composed : composedArray) {

            bitmapXTranslate += composed.getComposedBitmap().getWidth();
            canvas.translate(bitmapXTranslate, 0);
            canvas.drawBitmap(composed.getComposedBitmap(), new Matrix(), bitmapPainter);
            composed.free();
        }

        simplyPdfDocument.addContentHeight(largestHeight);
        canvas.restore();
    }

    @Override
    void clean() {
        super.clean();
        bitmapPainter = null;
    }

    public static class Properties {

        private int borderWidth;
        private int borderColor;

        public Properties(int borderWidth, int borderColor) {

            this.borderWidth = borderWidth;
            this.borderColor = borderColor;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public int getBorderColor() {
            return borderColor;
        }
    }
}
