package com.wwdablu.soumya.simplypdf.jsonengine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.TableComposer
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.models.cell.ImageCell
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell
import org.json.JSONObject
import java.util.*

internal class TableConverter(simplyPdfDocument: SimplyPdfDocument) : BaseConverter(simplyPdfDocument) {

    @Throws(Exception::class)
    public override fun generate(composer: Composer, compose: JSONObject) {
        if (composer !is TableComposer) {
            return
        }
        val tableProperties = getProperties(compose)
        composer.tableProperties =
            if (TextUtils.isEmpty(tableProperties)) TableProperties() else Gson().fromJson(
                tableProperties, TableProperties::class.java)

        //Generate the cell information
        val rowList: MutableList<List<Cell>> = ArrayList()
        val rowArray = compose.getJSONArray(Node.COMPOSER_TABLE_CONTENTS)
        val rowCount = rowArray.length()
        for (rowIndex in 0 until rowCount) {
            val rowObject = rowArray.getJSONObject(rowIndex)
            val columnArray = rowObject.getJSONArray(Node.COMPOSER_TABLE_ROW)
            val colCount = columnArray.length()
            val columnList = ArrayList<Cell>(colCount)
            rowList.add(columnList)
            for (colIndex in 0 until colCount) {
                val colObject = columnArray.getJSONObject(colIndex)
                when (colObject.getString(Node.TYPE).lowercase()) {
                    Node.TYPE_TEXT -> {
                        val colTextProperties = getProperties(colObject)
                        val textCell = TextCell(
                            colObject.getString(Node.COMPOSER_TEXT_CONTENT),
                            simplyPdfDocument,
                            if (TextUtils.isEmpty(colTextProperties)) TextProperties() else Gson().fromJson(
                                colTextProperties,
                                TextProperties::class.java
                            ),
                            composer.resolveCellWidth(colObject.getInt(Node.COMPOSER_TABLE_WIDTH))
                        )
                        columnList.add(textCell)
                    }
                    Node.TYPE_IMAGE -> {
                        val colImageProperties = getProperties(colObject)
                        val imageProperties = if (TextUtils.isEmpty(colImageProperties)) ImageProperties()
                                else Gson().fromJson(colImageProperties, ImageProperties::class.java)

                        val bitmap: Bitmap? = when(imageProperties.format?.lowercase()) {
                            Node.IMAGE_FORMAT_BASE64 -> {
                                val decodedString = Base64.decode(imageProperties.source, Base64.URL_SAFE)
                                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                            }
                            else -> null
                        }
                    }
                }
            }
        }
        composer.draw(rowList)
    }
}