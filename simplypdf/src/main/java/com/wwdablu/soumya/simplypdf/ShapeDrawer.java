package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;

public class ShapeDrawer {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    private PdfDocument pdfDocument;
    private Paint painter;
    private Path shapePath;

    ShapeDrawer(@NonNull PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePath = new Path();
    }

    public void drawBox(int width, int height, int lineColor, float lineWidth, boolean fill, Alignment alignment) {

        shapePath.reset();
        shapePath.moveTo(0, 0);
        shapePath.lineTo(0, width);
        shapePath.lineTo(width, height);
        shapePath.lineTo(0, height);
        shapePath.lineTo(0, 0);
        shapePath.close();
        freeform(shapePath, lineColor, lineWidth, fill, alignment);
    }

    public void drawCircle(int radius, int lineColor, float lineWidth, boolean fill, Alignment alignment) {

        shapePath.reset();
        shapePath.addCircle(radius, radius, radius, Path.Direction.CW);
        shapePath.close();
        freeform(shapePath, lineColor, lineWidth, fill, alignment);
    }

    public void freeform(Path path, int lineColor, float lineWidth, boolean fill, Alignment alignment) {

        painter.setColor(lineColor);
        painter.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        painter.setStrokeWidth(lineWidth);

        Canvas canvas = pdfDocument.getCurrentPage().getCanvas();
        canvas.save();
        canvas.drawPath(path, painter);
        canvas.restore();
    }
}
