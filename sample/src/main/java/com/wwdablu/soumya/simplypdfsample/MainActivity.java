package com.wwdablu.soumya.simplypdfsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;

import com.wwdablu.soumya.simplypdf.DocumentInfo;
import com.wwdablu.soumya.simplypdf.PdfDocument;
import com.wwdablu.soumya.simplypdf.SimplyPdf;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PdfDocument pdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
                .colorMode(DocumentInfo.ColorMode.COLOR)
                .paperSize(PrintAttributes.MediaSize.ISO_A4)
                .margin(DocumentInfo.Margins.DEFAULT)
                .build();

        testVariableFontSizeText(pdfDocument);

        try {
            pdfDocument.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testVariableFontSizeText(PdfDocument pdfDocument) {

        for(int i = 1; i <= 10; i++) {
            pdfDocument.getTextWriter().write("The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]", i * 4);
        }
    }
}
