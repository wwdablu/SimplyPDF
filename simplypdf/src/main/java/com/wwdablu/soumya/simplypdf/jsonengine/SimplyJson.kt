package com.wwdablu.soumya.simplypdf.jsonengine

import android.content.Context
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.ImageComposer
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer
import com.wwdablu.soumya.simplypdf.composers.TableComposer
import com.wwdablu.soumya.simplypdf.composers.TextComposer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

internal class SimplyJson(private val context: Context, private val payload: String) {
    
    private lateinit var simplyPdfDocument: SimplyPdfDocument
    
    suspend fun generateWith(simplyPdfDocument: SimplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument
        withContext(Dispatchers.IO) {
            parsePayload()
        }
    }

    @Throws(Exception::class)
    private suspend fun parsePayload() {

        val contentArray = JSONObject(payload).getJSONArray(Node.CONTENT)
        if (contentArray.length() == 0) {
            return
        }

        val textComposer: TextComposer by lazy { TextComposer(simplyPdfDocument) }
        val imageComposer: ImageComposer by lazy { ImageComposer(simplyPdfDocument) }
        val tableComposer: TableComposer by lazy { TableComposer(simplyPdfDocument) }
        val shapeComposer: ShapeComposer by lazy { ShapeComposer(simplyPdfDocument) }

        val textConverter: TextConverter by lazy { TextConverter(simplyPdfDocument) }
        val tableConverter: TableConverter by lazy { TableConverter(simplyPdfDocument) }
        val shapeConverter: ShapeConverter by lazy { ShapeConverter(simplyPdfDocument) }

        val imageConverter: ImageConverter by lazy { ImageConverter(context, simplyPdfDocument) }

        for (index in 0 until contentArray.length()) {
            val compose = contentArray.getJSONObject(index)
            if (compose == null) {
                IllegalArgumentException("Could not parse JSON")
                return
            }

            when (compose.getString(Node.TYPE).lowercase()) {
                Node.TYPE_TEXT -> textConverter.generate(textComposer, compose)
                Node.TYPE_IMAGE -> imageConverter.generate(imageComposer, compose)
                Node.TYPE_TABLE -> tableConverter.generate(tableComposer, compose)
                Node.TYPE_SHAPE -> shapeConverter.generate(shapeComposer, compose)
                Node.TYPE_SPACE -> simplyPdfDocument.insertEmptySpace(compose.getInt(Node.COMPOSER_SPACE_HEIGHT))
                Node.TYPE_NEWPAGE -> simplyPdfDocument.newPage()
            }
        }

        simplyPdfDocument.finish()
    }
}