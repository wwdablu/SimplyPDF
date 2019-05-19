package com.wwdablu.soumya.simplypdf.jsonengine;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.wwdablu.soumya.simplypdf.composers.Composer;
import com.wwdablu.soumya.simplypdf.composers.TextComposer;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;

import org.json.JSONException;
import org.json.JSONObject;

class TextConverter extends BaseConverter {

    @Override
    protected void generate(Composer composer, JSONObject compose) throws JSONException {

        if(!(composer instanceof TextComposer)) {
            return;
        }

        TextComposer textComposer = (TextComposer) composer;

        //Check if properties has been defined
        String textProperties = getProperties(compose);
        textComposer.write(compose.getString(Node.COMPOSER_TEXT_CONTENT),
                TextUtils.isEmpty(textProperties) ? null :
                        new Gson().fromJson(textProperties, TextProperties.class));
    }
}
