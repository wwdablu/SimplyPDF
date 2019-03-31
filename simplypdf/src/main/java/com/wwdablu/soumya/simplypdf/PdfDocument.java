package com.wwdablu.soumya.simplypdf;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.graphics.pdf.PdfDocument.Page;

import java.io.File;
import java.io.FileNotFoundException;
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

    private int currentPageNumber = 0;
    private int pageContentHeight = 0;

    PdfDocument(@NonNull Context context, @NonNull File document) {
        documentInfo = new DocumentInfo();
        this.document = document;
        this.context = context;
    }

    public File getDocument() {
        return document;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public TextWriter getTextWriter() {

        if(textWriter == null) {
            textWriter = new TextWriter(this);
        }

        return textWriter;
    }

    public void finish() throws IOException {
        pdfDocument.writeTo(new FileOutputStream(document));
        pdfDocument.close();
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
    }

    PrintedPdfDocument getPdfDocument() {
        return pdfDocument;
    }

    PrintAttributes getPrintAttributes() {
        return printAttributes;
    }

    int getCurrentPageNumber() {
        return currentPageNumber;
    }

    Page getCurrentPage() {
        return currentPage;
    }

    Page newPage() {
        currentPage = pdfDocument.startPage(++this.currentPageNumber);
        pageContentHeight = 0;
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

    int getPageHeight() {
        return pdfDocument == null ? 0 : (pdfDocument.getPageContentRect().height() -
            (printAttributes.getMinMargins() == null ? 0  :
                (printAttributes.getMinMargins().getTopMils() +
                    printAttributes.getMinMargins().getBottomMils())));
    }
}
