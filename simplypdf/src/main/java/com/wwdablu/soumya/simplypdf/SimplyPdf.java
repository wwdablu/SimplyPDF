package com.wwdablu.soumya.simplypdf;

import android.content.Context;
import android.print.PrintAttributes;

import androidx.annotation.NonNull;

import com.wwdablu.soumya.simplypdf.jsonengine.SimplyJson;

import java.io.File;

import io.reactivex.Observable;

public final class SimplyPdf {

    private final SimplyPdfDocument document;

    private SimplyPdf(@NonNull Context context, @NonNull File outputPdf) {
        document = new SimplyPdfDocument(context, outputPdf);
    }

    public static SimplyPdf with(@NonNull Context context, @NonNull File outputPdf) {
        return new SimplyPdf(context, outputPdf);
    }

    public static Observable<Boolean> use(@NonNull Context context,
                                           @NonNull SimplyPdf simplyPdf,
                                           @NonNull String payload) {

        return new SimplyJson(context, payload).generateWith(simplyPdf.document);
    }

    public SimplyPdf colorMode(DocumentInfo.ColorMode colorMode) {
        document.getDocumentInfo().setColorMode(colorMode);
        return this;
    }

    public SimplyPdf margin(DocumentInfo.Margins margins) {
        document.getDocumentInfo().setMargins(margins);
        return this;
    }

    public SimplyPdf paperSize(PrintAttributes.MediaSize paperSize) {
        document.getDocumentInfo().setPaperSize(paperSize);
        return this;
    }

    public SimplyPdf paperOrientation(DocumentInfo.Orientation orientation) {
        document.getDocumentInfo().setOrientation(orientation);
        return this;
    }

    public SimplyPdfDocument build() {
        document.build();
        return document;
    }
}
