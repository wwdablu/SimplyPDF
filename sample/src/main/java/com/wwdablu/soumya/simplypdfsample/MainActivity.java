package com.wwdablu.soumya.simplypdfsample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;

import com.wwdablu.soumya.simplypdf.DocumentInfo;
import com.wwdablu.soumya.simplypdf.ShapeComposer;
import com.wwdablu.soumya.simplypdf.SimplyPdf;
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.TextComposer;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextComposer textComposer;
    private ShapeComposer shapeComposer;
    private SimplyPdfDocument simplyPdfDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
                .colorMode(DocumentInfo.ColorMode.COLOR)
                .paperSize(PrintAttributes.MediaSize.ISO_A4)
                .margin(DocumentInfo.Margins.DEFAULT)
                .paperOrientation(DocumentInfo.Orientation.LANDSCAPE)
                .build();

        textComposer = simplyPdfDocument.getTextComposer();
        shapeComposer = simplyPdfDocument.getShapeComposer();

        //testVariableFontSizeText();
        testHeaderTypeText();
        testColoredText();
        //testShapes();

        try {
            simplyPdfDocument.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testHeaderTypeText() {

        TextComposer.Properties textProperties = new TextComposer.Properties();
        textProperties.textSize = 16;
        textComposer.write("This is the header text", textProperties);

        shapeComposer.drawBox(simplyPdfDocument.pageWidth(), 1, Color.GRAY, 1, true, ShapeComposer.Alignment.LEFT);
        simplyPdfDocument.insertEmptyLine();
    }

    private void testShapes() {

        shapeComposer.setSpacing(25);

        shapeComposer.drawCircle(100, Color.RED, 1, true, ShapeComposer.Alignment.LEFT);
        shapeComposer.drawCircle(100, Color.BLUE, 5, false, ShapeComposer.Alignment.LEFT);
        shapeComposer.drawBox(200, 200, Color.YELLOW, 5, true, ShapeComposer.Alignment.LEFT);
        shapeComposer.drawBox(200, 200, Color.BLACK, 5, false, ShapeComposer.Alignment.LEFT);
    }

    private void testColoredText() {

        TextComposer.Properties properties = new TextComposer.Properties();
        properties.textSize = 16;

        properties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        textComposer.write("Demonstrate the usage of TextComposer.", properties);

        properties.typeface = null;
        properties.isBullet = true;
        properties.bulletSymbol = "•";
        textComposer.write("Black", properties);

        properties.textColor = Color.RED;
        properties.underline = true;
        textComposer.write("Red", properties);

        properties.strikethrough = true;
        properties.textColor = Color.parseColor("#ABCDEF");
        textComposer.write("Custom", properties);

        properties.textColor = Color.BLACK;
        textComposer.write("The quick brown fox, jumps over the hungry lazy dog. " +
                "This is a very long and interesting string.", properties);

        properties.strikethrough = false;
        properties.underline = false;
        textComposer.write("The quick brown fox, jumps over the hungry lazy dog. " +
                "This is a very long and interesting string.", properties);
    }

    private void testVariableFontSizeText() {

        TextComposer.Properties properties = new TextComposer.Properties();
        for(int i = 1; i <= 10; i++) {
            properties.textSize = i * 4;
            textComposer.write("The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]", properties);
        }
    }
}
