package com.wwdablu.soumya.simplypdf.jsonengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.ImageComposer;
import com.wwdablu.soumya.simplypdf.composers.TableComposer;
import com.wwdablu.soumya.simplypdf.composers.TextComposer;
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties;
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell;
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public final class SimplyJson {

    private Context context;
    private String payload;
    private SimplyPdfDocument simplyPdfDocument;

    private ObservableEmitter<Boolean> emitter;

    private TextComposer textComposer;
    private ImageComposer imageComposer;
    private TableComposer tableComposer;

    public SimplyJson(@NonNull Context context, @NonNull String payload) {
        this.context = context;
        this.payload = payload;
    }

    public Observable<Boolean> generateWith(@NonNull SimplyPdfDocument simplyPdfDocument) {

        this.simplyPdfDocument = simplyPdfDocument;
        return Observable.create(e -> {
            emitter = e;

            try {
                parsePayload();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    private void emitSuccess() throws IOException {
        simplyPdfDocument.finish();
        emitter.onNext(true);
        emitter.onComplete();
    }

    private void emitFailure(Throwable t) {
        emitter.onError(t);
    }

    private void parsePayload() throws Exception {

        JSONObject root = new JSONObject(payload);
        JSONArray contentArray = root.getJSONArray(Node.CONTENT);

        if(contentArray == null || contentArray.length() == 0) {
            emitSuccess();
            return;
        }

        for(int index = 0; index < contentArray.length(); index++) {

            JSONObject compose = contentArray.getJSONObject(index);
            if(compose == null) {
                emitFailure(new IllegalArgumentException("Could not parse JSON"));
                return;
            }

            switch (compose.getString(Node.TYPE).toLowerCase()) {

                case Node.TYPE_TEXT:
                    if(textComposer == null) {
                        textComposer = new TextComposer(simplyPdfDocument);
                    }

                    //Check if properties has been defined
                    String textProperties = getProperties(compose);
                    textComposer.write(compose.getString(Node.COMPOSER_TEXT_CONTENT),
                        TextUtils.isEmpty(textProperties) ? null :
                            new Gson().fromJson(textProperties, TextProperties.class));
                    break;

                case Node.TYPE_IMAGE:
                    if(imageComposer == null) {
                        imageComposer = new ImageComposer(simplyPdfDocument);
                    }

                    String imageProperties = getProperties(compose);
                    Bitmap bitmap = Glide.with(context)
                        .asBitmap()
                        .load(compose.getString(Node.COMPOSER_IMAGE_URL))
                        .submit()
                        .get();
                    imageComposer.drawBitmap(bitmap, TextUtils.isEmpty(imageProperties) ? null :
                            new Gson().fromJson(imageProperties, ImageProperties.class));
                    bitmap.recycle();
                    break;

                case Node.TYPE_TABLE:
                    if(tableComposer == null) {
                        tableComposer = new TableComposer(simplyPdfDocument);
                    }

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
                                            resolveCellWidth(colObject.getInt(Node.COMPOSER_TABLE_WIDTH))
                                    );
                                    columnList.add(textCell);
                                    break;
                            }
                        }
                    }

                    tableComposer.draw(rowList);
                    break;
            }
        }

        emitSuccess();
    }

    private String getProperties(JSONObject compose) throws JSONException {
        return compose.has(Node.TYPE_PROPERTIES) ?
            compose.getJSONObject(Node.TYPE_PROPERTIES).toString() : null;
    }

    private int resolveCellWidth(int widthPercent) {
        return simplyPdfDocument.pageWidth() / (100 / widthPercent);
    }
}
