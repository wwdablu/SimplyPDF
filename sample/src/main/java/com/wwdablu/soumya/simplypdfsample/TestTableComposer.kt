package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Layout
import com.bumptech.glide.Glide
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.ImageCell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TestTableComposer(context: Context) : CommonActions(context) {

    init {
        createSimplyPdfDocument()
        CoroutineScope(Dispatchers.Main).launch {
            testTableComposer()
        }
    }

    private suspend fun testTableComposer() {

        val properties = TableProperties().apply {
            borderColor = "#000000"
            borderWidth = 1
        }
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 16
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        testTableBorders()
        simplyPdfDocument.newPage()
        testTableAlignment()

        val rows = LinkedList<LinkedList<Cell>>()
        simplyPdfDocument.text.write("Table with text data", textProperties)

        /*
         * This will add a table with 1 column and N rows
         */
        LinkedList<Cell>().apply {
            add(TextCell("Row - 1", textProperties, simplyPdfDocument.usablePageWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            add(TextCell("Row - 2", textProperties, simplyPdfDocument.usablePageWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            add(TextCell("Row - 3", textProperties, simplyPdfDocument.usablePageWidth))
            rows.add(this)
        }

        /*
         * This will add a table with 2 equal width column and N rows
         */
        val halfWidth = simplyPdfDocument.usablePageWidth / 2
        LinkedList<Cell>().apply {
            textProperties.alignment = Layout.Alignment.ALIGN_NORMAL
            add(TextCell("R1 C1", textProperties, halfWidth))
            add(TextCell("R1 C2", textProperties, halfWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            textProperties.alignment = Layout.Alignment.ALIGN_CENTER
            add(TextCell("R2 C1", textProperties, halfWidth))
            add(TextCell("R2 C2", textProperties, halfWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            textProperties.alignment = Layout.Alignment.ALIGN_OPPOSITE
            add(TextCell("R3 C1", textProperties, halfWidth))
            add(TextCell("R3 C2", textProperties, halfWidth))
            rows.add(this)
        }

        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL

        /*
         * This will add a table with 3 different width column and N rows
         */
        LinkedList<Cell>().apply {
            add(TextCell("R4 C1", textProperties, halfWidth))
            add(TextCell("R4 C2", textProperties, halfWidth/2))
            add(TextCell("R4 C3", textProperties, halfWidth/2))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            add(TextCell("R5 C1", TextProperties(), halfWidth/2))
            add(TextCell("R5 C2", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_CENTER }, halfWidth))
            add(TextCell("R5 C3", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_OPPOSITE }, halfWidth/2))
            rows.add(this)
        }

        simplyPdfDocument.table.draw(rows, properties)


        // ---- New page --- Tests images inside cell


        /*
         * This will add bitmap and image to the table along with text as description
         */
        simplyPdfDocument.newPage()
        simplyPdfDocument.text.write("Drawing bitmaps inside tables", textProperties)
        rows.clear()

        val redBmp = Bitmap.createBitmap(200, 100, Bitmap.Config.RGB_565).apply {
            eraseColor(Color.RED)
        }
        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(redBmp, ImageProperties(), halfWidth))
            add(TextCell("This is a red bitmap, start aligned", TextProperties().apply {
                textSize = 16 }, halfWidth))
        })

        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(redBmp, ImageProperties().apply { alignment = Composer.Alignment.CENTER }, halfWidth))
            add(TextCell("This is a red bitmap, center aligned", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_CENTER
                textSize = 16
            }, halfWidth))
        })

        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(redBmp, ImageProperties().apply { alignment = Composer.Alignment.END }, halfWidth))
            add(TextCell("This is a red bitmap, end aligned", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_OPPOSITE
                textSize = 16
            }, halfWidth))
        })

        val greenBmp = Bitmap.createBitmap(simplyPdfDocument.usablePageWidth,
            500, Bitmap.Config.RGB_565).apply {
            eraseColor(Color.GREEN)
        }
        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(greenBmp, ImageProperties(), halfWidth))
            add(TextCell("This is a green bitmap, which has been shrunk to fit within cell", TextProperties().apply {
                textSize = 32 }, halfWidth))
        })

        simplyPdfDocument.table.draw(rows, properties)
        redBmp.recycle()
        greenBmp.recycle()

        //Try to fit a bit image within a small table
        rows.clear()
        val bigImage = withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load("https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4")
                .timeout(30 * 1000)
                .submit()
                .get()
        }

        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(bigImage, ImageProperties(), halfWidth/3))
        })

        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(bigImage, ImageProperties(), Cell.MATCH_PARENT))
        })

        simplyPdfDocument.table.draw(rows, properties)
        finishDoc()
    }

    private fun testTableBorders() {

        val properties = TableProperties().apply {
            borderColor = "#000000"
            borderWidth = 1
            borderStyle = TableProperties.BORDER_INNER
        }
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 16
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        val rows = LinkedList<LinkedList<Cell>>()

        val halfWidth = simplyPdfDocument.usablePageWidth / 2
        rows.add(LinkedList<Cell>().apply {
            add(TextCell("Name", textProperties, halfWidth))
            add(TextCell("Soumya Kanti Kar", textProperties, halfWidth))
        })

        rows.add(LinkedList<Cell>().apply {
            add(TextCell("Developed", textProperties, halfWidth))
            add(TextCell("SimplyPdf", textProperties, halfWidth))
        })

        rows.add(LinkedList<Cell>().apply {
            add(TextCell("SimplePDF", textProperties, halfWidth))
            add(TextCell("PDF made", textProperties, halfWidth/2))
            add(TextCell("E A S Y", textProperties, halfWidth/2))
        })

        simplyPdfDocument.run {
            properties.borderStyle = TableProperties.BORDER_INNER
            text.write("Table Inner Border:", textProperties)
            table.draw(rows, properties)
            insertEmptySpace(16)

            properties.borderStyle = TableProperties.BORDER_OUTER
            text.write("Table Outer Border:", textProperties)
            table.draw(rows, properties)
            insertEmptySpace(16)

            properties.borderStyle = TableProperties.BORDER_HORIZONTAL
            text.write("Table Horizontal Border:", textProperties)
            table.draw(rows, properties)
            insertEmptySpace(16)

            properties.borderStyle = TableProperties.BORDER_VERTICAL
            text.write("Table Vertical Border:", textProperties)
            table.draw(rows, properties)
            insertEmptySpace(16)

            properties.borderStyle = TableProperties.BORDER_ALL
            text.write("Table All Border:", textProperties)
            table.draw(rows, properties)
            insertEmptySpace(16)
        }
    }

    private fun testTableAlignment() {

        simplyPdfDocument.insertEmptySpace(16)

        val properties = TableProperties().apply {
            borderColor = "#000000"
            borderWidth = 1
        }
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 16
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        simplyPdfDocument.text.write("Testing table alignment", textProperties)

        val rows = LinkedList<LinkedList<Cell>>()

        val halfWidth = simplyPdfDocument.usablePageWidth / 2
        val qrtWidth = halfWidth / 2
        rows.add(LinkedList<Cell>().apply {
            add(TextCell("Name", textProperties, qrtWidth))
            add(TextCell("Soumya Kanti Kar", textProperties, qrtWidth))
        })

        simplyPdfDocument.table.draw(rows, properties)
    }
}