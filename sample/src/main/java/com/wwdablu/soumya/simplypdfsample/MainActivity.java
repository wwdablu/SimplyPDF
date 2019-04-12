package com.wwdablu.soumya.simplypdfsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.text.Layout;

import com.wwdablu.soumya.simplypdf.DocumentInfo;
import com.wwdablu.soumya.simplypdf.ImageComposer;
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
    private ImageComposer imageComposer;
    private SimplyPdfDocument simplyPdfDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
                .colorMode(DocumentInfo.ColorMode.COLOR)
                .paperSize(PrintAttributes.MediaSize.ISO_A4)
                .margin(DocumentInfo.Margins.DEFAULT)
                .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
                .build();

        textComposer = simplyPdfDocument.getTextComposer();
        shapeComposer = simplyPdfDocument.getShapeComposer();
        imageComposer = simplyPdfDocument.getImageComposer();

        //testVariableFontSizeText();
        //testHeaderTypeText();
        //testColoredText();
        //testNewPageWithBackground();
        //testShapes();
        //testTextAlignments();
        //testShapeAlignment();
        testBitmapRender();

        try {
            simplyPdfDocument.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testBitmapRender() {

        Bitmap bitmap = Bitmap.createBitmap(simplyPdfDocument.pageWidth(), 100, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.BLUE);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(32);
        c.drawText("Hello Bitmap", 10, 50, paint);
        imageComposer.drawBitmap(bitmap, null);
        bitmap.recycle();

        ImageComposer.Properties properties = new ImageComposer.Properties();
        properties.alignment = ImageComposer.Alignment.LEFT;
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        c = new Canvas(bitmap);
        c.drawColor(Color.RED);
        imageComposer.drawBitmap(bitmap, properties);

        properties.alignment = ImageComposer.Alignment.CENTER;
        imageComposer.drawBitmap(bitmap, properties);

        properties.alignment = ImageComposer.Alignment.RIGHT;
        imageComposer.drawBitmap(bitmap, properties);

        bitmap.recycle();
    }

    private void testShapeAlignment() {

        ShapeComposer.Properties shapeProperties = new ShapeComposer.Properties();
        shapeProperties.lineWidth = 1;
        shapeProperties.shouldFill = true;

        shapeComposer.setSpacing(15);

        shapeProperties.lineColor = Color.RED;
        shapeProperties.alignment = ShapeComposer.Alignment.LEFT;
        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = Color.GREEN;
        shapeProperties.alignment = ShapeComposer.Alignment.CENTER;
        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = Color.BLUE;
        shapeProperties.alignment = ShapeComposer.Alignment.RIGHT;
        shapeComposer.drawCircle(100, shapeProperties);
    }

    private void testTextAlignments() {

        TextComposer.Properties textProperties = new TextComposer.Properties();
        textProperties.textSize = 16;

        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL;
        textComposer.write("Left aligned text.", textProperties);

        textProperties.alignment = Layout.Alignment.ALIGN_CENTER;
        textComposer.write("Center aligned text.", textProperties);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            textProperties.alignment = Layout.Alignment.ALIGN_RIGHT;
            textComposer.write("Right aligned text.", textProperties);
        } else {
            textProperties.alignment = Layout.Alignment.ALIGN_NORMAL;
            textComposer.write("Right alignment needs API 28+", textProperties);
        }
    }

    private void testHeaderTypeText() {

        TextComposer.Properties textProperties = new TextComposer.Properties();
        textProperties.textSize = 16;
        textComposer.write("This is the header text", textProperties);

        ShapeComposer.Properties shapeProperties = new ShapeComposer.Properties();
        shapeProperties.lineWidth = 1;
        shapeProperties.lineColor = Color.GRAY;
        shapeProperties.shouldFill = true;
        shapeComposer.drawBox(simplyPdfDocument.pageWidth(), 1, shapeProperties);
        simplyPdfDocument.insertEmptyLine();
    }

    private void testShapes() {

        ShapeComposer.Properties shapeProperties = new ShapeComposer.Properties();
        shapeProperties.lineWidth = 1;
        shapeProperties.lineColor = Color.RED;
        shapeProperties.shouldFill = true;

        shapeComposer.setSpacing(25);

        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = Color.BLUE;
        shapeProperties.shouldFill = false;
        shapeProperties.lineWidth = 5;
        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = Color.YELLOW;
        shapeProperties.shouldFill = true;
        shapeComposer.drawBox(200, 200, shapeProperties);

        shapeProperties.lineColor = Color.BLACK;
        shapeProperties.shouldFill = false;
        shapeProperties.lineWidth = 15;
        shapeComposer.drawBox(200, 200, shapeProperties);
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

    private void testNewPageWithBackground() {
        simplyPdfDocument.insertNewPage();
        simplyPdfDocument.setPageBackgroundColor(Color.MAGENTA);

        TextComposer.Properties properties = new TextComposer.Properties();
        properties.textSize = 32;
        properties.textColor = Color.WHITE;

        textComposer.write("White text on magenta background page.", properties);
    }
}
