package com.wwdablu.soumya.simplypdfsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.text.Layout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wwdablu.soumya.simplypdf.DocumentInfo;
import com.wwdablu.soumya.simplypdf.SimplyPdf;
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument;
import com.wwdablu.soumya.simplypdf.composers.ImageComposer;
import com.wwdablu.soumya.simplypdf.composers.ShapeComposer;
import com.wwdablu.soumya.simplypdf.composers.TableComposer;
import com.wwdablu.soumya.simplypdf.composers.TextComposer;
import com.wwdablu.soumya.simplypdf.composers.UnitComposer;
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties;
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties;
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties;
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties;
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell;
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextComposer textComposer;
    private ShapeComposer shapeComposer;
    private ImageComposer imageComposer;
    private TableComposer tableComposer;
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

        textComposer = new TextComposer(simplyPdfDocument);
        shapeComposer = new ShapeComposer(simplyPdfDocument);
        imageComposer = new ImageComposer(simplyPdfDocument);
        tableComposer = new TableComposer(simplyPdfDocument);

        //testVariableFontSizeText();
        //testHeaderTypeText();
        //testColoredText();
        //testNewPageWithBackground();
        //testShapes();
        //testTextAlignments();
        //testShapeAlignment();
        //testBitmapRender();
        //testSampleOutput();
        //testTextComposed();
        //testCustomComposer();

        generateFromJson();

        try {
            simplyPdfDocument.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFromJson() {

        SimplyPdfDocument simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_json.pdf"))
                .colorMode(DocumentInfo.ColorMode.COLOR)
                .paperSize(PrintAttributes.MediaSize.ISO_A4)
                .margin(DocumentInfo.Margins.DEFAULT)
                .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
                .build();

        final DisposableObserver<Boolean> disposableObserver = SimplyPdf.use(this, simplyPdfDocument, JSONStruct.payload)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    Toast.makeText(MainActivity.this, "Success: " + aBoolean, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {
                    //
                }
            });
    }

    private void testCustomComposer() {

        MyUnitComposer myUnitComposer = new MyUnitComposer();
        myUnitComposer.setSimplyPdfDocument(simplyPdfDocument);
        myUnitComposer.draw();
    }

    private void testTextComposed() {

        TextProperties textProperties = new TextProperties();
        textProperties.textSize = 24;
        textProperties.alignment = Layout.Alignment.ALIGN_CENTER;

        int w_50_cent = simplyPdfDocument.pageWidth() / 2;

        TableProperties colProperties = new TableProperties();
        colProperties.borderWidth = 1;
        colProperties.borderColor = "#000000";
        tableComposer.setProperties(colProperties);

        List<List<Cell>> composedList = new ArrayList<>();
        ArrayList<Cell> rowList = new ArrayList<>();

        //1st row
        rowList.add(new TextCell("Likes", textProperties, w_50_cent));
        rowList.add(new TextCell("Dislikes", textProperties, w_50_cent));
        composedList.add(rowList);

        textProperties = new TextProperties();
        textProperties.textSize = 24;
        textProperties.alignment = Layout.Alignment.ALIGN_CENTER;
        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL;
        textProperties.bulletSymbol = "•";
        textProperties.isBullet = true;

        //2nd row
        rowList = new ArrayList<>();
        Cell cell = new TextCell("Apple", textProperties, w_50_cent);
        rowList.add(cell);
        rowList.add(new TextCell("Guava", textProperties, w_50_cent));
        composedList.add(rowList);

        //3rd row
        rowList = new ArrayList<>();
        rowList.add(new TextCell("Banana", textProperties, w_50_cent));
        rowList.add(new TextCell("Coconut", textProperties, w_50_cent));
        composedList.add(rowList);

        //4th row
        rowList = new ArrayList<>();
        rowList.add(new TextCell("Mango", textProperties, w_50_cent));
        composedList.add(rowList);
        tableComposer.draw(composedList);

        simplyPdfDocument.insertEmptySpace(25);
        textProperties.isBullet = false;

        //new table
        composedList.clear();
        rowList = new ArrayList<>();
        rowList.add(new TextCell("Small Left Text", textProperties, w_50_cent));
        rowList.add(new TextCell("This is a big text on the right column which will be multiple lines.",
                textProperties, w_50_cent));
        composedList.add(rowList);
        tableComposer.draw(composedList);

        simplyPdfDocument.insertEmptySpace(25);

        //new table
        composedList.clear();
        rowList = new ArrayList<>();

        cell = new TextCell(
                "This is a big text a a the right column which will be multiple lines.", textProperties, w_50_cent);
        cell.setHorizontalPadding(25);
        cell.setVerticalPadding(50);
        rowList.add(cell);

        cell = new TextCell("Small right text", textProperties, w_50_cent);
        cell.setHorizontalPadding(25);
        cell.setVerticalPadding(50);
        rowList.add(cell);
        composedList.add(rowList);
        tableComposer.draw(composedList);
    }

    private void testSampleOutput() {

        TextProperties textProperties = new TextProperties();
        textProperties.textSize = 24;
        textProperties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        textProperties.alignment = Layout.Alignment.ALIGN_CENTER;

        textComposer.write("SimplyPDF", textProperties);

        textProperties.typeface = null;
        textProperties.textSize = 20;
        textComposer.write("An android library that allows the user to generate PDF" +
                " files within the application ans save them in the device storage." +
                " This sample PDF is generated using this library", textProperties);

        ShapeProperties shapeProperties = new ShapeProperties();
        shapeProperties.lineWidth = 1;
        shapeProperties.shouldFill = true;
        shapeProperties.lineColor = "#000000";
        shapeComposer.setSpacing(15);
        shapeComposer.drawBox(simplyPdfDocument.pageWidth(), 1, shapeProperties);

        textProperties.textSize = 16;
        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL;
        textComposer.write("Version 1.0.0 supports the following basics:", textProperties);

        textProperties.isBullet = true;
        textProperties.bulletSymbol = ">";
        textComposer.write("Text", textProperties);
        textComposer.write("Shapes", textProperties);
        textComposer.write("Images", textProperties);
        textComposer.write("Can also set page background color", textProperties);

        simplyPdfDocument.insertEmptySpace(25);
        simplyPdfDocument.newPage();

        textProperties.isBullet = false;
        textComposer.write("It can draw shapes like these:", textProperties);

        testShapeAlignment();
        testNewPageWithBackground();
        simplyPdfDocument.newPage();

        textComposer.write("That is all in Version 1.0.0. Will enhance for more.", textProperties);
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

        ImageProperties properties = new ImageProperties();
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

        ShapeProperties shapeProperties = new ShapeProperties();
        shapeProperties.lineWidth = 1;
        shapeProperties.shouldFill = true;

        shapeComposer.setSpacing(15);

        shapeProperties.lineColor = "#FF0000";
        shapeProperties.alignment = ShapeComposer.Alignment.LEFT;
        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = "#00FF00";
        shapeProperties.alignment = ShapeComposer.Alignment.CENTER;
        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = "#0000FF";
        shapeProperties.alignment = ShapeComposer.Alignment.RIGHT;
        shapeComposer.drawCircle(100, shapeProperties);
    }

    private void testTextAlignments() {

        TextProperties textProperties = new TextProperties();
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

        TextProperties textProperties = new TextProperties();
        textProperties.textSize = 16;
        textComposer.write("This is the header text", textProperties);

        ShapeProperties shapeProperties = new ShapeProperties();
        shapeProperties.lineWidth = 1;
        shapeProperties.lineColor = "#C4C4C4";
        shapeProperties.shouldFill = true;
        shapeComposer.drawBox(simplyPdfDocument.pageWidth(), 1, shapeProperties);
        simplyPdfDocument.insertEmptySpace(25);
    }

    private void testShapes() {

        ShapeProperties shapeProperties = new ShapeProperties();
        shapeProperties.lineWidth = 1;
        shapeProperties.lineColor = "#FF0000";
        shapeProperties.shouldFill = true;

        shapeComposer.setSpacing(25);

        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = "#0000FF";
        shapeProperties.shouldFill = false;
        shapeProperties.lineWidth = 5;
        shapeComposer.drawCircle(100, shapeProperties);

        shapeProperties.lineColor = "#00FF00";
        shapeProperties.shouldFill = true;
        shapeComposer.drawBox(200, 200, shapeProperties);

        shapeProperties.lineColor = "#000000";
        shapeProperties.shouldFill = false;
        shapeProperties.lineWidth = 15;
        shapeComposer.drawBox(200, 200, shapeProperties);
    }

    private void testColoredText() {

        TextProperties properties = new TextProperties();
        properties.textSize = 16;

        properties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        textComposer.write("Demonstrate the usage of TextComposer.", properties);

        properties.typeface = null;
        properties.isBullet = true;
        properties.bulletSymbol = "•";
        textComposer.write("Black", properties);

        properties.textColor = "#FF0000";
        properties.underline = true;
        textComposer.write("Red", properties);

        properties.strikethrough = true;
        properties.textColor = "#ABCDEF";
        textComposer.write("Custom", properties);

        properties.textColor = "#FFFFFF";
        textComposer.write("The quick brown fox, jumps over the hungry lazy dog. " +
                "This is a very long and interesting string.", properties);

        properties.strikethrough = false;
        properties.underline = false;
        textComposer.write("The quick brown fox, jumps over the hungry lazy dog. " +
                "This is a very long and interesting string.", properties);
    }

    private void testVariableFontSizeText() {

        TextProperties properties = new TextProperties();
        for(int i = 1; i <= 10; i++) {
            properties.textSize = i * 4;
            textComposer.write("The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]", properties);
        }
    }

    private void testNewPageWithBackground() {
        simplyPdfDocument.newPage();
        simplyPdfDocument.setPageBackgroundColor(Color.MAGENTA);

        TextProperties properties = new TextProperties();
        properties.textSize = 32;
        properties.textColor = "#000000";

        textComposer.write("White text on magenta background page.", properties);
    }



    public class MyUnitComposer extends UnitComposer {

        @Override
        public String getComposerName() {
            return null;
        }

        public void draw() {

            simplyPdfDocument.newPage();

            Canvas canvas = getPageCanvas();
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(Color.RED);
            canvas.drawRect(new Rect(0,0,100,100), p);

            canvas.translate(0, 150);
            canvas.drawText("Custom composer", 0, 0, p);
        }
    }
}
