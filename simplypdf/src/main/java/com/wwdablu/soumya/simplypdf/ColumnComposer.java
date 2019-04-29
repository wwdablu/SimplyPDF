package com.wwdablu.soumya.simplypdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

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

        return simplyPdfDocument.getTextComposer().getComposed(text, properties,
            cellWidth, this.colProperties.borderWidth);
    }

    public void draw(@NonNull List<List<Composed>> composedList) {

        if(composedList.size() == 0) {
            return;
        }

        int largestHeight = composedList.get(0).get(0).getComposedBitmap().getHeight();
        for(List<Composed> rowComposedList : composedList) {

            for(Composed composed : rowComposedList) {

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
            int arrayLength = rowComposedList.size();
            for(int rowIndex = 0; rowIndex < arrayLength; rowIndex++) {

                Composed composed = rowComposedList.get(rowIndex);
                if(rowIndex != 0) {
                    bitmapXTranslate = composed.getComposedBitmap().getWidth();
                }

                canvas.translate(bitmapXTranslate, 0);
                canvas.drawBitmap(composed.getComposedBitmap(), new Matrix(), bitmapPainter);

                //release the internal bitmap
                composed.free();
            }

            simplyPdfDocument.addContentHeight(largestHeight);
            canvas.restore();

            drawBorders(canvas, largestHeight, rowComposedList);
        }
    }

    @Override
    void clean() {
        super.clean();
        bitmapPainter = null;
    }

    private void drawBorders(@NonNull Canvas canvas, int maxHeight, List<Composed> composedList) {

        canvas.save();
        canvas.translate(simplyPdfDocument.getLeftMargin(), simplyPdfDocument.getPageContentHeight() - maxHeight);
        borderPainter.setColor(colProperties.borderColor);

        int colIndex = 0;
        for(Composed composed : composedList) {

            Bitmap composedBitmap = composed.getComposedBitmap();

            //Left border
            if(colIndex == 0) {
                canvas.drawLine(0, 0, 0, maxHeight, borderPainter);
            }

            //Top border
            canvas.drawLine(0, 0, composedBitmap.getWidth(), 0, borderPainter);

            //Right border
            canvas.drawLine(composedBitmap.getWidth(), 0, composedBitmap.getWidth(), maxHeight, borderPainter);

            //Bottom border
            canvas.drawLine(composedBitmap.getWidth(), maxHeight, 0, maxHeight, borderPainter);

            ++colIndex;
            canvas.translate(composedBitmap.getWidth(), 0);
        }

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
