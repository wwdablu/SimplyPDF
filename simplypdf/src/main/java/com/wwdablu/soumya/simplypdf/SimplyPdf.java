package com.wwdablu.soumya.simplypdf;

import android.content.Context;
import android.print.PrintAttributes;

import java.io.File;

import androidx.annotation.NonNull;

public final class SimplyPdf {

    private SimplyPdfDocument document;

    private SimplyPdf(@NonNull Context context, @NonNull File outputPdf) {
        document = new SimplyPdfDocument(context, outputPdf);
    }

    public static SimplyPdf with(@NonNull Context context, @NonNull File outputPdf) {
        return new SimplyPdf(context, outputPdf);
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

    public SimplyPdfDocument build() {
        document.build();
        return document;
    }
}
