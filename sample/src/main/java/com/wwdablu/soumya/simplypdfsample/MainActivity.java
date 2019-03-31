package com.wwdablu.soumya.simplypdfsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        PdfDocument pdfDocument = SimplyPdf.with(this, new File("Sample.pdf"))
                .colorMode(DocumentInfo.ColorMode.COLOR)
                .paperSize(PrintAttributes.MediaSize.ISO_A4)
                .margin(DocumentInfo.Margins.DEFAULT)
                .build();

        pdfDocument.getTextWriter().write("Hello World", 18);
        try {
            pdfDocument.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
