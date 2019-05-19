package com.wwdablu.soumya.simplypdf.jsonengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wwdablu.soumya.simplypdf.composers.Composer;
import com.wwdablu.soumya.simplypdf.composers.ImageComposer;
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties;

import org.json.JSONObject;

class ImageConverter extends BaseConverter {

    private Context context;

    ImageConverter(Context context) {
        this.context = context;
    }

    @Override
    protected void generate(Composer composer, JSONObject compose) throws Exception {

        if(!(composer instanceof ImageComposer)) {
            return;
        }

        ImageComposer imageComposer = (ImageComposer) composer;

        String imageProperties = getProperties(compose);
        Bitmap bitmap = Glide.with(context)
                .asBitmap()
                .load(compose.getString(Node.COMPOSER_IMAGE_URL))
                .submit()
                .get();
        imageComposer.drawBitmap(bitmap, TextUtils.isEmpty(imageProperties) ? null :
                new Gson().fromJson(imageProperties, ImageProperties.class));
        bitmap.recycle();
    }
}
