package com.wwdablu.soumya.simplypdf;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class ImageComposer extends Composer {

    ImageComposer(SimplyPdfDocument simplyPdfDocument) {
        this.simplyPdfDocument = simplyPdfDocument;
    }

    @Override
    void clean() {
        super.clean();
    }
}
