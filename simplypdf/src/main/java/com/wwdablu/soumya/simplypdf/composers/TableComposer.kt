package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell

class TableComposer(simplyPdfDocument: SimplyPdfDocument) : GroupComposer(simplyPdfDocument) {

    var tableProperties: TableProperties = TableProperties().apply {
        borderColor = "#000000"
        borderWidth = 1
    }

    private val borderPainter = Paint(Paint.ANTI_ALIAS_FLAG)

    fun draw(cellList: List<List<Cell>>) {
        if (cellList.isEmpty()) {
            return
        }
        var largestHeight: Int
        for (rowCellList in cellList) {

            largestHeight = 0

            /* This will loop through all the cells on the row and then find the cell with the
             * largest height. That will be used to draw all the other cells in the same row
             */
            for (cell in rowCellList) {
                val cellHeight = cell.getCellHeight()

                if (largestHeight < cellHeight) {
                    largestHeight = cellHeight
                }
            }
            if (!canFitContentInPage(largestHeight)) {
                simplyPdfDocument.newPage()
            }
            var bitmapXTranslate = simplyPdfDocument.leftMargin
            val arrayLength = rowCellList.size
            for (rowIndex in 0 until arrayLength) {
                rowCellList[rowIndex].apply {
                    render(bitmapXTranslate)
                    bitmapXTranslate += getCellWidth()
                }
            }
            simplyPdfDocument.addContentHeight(largestHeight)
            drawBorders(pageCanvas, largestHeight.toFloat(), rowCellList)
        }
    }

    private fun drawBorders(canvas: Canvas, maxHeight: Float, rowCellList: List<Cell>) {

        borderPainter.color = Color.parseColor(tableProperties.borderColor)

        canvas.save()
        canvas.translate(simplyPdfDocument.leftMargin.toFloat(),
            (simplyPdfDocument.pageContentHeight - maxHeight))

        for ((colIndex, cell) in rowCellList.withIndex()) {

            //Left border
            if (colIndex == 0) {
                canvas.drawLine(0f, 0f, 0f, maxHeight, borderPainter)
            }

            //Top border
            canvas.drawLine(0f, 0f, cell.getCellWidth().toFloat(),
                0f, borderPainter)

            //Right border
            canvas.drawLine(cell.getCellWidth().toFloat(), 0f,
                cell.getCellWidth().toFloat(), maxHeight, borderPainter)

            //Bottom border
            canvas.drawLine(cell.getCellWidth().toFloat(), maxHeight,
                0f, maxHeight, borderPainter)

            canvas.translate(cell.getCellWidth().toFloat(), 0f)
        }

        canvas.restore()
    }
}