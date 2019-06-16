package com.wwdablu.soumya.simplypdf.composers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties;

public class ImageComposer extends UnitComposer {

    private ImageProperties properties;
    private Paint bitmapPainter;

    public ImageComposer(SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        this.properties = new ImageProperties();
        this.bitmapPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void drawBitmap(@NonNull Bitmap bitmap, @Nullable ImageProperties properties) {

        //If recycled, hence nothing to do
        if(bitmap.isRecycled()) {
            return;
        }

        ImageProperties bitmapProperties = properties != null ? properties : this.properties;
        int xTranslate = alignmentCanvasTranslation(bitmapProperties.alignment, bitmap.getWidth());

        if(!canFitContentInPage(bitmap.getHeight() + DEFAULT_SPACING)) {
            simplyPdfDocument.newPage();
        }

        final int bmpSpacing = getTopSpacing(DEFAULT_SPACING);

        Canvas canvas = getPageCanvas();
        canvas.save();
        canvas.translate(simplyPdfDocument.getLeftMargin() + xTranslate, simplyPdfDocument.getPageContentHeight() + bmpSpacing);
        Bitmap scaledBitmap = scaleToFit(bitmap, simplyPdfDocument.getPageContentHeight() + bmpSpacing);
        canvas.drawBitmap(scaledBitmap, new Matrix(), bitmapPainter);
        simplyPdfDocument.addContentHeight(scaledBitmap.getHeight() + bmpSpacing);
        scaledBitmap.recycle();
        canvas.restore();
    }

    @Override
    void clean() {
        super.clean();
        properties = null;
        bitmapPainter = null;
    }

    @Override
    public String getComposerName() {
        return ImageComposer.class.getName();
    }

    private Bitmap scaleToFit(@NonNull Bitmap bitmap, int topSpacing) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        int useableWidth = simplyPdfDocument.getUsablePageWidth();
        int useableHeight = (simplyPdfDocument.getUsablePageHeight() - topSpacing);

        float heightFactor = (float)originalHeight / (float)useableHeight;
        float widthFactor = (float)originalWidth / (float)useableWidth;
        float useFactor = heightFactor;

        if(widthFactor >= heightFactor) {
            useFactor = widthFactor;
        }

        int scaleWidth = (int)(originalWidth / useFactor);
        int scaleHeight = (int)(originalHeight / useFactor);

        return Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true);
    }
}
