package com.wwdablu.soumya.simplypdf.composers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;

public class ShapeComposer extends UnitComposer {

    private Paint painter;
    private Path shapePath;
    private Properties properties;

    public ShapeComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePath = new Path();
        properties = new Properties();
    }

    public void drawBox(int width, int height, @Nullable Properties properties) {

        shapePath.reset();
        shapePath.addRect(new RectF(0, 0, width, height), Path.Direction.CW);
        freeform(shapePath, properties);
    }

    public void drawCircle(int radius, @Nullable Properties properties) {

        shapePath.reset();
        shapePath.addCircle(radius, radius, radius, Path.Direction.CW);
        freeform(shapePath, properties);
    }

    public void freeform(@NonNull Path path, @Nullable Properties properties) {

        Properties shapeProperties = properties != null ? properties : this.properties;

        painter.setColor(shapeProperties.lineColor);
        painter.setStyle(shapeProperties.shouldFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        painter.setStrokeWidth(shapeProperties.lineWidth);

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        if(!canFitContentInPage((int) bounds.height() + getSpacing())) {
            simplyPdfDocument.newPage();
        }

        Canvas canvas = getPageCanvas();
        canvas.save();

        final int shapeSpacing = getTopSpacing(DEFAULT_SPACING);
        int xTranslate = alignmentCanvasTranslation(shapeProperties.alignment, (int) bounds.width());

        canvas.translate(simplyPdfDocument.getLeftMargin() + xTranslate, shapeSpacing + simplyPdfDocument.getPageContentHeight());
        canvas.drawPath(path, painter);
        simplyPdfDocument.addContentHeight((int) bounds.height() + shapeSpacing);
        canvas.restore();
    }

    @Override
    void clean() {
        super.clean();
        shapePath = null;
        painter = null;
        properties = null;
    }

    @Override
    public String getComposerName() {
        return ShapeComposer.class.getName();
    }

    public static class Properties {
        public int lineColor;
        public int lineWidth;
        public boolean shouldFill;
        public Alignment alignment;

        public Properties() {
            lineColor = Color.BLACK;
            lineWidth = 1;
            shouldFill = false;
            alignment = Alignment.LEFT;
        }
    }
}
