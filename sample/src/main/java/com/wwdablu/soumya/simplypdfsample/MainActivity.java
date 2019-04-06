package com.wwdablu.soumya.simplypdfsample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;

import com.wwdablu.soumya.simplypdf.DocumentInfo;
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.ShapeDrawer;
import com.wwdablu.soumya.simplypdf.SimplyPdf;
import com.wwdablu.soumya.simplypdf.TextWriter;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextWriter textWriter;
    private ShapeDrawer shapeDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimplyPdfDocument simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
                .colorMode(DocumentInfo.ColorMode.COLOR)
                .paperSize(PrintAttributes.MediaSize.ISO_A4)
                .margin(DocumentInfo.Margins.DEFAULT)
                .build();

        textWriter = simplyPdfDocument.getTextWriter();
        shapeDrawer = simplyPdfDocument.getShapeDrawer();

        //testVariableFontSizeText();
        //testColoredText();
        testShapes();

        try {
            simplyPdfDocument.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testShapes() {

        shapeDrawer.setSpacing(25);

        shapeDrawer.drawCircle(100, Color.RED, 1, true, ShapeDrawer.Alignment.LEFT);
        shapeDrawer.drawCircle(100, Color.BLUE, 5, false, ShapeDrawer.Alignment.LEFT);
        shapeDrawer.drawBox(200, 200, Color.YELLOW, 5, true, ShapeDrawer.Alignment.LEFT);
        shapeDrawer.drawBox(200, 200, Color.BLACK, 5, false, ShapeDrawer.Alignment.LEFT);
    }

    private void testColoredText() {

        textWriter.write("Black", Color.BLACK, 16);
        textWriter.write("Red", Color.RED, 16);
        textWriter.write("Green", Color.GREEN, 16);
        textWriter.write("Blue", Color.BLUE, 16);
        textWriter.write("Magenta", Color.MAGENTA, 16);
        textWriter.write("Custom", Color.parseColor("#C8CFEE"), 16);
    }

    private void testVariableFontSizeText() {

        for(int i = 1; i <= 10; i++) {
            textWriter.write("The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]", i * 4);
        }
    }
}
