package com.wwdablu.soumya.simplypdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageComposer extends UnitComposer {

    private Properties properties;
    private Paint bitmapPainter;

    ImageComposer(SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        this.properties = new Properties();
        this.bitmapPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void drawBitmap(@NonNull Bitmap bitmap, @Nullable Properties properties) {

        //If recycled, hence nothing to do
        if(bitmap.isRecycled()) {
            return;
        }

        Properties bitmapProperties = properties != null ? properties : this.properties;
        int xTranslate = alignmentCanvasTranslation(bitmapProperties.alignment, bitmap.getWidth());

        if(!canFitContentInPage(bitmap.getHeight() + DEFAULT_SPACING)) {
            simplyPdfDocument.newPage();
        }

        final int bmpSpacing = getTopSpacing(DEFAULT_SPACING);

        Canvas canvas = getPageCanvas();
        canvas.save();
        canvas.translate(simplyPdfDocument.getLeftMargin() + xTranslate, simplyPdfDocument.getPageContentHeight() + bmpSpacing);
        canvas.drawBitmap(bitmap, new Matrix(), bitmapPainter);
        simplyPdfDocument.addContentHeight(bitmap.getHeight() + bmpSpacing);
        canvas.restore();
    }

    @Override
    void clean() {
        super.clean();
        properties = null;
        bitmapPainter = null;
    }

    public static class Properties {

        public Alignment alignment;

        public Properties() {
            alignment = Alignment.LEFT;
        }
    }
}
