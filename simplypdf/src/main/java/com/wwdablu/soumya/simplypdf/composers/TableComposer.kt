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
        var rowIndex = 0
        var rowCount = cellList.size

        cellList.forEach { rowCellList ->

            largestHeight = getLargestCellSize(rowCellList)

            if (!canFitContentInPage(largestHeight)) {
                simplyPdfDocument.newPage()
                simplyPdfDocument.insertEmptyLines(1)
            }

            var bitmapXTranslate = simplyPdfDocument.startMargin
            rowCellList.forEach { cell ->
                cell.apply {
                    render(bitmapXTranslate)
                    bitmapXTranslate += getCellWidth()
                }
            }

            if(properties.drawBorder) {
                drawBorders(pageCanvas, largestHeight.toFloat(), rowIndex, rowCount, rowCellList, properties)
            }

            rowIndex++
            simplyPdfDocument.addContentHeight(largestHeight)
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
                            rowIndex: Int,
                            rowCount: Int,
                            rowCellList: List<Cell>,
                            tableProperties: TableProperties) {

        borderPainter.color = Color.parseColor(tableProperties.borderColor)

        canvas.save()
        canvas.translate(simplyPdfDocument.startMargin.toFloat(),
            simplyPdfDocument.pageContentHeight.toFloat())

        var colIndex: Int = 0

        rowCellList.forEach { cell ->
            //Left border
            if (colIndex == 0 && canDrawLeftBorder(tableProperties, rowIndex, rowCount, colIndex, rowCellList.size)) {
                canvas.drawLine(0f, 0f, 0f, maxHeight, borderPainter)
            }

            //Top border
            if(canDrawTopBorder(tableProperties, rowIndex, rowCount, colIndex, rowCellList.size)) {
                canvas.drawLine(
                    0f, 0f, cell.getCellWidth().toFloat(),
                    0f, borderPainter
                )
            }

            //Right border
            if(canDrawRightBorder(tableProperties, rowIndex, rowCount, colIndex, rowCellList.size)) {
                canvas.drawLine(
                    cell.getCellWidth().toFloat(), 0f,
                    cell.getCellWidth().toFloat(), maxHeight, borderPainter
                )
            }

            //Bottom border
            if(canDrawBottomBorder(tableProperties, rowIndex, rowCount, colIndex, rowCellList.size)) {
                canvas.drawLine(
                    cell.getCellWidth().toFloat(), maxHeight,
                    0f, maxHeight, borderPainter
                )
            }

            colIndex++
            canvas.translate(cell.getCellWidth().toFloat(), 0f)
        }

        canvas.restore()
    }

    private fun canDrawLeftBorder(prop: TableProperties,
                                  rowIndex: Int,
                                  rowCount: Int,
                                  colIndex: Int,
                                  colCount: Int) : Boolean {

        if(prop.borderStyle or TableProperties.BORDER_ALL == TableProperties.BORDER_ALL ||
            prop.borderStyle or TableProperties.BORDER_VERTICAL == TableProperties.BORDER_VERTICAL) {
            return true
        }

        return prop.borderStyle or TableProperties.BORDER_OUTER == TableProperties.BORDER_OUTER &&
                colIndex == 0
    }

    private fun canDrawRightBorder(prop: TableProperties,
                                  rowIndex: Int,
                                  rowCount: Int,
                                  colIndex: Int,
                                  colCount: Int) : Boolean {

        if(prop.borderStyle or TableProperties.BORDER_ALL == TableProperties.BORDER_ALL ||
            prop.borderStyle or TableProperties.BORDER_VERTICAL == TableProperties.BORDER_VERTICAL) {
            return true
        }

        return prop.borderStyle or TableProperties.BORDER_OUTER == TableProperties.BORDER_OUTER &&
                colIndex == colCount - 1
    }

    private fun canDrawTopBorder(prop: TableProperties,
                                 rowIndex: Int,
                                 rowCount: Int,
                                 colIndex: Int,
                                 colCount: Int) : Boolean {

        if(prop.borderStyle or TableProperties.BORDER_ALL == TableProperties.BORDER_ALL ||
            prop.borderStyle or TableProperties.BORDER_HORIZONTAL == TableProperties.BORDER_HORIZONTAL) {
            return true
        }

        return prop.borderStyle or TableProperties.BORDER_OUTER == TableProperties.BORDER_OUTER &&
                rowIndex == 0
    }

    private fun canDrawBottomBorder(prop: TableProperties,
                                 rowIndex: Int,
                                 rowCount: Int,
                                 colIndex: Int,
                                 colCount: Int) : Boolean {

        if(prop.borderStyle or TableProperties.BORDER_ALL == TableProperties.BORDER_ALL ||
            prop.borderStyle or TableProperties.BORDER_HORIZONTAL == TableProperties.BORDER_HORIZONTAL) {
            return true
        }

        return prop.borderStyle or TableProperties.BORDER_OUTER == TableProperties.BORDER_OUTER &&
                rowIndex == rowCount - 1
    }
}