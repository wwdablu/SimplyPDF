package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell

class TableComposer(simplyPdfDocument: SimplyPdfDocument) : GroupComposer(simplyPdfDocument) {

    private val borderPainter = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun getTypeHandler(): String = "table"

    fun draw(cellList: List<List<Cell>>, properties: TableProperties) {

        if (cellList.isEmpty()) {
            return
        }

        var largestHeight: Int
        cellList.forEach { rowCellList ->

            largestHeight = getLargestCellSize(rowCellList)

            if (!canFitContentInPage(largestHeight)) {
                simplyPdfDocument.newPage()
            }

            var bitmapXTranslate = simplyPdfDocument.startMargin
            rowCellList.forEach { cell ->
                cell.apply {
                    render(bitmapXTranslate)
                    bitmapXTranslate += getCellWidth()
                }
            }

            simplyPdfDocument.addContentHeight(largestHeight)

            if(properties.drawBorder) {
                drawBorders(pageCanvas, largestHeight.toFloat(), rowCellList, properties)
            }
        }
    }

    private fun getLargestCellSize(rowCellList: List<Cell>) : Int {

        var largestHeight = 0
        rowCellList.forEach {
            it.setDocument(simplyPdfDocument)
            largestHeight = largestHeight.coerceAtLeast(it.getCellHeight())
        }
        return largestHeight
    }

    private fun drawBorders(canvas: Canvas,
                            maxHeight: Float,
                            rowCellList: List<Cell>,
                            tableProperties: TableProperties) {

        borderPainter.color = Color.parseColor(tableProperties.borderColor)

        canvas.save()
        canvas.translate(simplyPdfDocument.startMargin.toFloat(),
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