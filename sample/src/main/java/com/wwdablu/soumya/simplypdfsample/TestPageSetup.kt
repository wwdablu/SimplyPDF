package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Environment
import android.print.PrintAttributes
import android.text.Layout
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.TextComposer
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.Margin
import com.wwdablu.soumya.simplypdf.document.PageHeader
import java.io.File
import java.util.*

class TestPageSetup(context: Context) : CommonActions(context) {

    private val startMargin: Int = 25
    private val topMargin: Int = 25
    private val endMargin: Int = 25
    private val bottomMargin: Int = 25

    init {
        simplyPdfDocument = SimplyPdf.with(context,
            File(Environment.getExternalStorageDirectory().absolutePath + "/test.pdf"))
            .colorMode(DocumentInfo.ColorMode.COLOR)
            .paperSize(PrintAttributes.MediaSize.ISO_A4)
            .margin(Margin(startMargin.toUInt(), topMargin.toUInt(), endMargin.toUInt(), bottomMargin.toUInt()))
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
            .firstPageBackgroundColor(Color.parseColor("#C8C8C8"))
            .pageModifier(PageHeader(LinkedList<Cell>().apply {
                add(TextCell("PDF Generated Using SimplyPDF", TextProperties().apply {
                    textSize = 24
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#000000"
                }, Cell.MATCH_PARENT))
                add(TextCell("Version 2.0.0", TextProperties().apply {
                    textSize = 18
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#000000"
                }, Cell.MATCH_PARENT))
            }))
            .pageModifier(HeaderLinePageModifier())
            .build()

        testPageSetup()
        finishDoc()
    }

    private fun testPageSetup() {

        simplyPdfDocument.text.write("The quick brown fox jumps over the hungry lazy dog",
        TextProperties().apply {
            textSize = 32
            textColor = "#000000"
        })

        simplyPdfDocument.text.write("The quick brown fox jumps over the hungry lazy dog",
            TextProperties().apply {
                textSize = 32
                textColor = "#000000"
                alignment = Layout.Alignment.ALIGN_CENTER
            })

        simplyPdfDocument.text.write("The quick brown fox jumps over the hungry lazy dog",
            TextProperties().apply {
                textSize = 32
                textColor = "#000000"
                alignment = Layout.Alignment.ALIGN_OPPOSITE
            })

        drawPageMargins()

        simplyPdfDocument.newPage(Color.parseColor("#C8C8C8"))
        testBitmapsWithMargins()
    }

    private fun testBitmapsWithMargins() {

        simplyPdfDocument.text.write("Testing the bitmaps for margin adherence",
            TextProperties().apply {
                textSize = 32
                textColor = "#000000"
            })

        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565).apply {
            eraseColor(Color.GREEN)
        }

        simplyPdfDocument.image.drawBitmap(bitmap, ImageProperties().apply {
            alignment = Composer.Alignment.START
        })

        simplyPdfDocument.image.drawBitmap(bitmap, ImageProperties().apply {
            alignment = Composer.Alignment.CENTER
        })

        simplyPdfDocument.image.drawBitmap(bitmap, ImageProperties().apply {
            alignment = Composer.Alignment.END
        })

        bitmap.recycle()
        drawPageMargins()

        simplyPdfDocument.newPage(Color.parseColor("#C8C8C8"))
        testTableWithMargins()
    }

    private fun testTableWithMargins() {

        simplyPdfDocument.text.write("Testing the tables for margin adherence",
            TextProperties().apply {
                textSize = 32
                textColor = "#000000"
            })

        val tableRows = LinkedList<LinkedList<Cell>>()
        val textProperties = TextProperties().apply {
            textSize = 16
            textColor = "#000000"
            alignment = Layout.Alignment.ALIGN_CENTER
        }

        tableRows.add(
            LinkedList<Cell>().apply {
                add(TextCell("R1", textProperties, simplyPdfDocument.usablePageWidth))
            }
        )

        tableRows.add(
            LinkedList<Cell>().apply {
                add(TextCell("R2 C1", textProperties, simplyPdfDocument.usablePageWidth/2))
                add(TextCell("R2 C2", textProperties, simplyPdfDocument.usablePageWidth/2))
            }
        )

        tableRows.add(
            LinkedList<Cell>().apply {
                add(TextCell("R3 C1", textProperties, simplyPdfDocument.usablePageWidth/3))
                add(TextCell("R3 C2", textProperties, simplyPdfDocument.usablePageWidth/3))
                add(TextCell("R3 C3", textProperties, simplyPdfDocument.usablePageWidth/3))
            }
        )

        simplyPdfDocument.table.draw(tableRows, TableProperties().apply {
            borderWidth = 1
            borderColor = "#000000"
        })

        simplyPdfDocument.insertEmptySpace(50)
        simplyPdfDocument.table.draw(tableRows, TableProperties().apply {
            borderWidth = 1
            borderColor = "#000000"
            drawBorder = false
        })

        drawPageMargins()
    }

    private fun drawPageMargins() {

        simplyPdfDocument.currentPage.canvas.drawRect(Rect(startMargin, simplyPdfDocument.topMargin, startMargin + 1,
            simplyPdfDocument.usablePageHeight + simplyPdfDocument.bottomMargin),
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.RED
            })

        simplyPdfDocument.currentPage.canvas.drawRect(Rect(simplyPdfDocument.currentPage.info.pageWidth - endMargin,
            simplyPdfDocument.topMargin, simplyPdfDocument.currentPage.info.pageWidth - endMargin - 1,
            simplyPdfDocument.usablePageHeight + simplyPdfDocument.bottomMargin),
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.RED
            })
    }
}