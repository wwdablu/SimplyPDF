package com.wwdablu.soumya.simplypdf;

import android.graphics.Canvas;

abstract class Composer {

    int DEFAULT_SPACING = 10;

    SimplyPdfDocument simplyPdfDocument;

    final Canvas getPageCanvas() {
        return simplyPdfDocument.getCurrentPage().getCanvas();
    }

    final boolean canFitContentInPage(int contentHeight) {

        return simplyPdfDocument.getUsablePageHeight() >=
            (contentHeight + simplyPdfDocument.getPageContentHeight());
    }

    public void setSpacing(int spacing) {
        this.DEFAULT_SPACING = spacing;
    }

    public void insertEmptyLine() {
        simplyPdfDocument.addContentHeight(DEFAULT_SPACING);
    }

    void clean() {
        simplyPdfDocument = null;
    }
}
