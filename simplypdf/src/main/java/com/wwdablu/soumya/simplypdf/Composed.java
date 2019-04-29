package com.wwdablu.soumya.simplypdf;

import android.graphics.Bitmap;

public final class Composed {

    private Bitmap bitmap;
    private String composedBy;

    private int width;
    private int height;

    Composed(String by, int width, int height) {
        this.composedBy = by;
        this.width = width;
        this.height = height;

        this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }

    Bitmap getComposedBitmap() {
        return this.bitmap;
    }

    void free() {
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public String composedBy() {
        return this.composedBy;
    }
}
