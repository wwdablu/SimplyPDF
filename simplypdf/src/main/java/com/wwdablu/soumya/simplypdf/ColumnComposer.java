package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;

public class ColumnComposer extends Composer {

    private Paint bitmapPainter;

    ColumnComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        this.bitmapPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
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

        int bitmapXTranslate = simplyPdfDocument.getLeftMargin() - composedArray[0].getComposedBitmap().getWidth();
        for(Composed composed : composedArray) {

            bitmapXTranslate += composed.getComposedBitmap().getWidth();
            canvas.translate(bitmapXTranslate,0);
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
}
