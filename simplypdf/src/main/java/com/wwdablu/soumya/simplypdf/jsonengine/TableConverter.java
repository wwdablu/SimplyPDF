package com.wwdablu.soumya.simplypdf.jsonengine;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.wwdablu.soumya.simplypdf.composers.Composer;
import com.wwdablu.soumya.simplypdf.composers.TableComposer;
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell;
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class TableConverter extends BaseConverter {

    @Override
    protected void generate(Composer composer, JSONObject compose) throws Exception {

        if(!(composer instanceof TableComposer)) {
            return;
        }

        TableComposer tableComposer = (TableComposer) composer;

        String tableProperties = getProperties(compose);
        tableComposer.setProperties(TextUtils.isEmpty(tableProperties) ? null :
                new Gson().fromJson(tableProperties, TableProperties.class));

        //Generate the cell information
        List<List<Cell>> rowList = new ArrayList<>();

        JSONArray rowArray = compose.getJSONArray(Node.COMPOSER_TABLE_CONTENTS);
        int rowCount = rowArray.length();
        for(int rowIndex = 0; rowIndex < rowCount; rowIndex++) {

            JSONObject rowObject = rowArray.getJSONObject(rowIndex);
            JSONArray columnArray = rowObject.getJSONArray(Node.COMPOSER_TABLE_ROW);

            int colCount = columnArray.length();
            ArrayList<Cell> columnList = new ArrayList<>(colCount);
            rowList.add(columnList);
            for(int colIndex = 0; colIndex < colCount; colIndex++) {

                JSONObject colObject = columnArray.getJSONObject(colIndex);

                switch (colObject.getString(Node.TYPE).toLowerCase()) {

                    case Node.TYPE_TEXT:
                        String colTextProperties = getProperties(colObject);
                        TextCell textCell = new TextCell(
                                colObject.getString(Node.COMPOSER_TEXT_CONTENT),
                                TextUtils.isEmpty(colTextProperties) ? null :
                                        new Gson().fromJson(colTextProperties, TextProperties.class),
                                tableComposer.resolveCellWidth(colObject.getInt(Node.COMPOSER_TABLE_WIDTH))
                        );
                        columnList.add(textCell);
                        break;
                }
            }
        }

        tableComposer.draw(rowList);
    }
}
