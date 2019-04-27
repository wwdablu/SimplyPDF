package com.wwdablu.soumya.simplypdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ColumnComposer extends GroupComposer {

    private Paint bitmapPainter;
    private Paint borderPainter;
    private Properties colProperties;

    ColumnComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        this.bitmapPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.borderPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.colProperties = new Properties(1, Color.BLACK);
    }

    @Nullable
    public Composed addTextCell(@NonNull String text, @Nullable TextComposer.Properties properties, int cellWidth) {

        Composed composed = simplyPdfDocument.getTextComposer().getComposed(text, properties,
            cellWidth, this.colProperties.borderWidth);
        drawBorders(composed);
        return composed;
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

        int bitmapXTranslate = simplyPdfDocument.getLeftMargin();
        for(int index = 0; index < composedArray.length; index++) {

            Composed composed = composedArray[index];
            if(index != 0) {
                bitmapXTranslate = composed.getComposedBitmap().getWidth();
            }

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

    private void drawBorders(@Nullable Composed composed) {

        if(composed == null) {
            return;
        }

        Bitmap composedBitmap = composed.getComposedBitmap();
        Canvas canvas = new Canvas(composedBitmap);
        canvas.save();
        borderPainter.setColor(colProperties.borderColor);
        canvas.drawLine(0, 0, 0, composedBitmap.getHeight(), borderPainter);
        canvas.drawLine(0, 0, composedBitmap.getWidth(), 0, borderPainter);
        canvas.drawLine(composedBitmap.getWidth(), 0, composedBitmap.getWidth(), composedBitmap.getHeight(), borderPainter);
        canvas.drawLine(composedBitmap.getWidth(), composedBitmap.getHeight(), 0, composedBitmap.getHeight(), borderPainter);
        canvas.restore();
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
