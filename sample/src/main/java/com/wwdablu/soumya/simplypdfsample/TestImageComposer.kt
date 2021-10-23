package com.wwdablu.soumya.simplypdfsample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Layout
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties

class TestImageComposer(context: Context) : CommonActions(context) {

    init {
        createSimplyPdfDocument()
        testImageComposer()
        finishDoc()
    }

    private fun testImageComposer() {

        val properties = ImageProperties()
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 12
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        //Load an image from a URL
        simplyPdfDocument.text.write("Loading image from URL", textProperties)
        simplyPdfDocument.image.drawFromUrl("https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4", context, properties)

        //Draw a bitmap
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)
        bitmap.eraseColor(Color.RED)
        simplyPdfDocument.text.write("Loading a red color bitmap", textProperties)
        simplyPdfDocument.image.drawBitmap(bitmap, properties)
        bitmap.recycle()
    }
}