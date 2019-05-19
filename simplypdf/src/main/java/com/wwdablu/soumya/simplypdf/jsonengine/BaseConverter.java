package com.wwdablu.soumya.simplypdf.jsonengine;

import com.wwdablu.soumya.simplypdf.composers.Composer;

import org.json.JSONException;
import org.json.JSONObject;

abstract class BaseConverter {

    protected abstract void generate(Composer composer, JSONObject compose) throws Exception;

    protected String getProperties(JSONObject compose) throws JSONException {
        return compose.has(Node.TYPE_PROPERTIES) ?
                compose.getJSONObject(Node.TYPE_PROPERTIES).toString() : null;
    }
}
