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
import com.wwdablu.soumya.simplypdf.composers.properties.ImageProperties
import com.wwdablu.soumya.simplypdf.composers.properties.ShapeProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.ImageCell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.Margin
import com.wwdablu.soumya.simplypdf.document.PageHeader
import com.wwdablu.soumya.simplypdfsample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.File
import java.util.*

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

        binding.btnTableTest.setOnClickListener {
            if(this::binding.isInitialized) {
                testTableComposer()
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
        val properties = TextProperties().apply {
            textColor = "#000000"
        }
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

        //Test to write text with a fixed width in the page
        simplyPdfDocument.insertEmptySpace(25)
        properties.apply {
            underline = false
            textSize = 32
            alignment = Layout.Alignment.ALIGN_CENTER
        }
        simplyPdfDocument.text.write("The quick brown fox jumps over the hungry lazy dog. " +
                "This text is written keeping the page width as half.",
        properties, simplyPdfDocument.usablePageWidth/2, 0, 50)

        simplyPdfDocument.text.write("Complete", properties.apply { textSize = 12 })

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

        //Draw a bitmap
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

    private fun testTableComposer() {

        val properties = TableProperties().apply {
            borderColor = "#000000"
            borderWidth = 1
        }
        val textProperties = TextProperties().apply {
            textColor = "#000000"
            textSize = 16
            alignment = Layout.Alignment.ALIGN_NORMAL
        }
        val imageProperties = ImageProperties()

        val rows = LinkedList<LinkedList<Cell>>()
        simplyPdfDocument.text.write("Table with text data", textProperties)

        /*
         * This will add a table with 1 column and N rows
         */
        LinkedList<Cell>().apply {
            add(TextCell("Row - 1", textProperties, simplyPdfDocument.usablePageWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            add(TextCell("Row - 2", textProperties, simplyPdfDocument.usablePageWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            add(TextCell("Row - 3", textProperties, simplyPdfDocument.usablePageWidth))
            rows.add(this)
        }

        /*
         * This will add a table with 2 equal width column and N rows
         */
        val halfWidth = simplyPdfDocument.usablePageWidth / 2
        LinkedList<Cell>().apply {
            textProperties.alignment = Layout.Alignment.ALIGN_NORMAL
            add(TextCell("R1 C1", textProperties, halfWidth))
            add(TextCell("R1 C2", textProperties, halfWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            textProperties.alignment = Layout.Alignment.ALIGN_CENTER
            add(TextCell("R2 C1", textProperties, halfWidth))
            add(TextCell("R2 C2", textProperties, halfWidth))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            textProperties.alignment = Layout.Alignment.ALIGN_OPPOSITE
            add(TextCell("R3 C1", textProperties, halfWidth))
            add(TextCell("R3 C2", textProperties, halfWidth))
            rows.add(this)
        }

        textProperties.alignment = Layout.Alignment.ALIGN_NORMAL

        /*
         * This will add a table with 3 different width column and N rows
         */
        LinkedList<Cell>().apply {
            add(TextCell("R4 C1", textProperties, halfWidth))
            add(TextCell("R4 C2", textProperties, halfWidth/2))
            add(TextCell("R4 C3", textProperties, halfWidth/2))
            rows.add(this)
        }

        LinkedList<Cell>().apply {
            add(TextCell("R5 C1", TextProperties(), halfWidth/2))
            add(TextCell("R5 C2", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_CENTER }, halfWidth))
            add(TextCell("R5 C3", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_OPPOSITE }, halfWidth/2))
            rows.add(this)
        }

        simplyPdfDocument.table.draw(rows, properties)


        // ---- New page --- Tests images inside cell


        /*
         * This will add bitmap and image to the table along with text as description
         */
        simplyPdfDocument.newPage()
        simplyPdfDocument.text.write("Drawing bitmaps inside tables", textProperties)
        rows.clear()

        val redBmp = Bitmap.createBitmap(200, 100, Bitmap.Config.RGB_565).apply {
            eraseColor(Color.RED)
        }
        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(redBmp, ImageProperties(), halfWidth))
            add(TextCell("This is a red bitmap, start aligned", TextProperties().apply {
                textSize = 16 }, halfWidth))
        })

        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(redBmp, ImageProperties().apply { alignment = Composer.Alignment.CENTER }, halfWidth))
            add(TextCell("This is a red bitmap, center aligned", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_CENTER
                textSize = 16
            }, halfWidth))
        })

        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(redBmp, ImageProperties().apply { alignment = Composer.Alignment.END }, halfWidth))
            add(TextCell("This is a red bitmap, end aligned", TextProperties().apply {
                alignment = Layout.Alignment.ALIGN_OPPOSITE
                textSize = 16
            }, halfWidth))
        })

        val greenBmp = Bitmap.createBitmap(simplyPdfDocument.usablePageWidth,
            500, Bitmap.Config.RGB_565).apply {
            eraseColor(Color.GREEN)
        }
        rows.add(LinkedList<Cell>().apply {
            add(ImageCell(greenBmp, ImageProperties(), halfWidth))
            add(TextCell("This is a green bitmap, which has been shrunk to fit within cell", TextProperties().apply {
                textSize = 32 }, halfWidth))
        })

        simplyPdfDocument.table.draw(rows, properties)
        redBmp.recycle()
        greenBmp.recycle()

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
            .pageHeader(PageHeader(LinkedList<Cell>().apply {
                add(TextCell("PDF Generated Using SimplyPDF", TextProperties().apply {
                    textSize = 24
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#000000"
                }, Cell.MATCH_PARENT))
                add(TextCell("Version 2.0.0", TextProperties().apply {
                    textSize = 18
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#000000"
                }, Cell.MATCH_PARENT))
            }))
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
            .pageHeader(PageHeader(LinkedList<Cell>().apply {
                TextCell("PDF Generated Using SimplyPDF", TextProperties().apply {
                    textSize = 32
                    alignment = Layout.Alignment.ALIGN_CENTER
                    textColor = "#88000000"
                }, Cell.MATCH_PARENT)
            }))
            .build()
        
        val exceptionHandler = CoroutineExceptionHandler { _, throwable -> 
            Log.e(MainActivity::class.java.simpleName, "JSON to PDF exception.", throwable)
        }

        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            usingJson(this@MainActivity, simplyPdfDocument, JSONStruct.payload)
        }
    }
}