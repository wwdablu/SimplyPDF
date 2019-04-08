package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public class ShapeComposer extends Composer {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    private Paint painter;
    private Path shapePath;

    ShapeComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePath = new Path();
    }

    public void drawBox(int width, int height, int lineColor, float lineWidth, boolean fill, Alignment alignment) {

        shapePath.reset();
        shapePath.addRect(new RectF(0, 0, width, height), Path.Direction.CW);
        freeform(shapePath, lineColor, lineWidth, fill, alignment);
    }

    public void drawCircle(int radius, int lineColor, float lineWidth, boolean fill, Alignment alignment) {

        shapePath.reset();
        shapePath.addCircle(radius, radius, radius, Path.Direction.CW);
        freeform(shapePath, lineColor, lineWidth, fill, alignment);
    }

    public void freeform(Path path, int lineColor, float lineWidth, boolean fill, Alignment alignment) {

        painter.setColor(lineColor);
        painter.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        painter.setStrokeWidth(lineWidth);

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        if(!canFitContentInPage((int) bounds.height() + DEFAULT_SPACING)) {
            simplyPdfDocument.newPage();
        }

        Canvas canvas = getPageCanvas();
        canvas.save();

        final int shapeSpacing = (simplyPdfDocument.getPageContentHeight() ==
                simplyPdfDocument.getTopMargin() ? 0 : DEFAULT_SPACING);

        canvas.translate(simplyPdfDocument.getLeftMargin(), shapeSpacing + simplyPdfDocument.getPageContentHeight());
        canvas.drawPath(path, painter);
        simplyPdfDocument.addContentHeight((int) bounds.height() + shapeSpacing);
        canvas.restore();
    }

    @Override
    void clean() {
        super.clean();
        shapePath = null;
        painter = null;
    }
}
