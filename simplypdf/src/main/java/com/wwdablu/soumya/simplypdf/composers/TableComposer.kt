package com.wwdablu.soumya.simplypdf.composers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell

class TableComposer(simplyPdfDocument: SimplyPdfDocument) : GroupComposer(simplyPdfDocument) {

    private var colProperties: TableProperties = TableProperties()
    private var textComposer: TextComposer = TextComposer(simplyPdfDocument)

    init {
        colProperties.borderColor = "#000000"
        colProperties.borderWidth = 1
    }

    fun draw(cellList: List<List<Cell>>) {
        if (cellList.isEmpty()) {
            return
        }
        var largestHeight = 0
        for (rowCellList in cellList) {
            for (cell in rowCellList) {
                var cellHeight = 0
                if (cell is TextCell) {
                    cellHeight = textComposer
                        .write(cell.text, cell.properties,
                            cell.width, cell.verticalPadding, true, 0,
                            cell.horizontalPadding, false)
                }
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
                val cellData = rowCellList[rowIndex]
                if (cellData is TextCell) {
                    textComposer.write(cellData.text, cellData.properties,
                        cellData.width, cellData.verticalPadding,
                        true, bitmapXTranslate, cellData.horizontalPadding, true)
                    bitmapXTranslate += cellData.width
                }
            }
            simplyPdfDocument.addContentHeight(largestHeight)
            drawBorders(pageCanvas, largestHeight.toFloat(), rowCellList)
            largestHeight = 0
        }
    }

    fun setProperties(properties: TableProperties?) {
        colProperties = properties ?: TableProperties()
    }

    override val composerName: String
        get() = TableComposer::class.java.name

    private fun drawBorders(canvas: Canvas, maxHeight: Float, rowCellList: List<Cell>) {

        val borderPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPainter.color = Color.parseColor(colProperties.borderColor)

        canvas.save()
        canvas.translate(simplyPdfDocument.leftMargin.toFloat(),
            (simplyPdfDocument.pageContentHeight - maxHeight))

        for ((colIndex, cell) in rowCellList.withIndex()) {

            //Left border
            if (colIndex == 0) {
                canvas.drawLine(0f, 0f, 0f, maxHeight, borderPainter)
            }

            //Top border
            canvas.drawLine(0f, 0f, cell.width.toFloat(), 0f, borderPainter)

            //Right border
            canvas.drawLine(
                cell.width.toFloat(),
                0f,
                cell.width.toFloat(),
                maxHeight,
                borderPainter
            )

            //Bottom border
            canvas.drawLine(
                cell.width.toFloat(),
                maxHeight,
                0f,
                maxHeight,
                borderPainter
            )
            canvas.translate(cell.width.toFloat(), 0f)
        }
        canvas.restore()
    }
}