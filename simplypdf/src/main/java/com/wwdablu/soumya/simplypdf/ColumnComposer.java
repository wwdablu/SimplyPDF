package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ColumnComposer extends GroupComposer {

    private Paint borderPainter;
    private Properties colProperties;

    ColumnComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
        this.borderPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.colProperties = new Properties(1, Color.BLACK);
    }

    public void draw(@NonNull List<List<Cell>> cellList) {

        if(cellList.size() == 0) {
            return;
        }

        int largestHeight = 0;
        for(List<Cell> rowCellList : cellList) {

            for(Cell cell : rowCellList) {

                int cellHeight = 0;
                if(cell instanceof TextCell) {
                    cellHeight = simplyPdfDocument.getTextComposer()
                        .write(((TextCell) cell).text, ((TextCell) cell).properties,
                            cell.width, 0, true, 0, false);
                }

                if(largestHeight < cellHeight) {
                    largestHeight = cellHeight;
                }
            }

            if(!canFitContentInPage(largestHeight)) {
                simplyPdfDocument.newPage();
            }

            int bitmapXTranslate = simplyPdfDocument.getLeftMargin();
            int arrayLength = rowCellList.size();
            for(int rowIndex = 0; rowIndex < arrayLength; rowIndex++) {

                Cell cellData = rowCellList.get(rowIndex);
                if(cellData instanceof TextCell) {
                    simplyPdfDocument.getTextComposer()
                        .write(((TextCell) cellData).text, ((TextCell) cellData).properties,
                            cellData.width, 0,
                                true, bitmapXTranslate, true);
                    bitmapXTranslate += cellData.width;
                }
            }

            simplyPdfDocument.addContentHeight(largestHeight);
            drawBorders(getPageCanvas(), largestHeight, rowCellList);
        }
    }

    @Override
    void clean() {
        super.clean();
        borderPainter = null;
    }

    @Override
    String getComposerName() {
        return ColumnComposer.class.getName();
    }

    private void drawBorders(@NonNull Canvas canvas, int maxHeight, List<Cell> rowCellList) {

        canvas.save();
        canvas.translate(simplyPdfDocument.getLeftMargin(), simplyPdfDocument.getPageContentHeight() - maxHeight);
        borderPainter.setColor(colProperties.borderColor);

        int colIndex = 0;
        for(Cell cell : rowCellList) {

            //Left border
            if(colIndex == 0) {
                canvas.drawLine(0, 0, 0, maxHeight, borderPainter);
            }

            //Top border
            canvas.drawLine(0, 0, cell.width, 0, borderPainter);

            //Right border
            canvas.drawLine(cell.width, 0, cell.width, maxHeight, borderPainter);

            //Bottom border
            canvas.drawLine(cell.width, maxHeight, 0, maxHeight, borderPainter);

            ++colIndex;
            canvas.translate(cell.width, 0);
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

    public static abstract class Cell {
        protected int width;
    }

    public static class TextCell extends Cell {

        private String text;
        private TextComposer.Properties properties;

        public TextCell(@NonNull String text, @Nullable TextComposer.Properties properties, int width) {
            this.text = text;
            this.properties = properties;
            this.width = width;
        }
    }
}
