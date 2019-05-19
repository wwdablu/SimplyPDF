package com.wwdablu.soumya.simplypdf.jsonengine;

import android.content.Context;

import androidx.annotation.NonNull;

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.ImageComposer;
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer;
import com.wwdablu.soumya.simplypdf.composers.TableComposer;
import com.wwdablu.soumya.simplypdf.composers.TextComposer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public final class SimplyJson {

    private Context context;
    private String payload;
    private SimplyPdfDocument simplyPdfDocument;

    private ObservableEmitter<Boolean> emitter;

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

        TextComposer textComposer = null;
        ImageComposer imageComposer = null;
        TableComposer tableComposer = null;
        ShapeComposer shapeComposer = null;

        TextConverter textConverter = null;
        ImageConverter imageConverter = null;
        TableConverter tableConverter = null;
        ShapeConverter shapeConverter = null;

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
                        textConverter = new TextConverter();
                    }

                    textConverter.generate(textComposer, compose);
                    break;

                case Node.TYPE_IMAGE:
                    if(imageComposer == null) {
                        imageComposer = new ImageComposer(simplyPdfDocument);
                        imageConverter = new ImageConverter(context);
                    }

                    imageConverter.generate(imageComposer, compose);
                    break;

                case Node.TYPE_TABLE:
                    if(tableComposer == null) {
                        tableComposer = new TableComposer(simplyPdfDocument);
                        tableConverter = new TableConverter();
                    }

                    tableConverter.generate(tableComposer, compose);
                    break;

                case Node.TYPE_SHAPE:
                    if(shapeComposer == null) {
                        shapeComposer = new ShapeComposer(simplyPdfDocument);
                        shapeConverter = new ShapeConverter();
                    }

                    shapeConverter.generate(shapeComposer, compose);
                    break;

                case Node.TYPE_SPACE:
                    simplyPdfDocument.insertEmptySpace(compose.getInt(Node.COMPOSER_SPACE_HEIGHT));
                    break;

                case Node.TYPE_NEWPAGE:
                    simplyPdfDocument.newPage();
                    break;
            }
        }

        emitSuccess();
    }
}
