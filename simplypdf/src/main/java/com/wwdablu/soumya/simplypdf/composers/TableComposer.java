package com.wwdablu.soumya.simplypdf.composers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell;
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell;

import java.util.List;

public class TableComposer extends GroupComposer {

    private Paint borderPainter;
    private TableProperties colProperties;

    private TextComposer textComposer;

    public TableComposer(@NonNull SimplyPdfDocument simplyPdfDocument) {

        this.simplyPdfDocument = simplyPdfDocument;
        this.borderPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        initProperties();
        initSupportedComposers(simplyPdfDocument);
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
                    cellHeight = textComposer
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
                    textComposer.write(((TextCell) cellData).text, ((TextCell) cellData).properties,
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

    public void setProperties(@Nullable TableProperties properties) {

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
    public String getComposerName() {
        return TableComposer.class.getName();
    }

    private void initProperties() {
        this.colProperties = new TableProperties();
        colProperties.borderColor = "#000000";
        colProperties.borderWidth = 1;
    }

    private void initSupportedComposers(@NonNull SimplyPdfDocument simplyPdfDocument) {
        textComposer = new TextComposer(simplyPdfDocument);
    }

    private void drawBorders(@NonNull Canvas canvas, int maxHeight, List<Cell> rowCellList) {

        canvas.save();
        canvas.translate(simplyPdfDocument.getLeftMargin(), simplyPdfDocument.getPageContentHeight() - maxHeight);
        borderPainter.setColor(Color.parseColor(colProperties.borderColor));

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
}
