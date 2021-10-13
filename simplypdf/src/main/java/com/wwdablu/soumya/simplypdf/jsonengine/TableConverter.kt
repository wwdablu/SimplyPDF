package com.wwdablu.soumya.simplypdf.jsonengine

import com.wwdablu.soumya.simplypdf.composers.models.TextProperties
import android.text.TextUtils
import com.wwdablu.soumya.simplypdf.composers.TableComposer
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell
import kotlin.Throws
import org.json.JSONObject
import com.google.gson.Gson
import com.wwdablu.soumya.simplypdf.composers.Composer
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell
import java.lang.Exception
import java.util.ArrayList

internal class TableConverter : BaseConverter() {

    @Throws(Exception::class)
    public override fun generate(composer: Composer, compose: JSONObject) {
        if (composer !is TableComposer) {
            return
        }
        val tableProperties = getProperties(compose)
        composer.setProperties(
            if (TextUtils.isEmpty(tableProperties)) null else Gson().fromJson(
                tableProperties,
                TableProperties::class.java
            )
        )

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
                            if (TextUtils.isEmpty(colTextProperties)) null else Gson().fromJson(
                                colTextProperties,
                                TextProperties::class.java
                            ),
                            composer.resolveCellWidth(colObject.getInt(Node.COMPOSER_TABLE_WIDTH))
                        )
                        columnList.add(textCell)
                    }
                }
            }
        }
        composer.draw(rowList)
    }
}