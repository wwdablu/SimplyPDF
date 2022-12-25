package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.graphics.Typeface
import android.text.Layout
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties

class TestTextComposer(context: Context) : CommonActions(context) {

    init {
        createSimplyPdfDocument()
        testTextComposer()
        finishDoc()
    }

    private fun testTextComposer() {

        //Create text entries of variable font size
        val properties = TextProperties().apply {
            textColor = "#000000"
        }
        for (i in 1..10) {
            properties.textSize = i * 4
            simplyPdfDocument.text.write(
                "The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]",
                properties
            )
        }

        //Insert a new page
        simplyPdfDocument.newPage()

        //Text with red color
        properties.textSize = 16
        properties.textColor = "#FF0000"
        simplyPdfDocument.text.write("Text with red color font", properties)

        //Text with custom color
        properties.textColor = "#ABCDEF"
        simplyPdfDocument.text.write("Text with color set as #ABCDEF", properties)

        //Text with bullet
        properties.textColor = "#000000"
        properties.bulletSymbol = "•"
        simplyPdfDocument.text.write("Text with bullet mark at the start", properties)
        simplyPdfDocument.text.write("Text with bullet mark at the start 2nd line", properties)

        properties.bulletSymbol = ""
        simplyPdfDocument.text.write("Normal text after bullets", properties)
        simplyPdfDocument.text.write("Normal text 2nd line", properties)

        //Text with alignments
        properties.alignment = Layout.Alignment.ALIGN_NORMAL
        simplyPdfDocument.text.write("Normal text alignment", properties)

        properties.alignment = Layout.Alignment.ALIGN_CENTER
        simplyPdfDocument.text.write("Center text alignment", properties)

        properties.alignment = Layout.Alignment.ALIGN_OPPOSITE
        simplyPdfDocument.text.write("Opposite text alignment", properties)

        //Bold typeface
        properties.alignment = Layout.Alignment.ALIGN_NORMAL
        properties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        simplyPdfDocument.text.write("Bold text", properties)

        //Underlined text
        properties.underline = true
        properties.typeface = null
        simplyPdfDocument.text.write("Underlined text", properties)

        //Test to write text with a fixed width in the page
        simplyPdfDocument.insertEmptySpace(25)
        properties.apply {
            underline = false
            textSize = 32
            alignment = Layout.Alignment.ALIGN_CENTER
        }
        simplyPdfDocument.text.write("The quick brown fox jumps over the hungry lazy dog. " +
                "This text is written keeping the page width as half.",
            properties, simplyPdfDocument.usablePageWidth/2, 0, 50)

        simplyPdfDocument.text.writeAtPosition(
            "Text at absolute position x = 60, y = 450",
            properties,
            xShift = 60,
            yShift = 450
        )

        simplyPdfDocument.text.write("Complete", properties.apply { textSize = 12 })
    }
}