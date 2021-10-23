package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.text.Layout
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer
import com.wwdablu.soumya.simplypdf.composers.properties.ShapeProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties

class TestShapeComposer(context: Context) : CommonActions(context) {

    init {
        createSimplyPdfDocument()
        testShapeComposer()
        finishDoc()
    }

    private fun testShapeComposer() {

        val properties = ShapeProperties().apply {
            lineColor = "#000000"
            lineWidth = 1
        }
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 12
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        //Drawing a hollow circle
        simplyPdfDocument.text.write("Drawing a circle", textProperties)
        simplyPdfDocument.shape.drawCircle(125f, properties)
        simplyPdfDocument.insertEmptySpace(25)

        //Drawing a filled box
        simplyPdfDocument.apply {
            properties.shouldFill = true
            text.write("Drawing a filled box", textProperties)
            shape.drawBox(100f, 100f, properties)
        }

        //Draw filled circles with alignment
        simplyPdfDocument.insertEmptySpace(25)
        simplyPdfDocument.text.write("Draw filled shapes with alignments", textProperties)

        //Draw red filled circle
        simplyPdfDocument.apply {
            properties.lineColor = "#FF0000"
            properties.alignment = Composer.Alignment.START
            shape.drawCircle(25f, properties)
        }

        //Draw green filled circle center aligned
        simplyPdfDocument.apply {
            properties.lineColor = "#00FF00"
            properties.alignment = Composer.Alignment.CENTER
            shape.drawCircle(25f, properties)
        }

        //Draw blue filled circle end aligned
        simplyPdfDocument.apply {
            properties.lineColor = "#0000FF"
            properties.alignment = Composer.Alignment.END
            shape.drawCircle(25f, properties)
        }

        //Create a new page
        simplyPdfDocument.newPage()

        //Draw a freeform
        simplyPdfDocument.text.write("Drawing a freeform using path", textProperties)
        simplyPdfDocument.insertEmptySpace(10)
        properties.alignment = Composer.Alignment.START
        val triangle = ShapeComposer.FreeformPath(simplyPdfDocument).apply {
            moveTo(0f, 0f)
            lineTo(100f, 0f)
            lineTo(50f, 100f)
            lineTo(0f, 0f)
            close()
        }
        simplyPdfDocument.shape.drawFreeform(triangle, properties)

        //Validate page content height
        simplyPdfDocument.text.write("Complete", textProperties)
    }
}