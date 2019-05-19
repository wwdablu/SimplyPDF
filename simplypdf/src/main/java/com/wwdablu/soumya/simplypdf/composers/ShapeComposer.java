package com.wwdablu.soumya.simplypdf.composers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties;

public class ShapeComposer extends UnitComposer {

    private Paint painter;
    private Path shapePath;
    private ShapeProperties properties;

    public ShapeComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePath = new Path();
        properties = new ShapeProperties();
    }

    public void drawBox(int width, int height, @Nullable ShapeProperties properties) {

        shapePath.reset();
        shapePath.addRect(new RectF(0, 0, width, height), Path.Direction.CW);
        freeform(shapePath, properties);
    }

    public void drawCircle(int radius, @Nullable ShapeProperties properties) {

        shapePath.reset();
        shapePath.addCircle(radius, radius, radius, Path.Direction.CW);
        freeform(shapePath, properties);
    }

    public void freeform(@NonNull Path path, @Nullable ShapeProperties properties) {

        ShapeProperties shapeProperties = properties != null ? properties : this.properties;

        painter.setColor(Color.parseColor(shapeProperties.lineColor));
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
}
