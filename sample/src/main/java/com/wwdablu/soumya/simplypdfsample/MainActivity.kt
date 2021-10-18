package com.wwdablu.soumya.simplypdfsample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.provider.Settings
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wwdablu.soumya.simplypdf.SimplyPdf.Companion.usingJson
import com.wwdablu.soumya.simplypdf.SimplyPdf.Companion.with
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.*
import com.wwdablu.soumya.simplypdf.composers.models.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.models.ShapeProperties
import com.wwdablu.soumya.simplypdf.composers.models.TableProperties
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties
import com.wwdablu.soumya.simplypdf.composers.models.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.models.cell.ImageCell
import com.wwdablu.soumya.simplypdf.composers.models.cell.TextCell
import com.wwdablu.soumya.simplypdf.documentinfo.DocumentInfo
import com.wwdablu.soumya.simplypdf.documentinfo.Margin
import com.wwdablu.soumya.simplypdfsample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var simplyPdfDocument: SimplyPdfDocument

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTextTest.setOnClickListener {
            if(this::binding.isInitialized) {
                testTextComposer()
            }
        }

        binding.btnImageTest.setOnClickListener {
            if(this::binding.isInitialized) {
                testImageComposer()
            }
        }

        binding.btnShapeTest.setOnClickListener {
            if(this::binding.isInitialized) {
                testShapeComposer()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        addCategory("android.intent.category.DEFAULT")
                        data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                    }
                    startActivityForResult(intent, 2296)
                } catch (e: Exception) {
                    val intent = Intent().apply {
                        action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    }
                    startActivityForResult(intent, 2296)
                }
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1000
                )
            }
            return
        }
        createSimplyPdfDocument()
    }

    private fun testTextComposer() {

        //Create text entries of variable font size
        val properties = TextProperties()
        for (i in 1..10) {
            properties.textSize = i * 4
            simplyPdfDocument.text.write(
                "The quick brown fox jumps over the hungry lazy dog. [Size: " + i * 4 + "]",
                properties
            )
        }

        //Insert a new page
        simplyPdfDocument.newPage()

        //Text with red color
        properties.textSize = 16
        properties.textColor = "#FF0000"
        simplyPdfDocument.text.write("Text with red color font", properties)

        //Text with custom color
        properties.textColor = "#ABCDEF"
        simplyPdfDocument.text.write("Text with color set as #ABCDEF", properties)

        //Text with bullet
        properties.textColor = "#000000"
        properties.bulletSymbol = "•"
        properties.isBullet = true
        simplyPdfDocument.text.write("Text with bullet mark at the start", properties)

        //Text with alignments
        properties.alignment = Layout.Alignment.ALIGN_NORMAL
        properties.isBullet = false
        simplyPdfDocument.text.write("Normal text alignment", properties)

        properties.alignment = Layout.Alignment.ALIGN_CENTER
        simplyPdfDocument.text.write("Center text alignment", properties)

        properties.alignment = Layout.Alignment.ALIGN_OPPOSITE
        simplyPdfDocument.text.write("Opposite text alignment", properties)

        //Bold typeface
        properties.alignment = Layout.Alignment.ALIGN_NORMAL
        properties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        simplyPdfDocument.text.write("Bold text", properties)

        //Underlined text
        properties.underline = true
        properties.typeface = null
        simplyPdfDocument.text.write("Underlined text", properties)

        finishDoc(simplyPdfDocument)
    }

    private fun testImageComposer() {

        val properties = ImageProperties()
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 12
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        //Load an image from a URL
        simplyPdfDocument.text.write("Loading image from URL", textProperties)
        simplyPdfDocument.image.drawFromUrl("https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4", this, properties)

        simplyPdfDocument.newPage()

        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)
        bitmap.eraseColor(Color.RED)
        simplyPdfDocument.text.write("Loading a red color bitmap", textProperties)
        simplyPdfDocument.image.drawBitmap(bitmap, properties)
        bitmap.recycle()

        finishDoc(simplyPdfDocument)
    }

    private fun testShapeComposer() {

        val properties = ShapeProperties().apply {
            lineColor = "#000000"
            lineWidth = 1
        }
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 12
            alignment = Layout.Alignment.ALIGN_NORMAL
        }

        //Drawing a hollow circle
        simplyPdfDocument.text.write("Drawing a circle", textProperties)
        simplyPdfDocument.shape.drawCircle(125f, properties)
        simplyPdfDocument.insertEmptySpace(25)

        //Drawing a filled box
        simplyPdfDocument.apply {
            properties.shouldFill = true
            text.write("Drawing a filled box", textProperties)
            shape.drawBox(100f, 100f, properties)
        }

        //Draw filled circles with alignment
        simplyPdfDocument.insertEmptySpace(25)
        simplyPdfDocument.text.write("Draw filled shapes with alignments", textProperties)

        //Draw red filled circle
        simplyPdfDocument.apply {
            properties.lineColor = "#FF0000"
            properties.alignment = Composer.Alignment.START
            shape.drawCircle(25f, properties)
        }

        //Draw green filled circle center aligned
        simplyPdfDocument.apply {
            properties.lineColor = "#00FF00"
            properties.alignment = Composer.Alignment.CENTER
            shape.drawCircle(25f, properties)
        }

        //Draw blue filled circle end aligned
        simplyPdfDocument.apply {
            properties.lineColor = "#0000FF"
            properties.alignment = Composer.Alignment.END
            shape.drawCircle(25f, properties)
        }

        //Create a new page
        simplyPdfDocument.newPage()

        //Draw a freeform
        simplyPdfDocument.text.write("Drawing a freeform using path", textProperties)
        simplyPdfDocument.insertEmptySpace(10)
        properties.alignment = Composer.Alignment.START
        val triangle = ShapeComposer.FreeformPath(simplyPdfDocument).apply {
            moveTo(0f, 0f)
            lineTo(100f, 0f)
            lineTo(50f, 100f)
            lineTo(0f, 0f)
            close()
        }
        simplyPdfDocument.shape.drawFreeform(triangle, properties)

        //Validate page content height
        simplyPdfDocument.text.write("Complete", textProperties)

        finishDoc(simplyPdfDocument)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    createSimplyPdfDocument()
                } else {
                    Toast.makeText(this, "Allow permission for storage access!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createSimplyPdfDocument()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun createSimplyPdfDocument() {

        simplyPdfDocument = with(this, File(Environment.getExternalStorageDirectory().absolutePath + "/test.pdf"))
            .colorMode(DocumentInfo.ColorMode.COLOR)
            .paperSize(PrintAttributes.MediaSize.ISO_A4)
            .margin(Margin(15U, 15U, 15U, 15U))
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
            .build()
    }

    private fun finishDoc(simplyPdfDocument: SimplyPdfDocument) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = simplyPdfDocument.finish()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "PDF $result", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateFromJson() {
        val simplyPdfDocument = with(this,
                File(Environment.getExternalStorageDirectory().absolutePath + "/test_json.pdf"))
            .colorMode(DocumentInfo.ColorMode.COLOR)
            .paperSize(PrintAttributes.MediaSize.ISO_A4)
            .margin(Margin(15U, 15U, 15U, 15U))
            .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
            .build()
        
        val exceptionHandler = CoroutineExceptionHandler { _, throwable -> 
            Log.e(MainActivity::class.java.simpleName, "JSON to PDF exception.", throwable)
        }

        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            usingJson(this@MainActivity, simplyPdfDocument, JSONStruct.payload)
        }
    }

    private fun testCustomComposer() {
        val myUnitComposer = MyUnitComposer(simplyPdfDocument)
        myUnitComposer.draw()
    }

    private fun testTextComposed() {
        var textProperties = TextProperties()
        textProperties.textSize = 24
        textProperties.textColor = "#FF0000"
        textProperties.alignment = Layout.Alignment.ALIGN_CENTER
        
        val w_50_cent = simplyPdfDocument.pageWidth() / 2
        val colProperties = TableProperties()
        colProperties.borderWidth = 1
        colProperties.borderColor = "#000000"
        simplyPdfDocument.table.tableProperties = colProperties
        val composedList: ArrayList<List<Cell>> = ArrayList()
        var rowList = ArrayList<Cell>()

        //1st row
        rowList.add(TextCell("Likes", simplyPdfDocument, textProperties, w_50_cent))
        rowList.add(TextCell("Dislikes", simplyPdfDocument, textProperties, w_50_cent))
        composedList.add(rowList)

        textProperties = TextProperties()
        textProperties.textSize = 24
        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL
        textProperties.bulletSymbol = "•"
        textProperties.isBullet = true

        //2nd row
        rowList = ArrayList()
        var cell: Cell = TextCell("Apple", simplyPdfDocument, textProperties, w_50_cent)
        rowList.add(cell)
        rowList.add(TextCell("Guava", simplyPdfDocument, textProperties, w_50_cent))
        composedList.add(rowList)

        //3rd row
        rowList = ArrayList()
        rowList.add(TextCell("Banana", simplyPdfDocument, textProperties, w_50_cent))
        rowList.add(TextCell("Coconut", simplyPdfDocument, textProperties, w_50_cent))
        composedList.add(rowList)

        //4th row
        rowList = ArrayList()
        rowList.add(TextCell("Mango", simplyPdfDocument, textProperties, w_50_cent))
        composedList.add(rowList)
        //simplyPdfDocument.table.draw(composedList)
        simplyPdfDocument.insertEmptySpace(25)
        textProperties.isBullet = false

        //new table
        composedList.clear()
        rowList = ArrayList()
        rowList.add(TextCell("Small Left Text", simplyPdfDocument, textProperties, w_50_cent))
        rowList.add(
            TextCell(
                "This is a big text on the right column which will be multiple lines.",
                simplyPdfDocument, textProperties, w_50_cent
            )
        )
        composedList.add(rowList)
        simplyPdfDocument.table.draw(composedList)
        simplyPdfDocument.insertEmptySpace(25)

        //new table
        composedList.clear()
        rowList = ArrayList()
        cell = TextCell(
            "This is a big text a a the right column which will be multiple lines.",
            simplyPdfDocument, textProperties,
            w_50_cent
        )
        cell.horizontalPadding = 25
        cell.verticalPadding = 50
        rowList.add(cell)
        cell = TextCell("Small right text", simplyPdfDocument, textProperties, w_50_cent)
        cell.horizontalPadding = 25
        cell.verticalPadding = 50
        rowList.add(cell)
        composedList.add(rowList)
        simplyPdfDocument.table.draw(composedList)
        simplyPdfDocument.insertEmptySpace(25)

        //new table --- with image and text
        composedList.clear()
        rowList = ArrayList()
        val redBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565)
        val canvas = Canvas(redBitmap)
        canvas.drawColor(Color.RED)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawCircle(50f, 50f, 25f, paint)
        cell = ImageCell(redBitmap, simplyPdfDocument, ImageProperties()).apply {
            horizontalPadding = 25
            verticalPadding = 25
        }
        rowList.add(cell)

        //Another red circle
        cell = TextCell("This is a red bitmap", simplyPdfDocument, textProperties, w_50_cent).apply {
            horizontalPadding = 25
            verticalPadding = 25
        }
        rowList.add(cell)

        composedList.add(rowList)
        simplyPdfDocument.table.draw(composedList)
        redBitmap.recycle()
    }

    private fun testSampleOutput() {
        val textProperties = TextProperties()
        textProperties.textSize = 24
        textProperties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textProperties.alignment = Layout.Alignment.ALIGN_CENTER
        simplyPdfDocument.text.write("SimplyPDF", textProperties)
        textProperties.typeface = null
        textProperties.textSize = 20
        simplyPdfDocument.text.write(
            "An android library that allows the user to generate PDF" +
                    " files within the application ans save them in the device storage." +
                    " This sample PDF is generated using this library", textProperties
        )
        val shapeProperties = ShapeProperties()
        shapeProperties.lineWidth = 1
        shapeProperties.shouldFill = true
        shapeProperties.lineColor = "#000000"
        simplyPdfDocument.shape.spacing = 15
        simplyPdfDocument.shape.drawBox(0f, 0f, simplyPdfDocument.pageWidth().toFloat(), 1f, shapeProperties)
        textProperties.textSize = 16
        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL
        simplyPdfDocument.text.write("Version 1.0.0 supports the following basics:", textProperties)
        textProperties.isBullet = true
        textProperties.bulletSymbol = ">"
        simplyPdfDocument.text.write("Text", textProperties)
        simplyPdfDocument.text.write("Shapes", textProperties)
        simplyPdfDocument.text.write("Images", textProperties)
        simplyPdfDocument.text.write("Can also set page background color", textProperties)
        simplyPdfDocument.insertEmptySpace(25)
        simplyPdfDocument.newPage()
        textProperties.isBullet = false
        simplyPdfDocument.text.write("It can draw shapes like these:", textProperties)
        testShapeAlignment()
        testNewPageWithBackground()
        simplyPdfDocument.newPage()
        simplyPdfDocument.text.write("That is all in Version 1.0.0. Will enhance for more.", textProperties)
    }

    private fun testBitmapRender() {
        var bitmap =
            Bitmap.createBitmap(simplyPdfDocument.pageWidth(), 100, Bitmap.Config.RGB_565)
        var c = Canvas(bitmap)
        c.drawColor(Color.BLUE)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.textSize = 32f
        c.drawText("Hello Bitmap", 10f, 50f, paint)
        simplyPdfDocument.image.drawBitmap(bitmap, ImageProperties())
        bitmap.recycle()
        val properties = ImageProperties()
        properties.alignment = Composer.Alignment.START
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)
        c = Canvas(bitmap)
        c.drawColor(Color.RED)
        simplyPdfDocument.image.drawBitmap(bitmap, properties)
        properties.alignment = Composer.Alignment.CENTER
        simplyPdfDocument.image.drawBitmap(bitmap, properties)
        properties.alignment = Composer.Alignment.END
        simplyPdfDocument.image.drawBitmap(bitmap, properties)
        bitmap.recycle()
    }

    private fun testShapeAlignment() {
        val shapeProperties = ShapeProperties()
        shapeProperties.lineWidth = 1
        shapeProperties.shouldFill = true
        simplyPdfDocument.shape.spacing = 15
        shapeProperties.lineColor = "#FF0000"
        shapeProperties.alignment = Composer.Alignment.START
        simplyPdfDocument.shape.drawCircle(100f, 100f, 100f, shapeProperties)

        shapeProperties.lineColor = "#00FF00"
        shapeProperties.alignment = Composer.Alignment.CENTER
        simplyPdfDocument.shape.drawCircle(0f, 0f,100f, shapeProperties)

        shapeProperties.lineColor = "#0000FF"
        shapeProperties.alignment = Composer.Alignment.END
        simplyPdfDocument.shape.drawCircle(0f, 0f, 100f, shapeProperties)
    }

    private fun testHeaderTypeText() {
        val textProperties = TextProperties()
        textProperties.textSize = 16
        simplyPdfDocument.text.write("This is the header text", textProperties)
        val shapeProperties = ShapeProperties()
        shapeProperties.lineWidth = 1
        shapeProperties.lineColor = "#C4C4C4"
        shapeProperties.shouldFill = true
        simplyPdfDocument.shape.drawBox(0f, 0f, simplyPdfDocument.pageWidth().toFloat(), 1f, shapeProperties)
        simplyPdfDocument.insertEmptySpace(25)
    }

    private fun testShapes() {
        val shapeProperties = ShapeProperties()
        shapeProperties.lineWidth = 1
        shapeProperties.lineColor = "#FF0000"
        shapeProperties.shouldFill = true
        simplyPdfDocument.shape.spacing = 25
        simplyPdfDocument.shape.drawCircle(0f, 0f, 100f, shapeProperties)

        shapeProperties.lineColor = "#0000FF"
        shapeProperties.shouldFill = false
        shapeProperties.lineWidth = 5
        simplyPdfDocument.shape.drawCircle(100f, 50f, 100f, shapeProperties)

        shapeProperties.lineColor = "#00FF00"
        shapeProperties.shouldFill = true
        simplyPdfDocument.shape.drawBox(150f, 0f, 200f, 200f, shapeProperties)

        shapeProperties.lineColor = "#000000x"
        shapeProperties.shouldFill = false
        shapeProperties.lineWidth = 15
        simplyPdfDocument.shape.drawBox(0f, 0f, 200f, 200f, shapeProperties)
    }

    private fun testColoredText() {
        val properties = TextProperties()
        properties.textSize = 16
        properties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        simplyPdfDocument.text.write("Demonstrate the usage of simplyPdfDocument.text.", properties)
        properties.typeface = null
        properties.isBullet = true
        properties.bulletSymbol = "•"
        simplyPdfDocument.text.write("Black", properties)
        properties.textColor = "#FF0000"
        properties.underline = true
        simplyPdfDocument.text.write("Red", properties)
        properties.strikethrough = true
        properties.textColor = "#ABCDEF"
        simplyPdfDocument.text.write("Custom", properties)
        properties.textColor = "#FFFFFF"
        simplyPdfDocument.text.write(
            "The quick brown fox, jumps over the hungry lazy dog. " +
                    "This is a very long and interesting string.", properties
        )
        properties.strikethrough = false
        properties.underline = false
        simplyPdfDocument.text.write(
            "The quick brown fox, jumps over the hungry lazy dog. " +
                    "This is a very long and interesting string.", properties
        )
    }

    private fun testNewPageWithBackground() {
        simplyPdfDocument.newPage()
        simplyPdfDocument.setPageBackgroundColor(Color.MAGENTA)
        val properties = TextProperties()
        properties.textSize = 32
        properties.textColor = "#000000"
        simplyPdfDocument.text.write("White text on magenta background page.", properties)
    }

    inner class MyUnitComposer(simplyPdfDocument: SimplyPdfDocument) : UnitComposer(simplyPdfDocument) {

        fun draw() {
            simplyPdfDocument.newPage()
            val p = Paint(Paint.ANTI_ALIAS_FLAG)
            p.color = Color.RED
            pageCanvas.drawRect(Rect(0, 0, 100, 100), p)
            pageCanvas.translate(0f, 150f)
            pageCanvas.drawText("Custom composer", 0f, 0f, p)
        }
    }
}