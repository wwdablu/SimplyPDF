package com.wwdablu.soumya.simplypdf;

import android.content.Context;
import android.graphics.pdf.PdfDocument.Page;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;

public class PdfDocument {

    private File document;
    private Context context;
    private DocumentInfo documentInfo;
    private PrintedPdfDocument pdfDocument;
    private PrintAttributes printAttributes;
    private Page currentPage;

    private TextWriter textWriter;
    private ShapeDrawer shapeDrawer;

    private int currentPageNumber = 0;
    private int pageContentHeight = 0;

    PdfDocument(@NonNull Context context, @NonNull File document) {
        documentInfo = new DocumentInfo();
        this.document = document;
        this.context = context;
    }

    @NonNull
    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    @NonNull
    public TextWriter getTextWriter() {

        if(textWriter == null) {
            textWriter = new TextWriter(this);
        }

        return textWriter;
    }

    @NonNull
    public ShapeDrawer getShapeDrawer() {

        if(shapeDrawer == null) {
            shapeDrawer = new ShapeDrawer(this);
        }

        return shapeDrawer;
    }

    public int pageHeight() {
        return getUsablePageHeight();
    }

    public int pageWidth() {
        return getUsablePageWidth();
    }

    public void finish() throws IOException {
        pdfDocument.finishPage(currentPage);
        pdfDocument.writeTo(new FileOutputStream(document));
        pdfDocument.close();

        if(textWriter != null) {
            textWriter.clean();
        }
    }

    /*
     *
     * --- STOP ---
     *     ANYTHING BELOW IS MAINTAINED INTERNALLY BY SIMPLY PDF AND IS NOT EXPOSED
     * --- STOP ---
     *
     */

    void build() {
        printAttributes = new PrintAttributes.Builder()
                .setColorMode(documentInfo.resolveColorMode())
                .setMediaSize(documentInfo.getPaperSize())
                .setMinMargins(documentInfo.resolveMargin())
                .build();

        pdfDocument = new PrintedPdfDocument(context, printAttributes);
        currentPage = pdfDocument.startPage(getCurrentPageNumber());
        pageContentHeight = getTopMargin();
    }

    int getLeftMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getLeftMils();
    }

    int getTopMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getTopMils();
    }

    int getRightMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getRightMils();
    }

    int getBottomMargin() {
        return printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getBottomMils();
    }

    int getCurrentPageNumber() {
        return currentPageNumber;
    }

    Page getCurrentPage() {
        return currentPage;
    }

    Page newPage() {
        pdfDocument.finishPage(currentPage);
        ++this.currentPageNumber;
        currentPage = pdfDocument.startPage(currentPageNumber);
        pageContentHeight = getTopMargin();
        addPageContentHeight(printAttributes.getMinMargins() == null ? 0 :
            printAttributes.getMinMargins().getTopMils());
        return currentPage;
    }

    int getPageContentHeight() {
        return pageContentHeight;
    }

    void addPageContentHeight(int pageContentHeight) {
        this.pageContentHeight += pageContentHeight;
    }

    int getUsablePageHeight() {
        return pdfDocument == null ? 0 : (pdfDocument.getPageContentRect().height() -
            (getTopMargin() + getBottomMargin()));
    }

    int getUsablePageWidth() {
        return pdfDocument.getPageWidth() - (getLeftMargin() + getRightMargin());
    }
}
