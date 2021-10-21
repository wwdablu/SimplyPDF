package com.wwdablu.soumya.simplypdf.jsonengine

import android.text.TextUtils
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.TableComposer
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.ImageCell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import com.wwdablu.soumya.simplypdf.jsonengine.base.ComposerConverter
import org.json.JSONObject
import java.util.*

internal class TableConverter(simplyPdfDocument: SimplyPdfDocument) : ComposerConverter(simplyPdfDocument) {

    @Throws(Exception::class)
    override fun generate(composeJsonObject: JSONObject) {

        val tableProperties = getProperties(composeJsonObject)
        val composer = simplyPdfDocument.table

        //Generate the cell information
        val rowList: MutableList<List<Cell>> = ArrayList()
        val rowArray = composeJsonObject.getJSONArray(Node.COMPOSER_TABLE_CONTENTS)
        val rowCount = rowArray.length()
        for (rowIndex in 0 until rowCount) {
            val rowObject = rowArray.getJSONObject(rowIndex)
            val columnArray = rowObject.getJSONArray(Node.COMPOSER_TABLE_ROW)
            val colCount = columnArray.length()
            val columnList = ArrayList<Cell>(colCount)
            rowList.add(columnList)
            for (colIndex in 0 until colCount) {
                val colObject = columnArray.getJSONObject(colIndex)
                columnList.add(cellConverter(colObject, composer))
            }
        }
        composer.draw(rowList, if (TextUtils.isEmpty(tableProperties)) TableProperties() else Gson().fromJson(
            tableProperties, TableProperties::class.java))
    }

    internal fun cellConverter(jsonObject: JSONObject, composer: TableComposer) : Cell {

        val widthPercent = if(jsonObject.has(Node.COMPOSER_TABLE_WIDTH)) {
            jsonObject.getInt(Node.COMPOSER_TABLE_WIDTH)
        } else {
            100
        }

        return when (jsonObject.getString(Node.TYPE).lowercase()) {
            Node.TYPE_TEXT -> {
                val colTextProperties = getProperties(jsonObject)
                TextCell(
                    jsonObject.getString(Node.COMPOSER_TEXT_CONTENT),
                    if (TextUtils.isEmpty(colTextProperties)) TextProperties() else Gson().fromJson(
                        colTextProperties,
                        TextProperties::class.java
                    ),
                    composer.resolveCellWidth(widthPercent)
                )
            }
            else -> {
                TextCell("", TextProperties().apply { textSize = 0 },
                    composer.resolveCellWidth(widthPercent)
                )
            }
        }
    }

    override fun getTypeHandler(): String = "table"
}