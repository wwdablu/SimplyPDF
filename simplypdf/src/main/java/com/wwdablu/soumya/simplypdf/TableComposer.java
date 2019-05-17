package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TableComposer extends GroupComposer {

    private Paint borderPainter;
    private Properties colProperties;

    TableComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {

        this.simplyPdfDocument = simplyPdfDocument;
        this.borderPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        initProperties();
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
                            cell.width, cell.verticalPadding, true, 0, cell.horizontalPadding, false);
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
                            cellData.width, cellData.verticalPadding,
                                true, bitmapXTranslate, cellData.horizontalPadding, true);
                    bitmapXTranslate += (cellData.width);
                }
            }

            simplyPdfDocument.addContentHeight(largestHeight);
            drawBorders(getPageCanvas(), largestHeight, rowCellList);
            largestHeight = 0;
        }
    }

    public void setProperties(@Nullable Properties properties) {

        if(properties != null) {
            this.colProperties = properties;
        } else {
            initProperties();
        }
    }

    @Override
    void clean() {
        super.clean();
        borderPainter = null;
    }

    @Override
    String getComposerName() {
        return TableComposer.class.getName();
    }

    private void initProperties() {
        this.colProperties = new Properties(1, Color.BLACK);
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
        int width;
        int horizontalPadding;
        int verticalPadding;

        Cell() {
            horizontalPadding = 10;
            verticalPadding = 10;
        }

        public int getHorizontalPadding() {
            return horizontalPadding;
        }

        public void setHorizontalPadding(int horizontalPadding) {
            this.horizontalPadding = horizontalPadding;
        }

        public int getVerticalPadding() {
            return verticalPadding;
        }

        public void setVerticalPadding(int verticalPadding) {
            this.verticalPadding = verticalPadding;
        }
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
