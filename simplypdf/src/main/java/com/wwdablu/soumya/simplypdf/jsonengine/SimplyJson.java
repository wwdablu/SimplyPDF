package com.wwdablu.soumya.simplypdf.jsonengine;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.TextComposer;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;

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

    private TextComposer textComposer;

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
                    String textProperties = compose.has(Node.TYPE_PROPERTIES) ?
                        compose.getJSONObject(Node.TYPE_PROPERTIES).toString() : null;

                    textComposer.write(compose.getString(Node.COMPOSER_TEXT_CONTENT),
                        TextUtils.isEmpty(textProperties) ? null :
                            new Gson().fromJson(textProperties, TextProperties.class));
                    break;
            }
        }

        emitSuccess();
    }
}
