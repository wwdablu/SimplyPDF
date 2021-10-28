[![](https://jitpack.io/v/wwdablu/SimplyPDF.svg)](https://jitpack.io/#wwdablu/SimplyPDF)  

# SimplyPDF  
SimplyPDF is an android library that allows developers to generate PDF documents from within their application. It provides various composers which are used to draw various elements. It also provides a JSON template which can be used to create PDF documents. This will be useful to control the template from remote server and replace content provided by user, like forms.  

## Using it  
You can use it in gradle as following:  
~~~

maven { url 'https://jitpack.io' }

dependencies {
    implementation 'com.github.wwdablu:SimplyPDF:2.0.0'
}
~~~  
  
## PDF Document  
To obtain the PDF document, the developer needs to use the SimplyPdf class to obtain the SimplyPdfDocument. This contains the methods to access the various properties and methods for the PDF document. This is also used by the Composers to perform the required operation to write the data on it.  
  
~~~
simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
    .colorMode(DocumentInfo.ColorMode.COLOR)
    .paperSize(PrintAttributes.MediaSize.ISO_A4)
    .margin(Margin(startMargin, topMargin, endMargin, bottomMargin)
    .pageModifier(PageHeader())
    .firstPageBackgroundColor(Color.WHITE)
    .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
    .build();
~~~  

The above setup means that a PDF document named test will be created in the external directory. The PDF document will be in color mode with the paper size as A4. The specified margins will be used and the print orientation will be portrait.
Also the background color of the first page will be white in color. Along with it page modifiers are also supported.
They are applied to each of the pages when created. One example (provided support by the library) is a page header. This
will get added everytime a new page is added to the document.

Following are the methods provided by the SimplyPdfDocument for use.
| Method/Properties                                       | Usage         |
| ------------------------------------------------------- |:-------------:|
| insertEmptyLines(LineCount)                             | Inserts number of empty lines in the document |
| insertEmptySpace(Height)                                | Inserts an empty content of the specified height |
| newPage(Color)                                          | Inserts a new page with the specific background color. Default: White |
| addContentHeight(Height)                                | Used by composers. Used to specify how much content drawn in the page |
| finish()                                                | Completes all the rendering and saves the PDF file. |
| text                                                    | Access the default text composer |
| image                                                   | Access the default image composer |
| shape                                                   | Access the default shape composer |
| table                                                   | Access the default table composer |
| usablePageWidth                                         | Usable width of the page considering margins |
| usablePageHeight                                        | Usable height of the page considering margins |
| currentPage                                             | Page object of the PDF Document. Can be used to access the page canvas |
| currentPageNumber                                       | Page number being currently worked on |
| pageContentHeight                                       | Height of the contents rendered in the current page |
| documentInfo                                            | DocumentInfo for the current PDF Document |
| lineHeight                                              | Height of a line considered by insertEmptyLines() |

*Note:* The document will be created once finish() method is called. Till then everything is occurring in-memory.

## Composers
The classes which are responsible for drawing content onto the PDF are called Composers in this library. Currently these are the supported composers:
* TextComposer
* ShapeComposer
* ImageComposer
* TableComposer
These composers allow the developer to write text, draw shapes and bitmaps on the PDF document.
TableComposer supports displaying of texts and images. Shapes are currently not supported.
To create a custom composer, one needs to extend the UnitComposer class.

## TextComposer
TextComposer is used to perform Text operations on the document.
| Method/Properties                                               | Usage         |
| --------------------------------------------------------------- |:-------------:|
| write(String, TextProperties, PageWidth, xMargin, yMargin)      | Writes the text with the specified width by applying the margins. |

PageWidth has a default value set to usablePageWidth.
xMargin by default is set to 0.
yMargin by default is set to 0.


## TextProperties
TextProperties are used to assign various properties that are associated with the TextComposer.
| Method/Properties              | Usage         |
| ------------------------------ |:-------------:|
| textSize                       | Set the size of the text to be displayed |
| textColor                      | Set the color of the text to be displayed with |
| typeface                       | Typeface to be used to display the text content |
| alignment                      | Alignment of the center like Normal, Center, Opposite |
| underline                      | Should the text be underlined |
| strikethrough                  | Should the text be strikethrough |
| bulletSymbol                   | The character to be displayed for bullet content |


*__Sample__*:
~~~
simplyPdfDocument.text.write("Hello World", TextProperties().apply {
    textSize = 12
    textColor = "#000000"
    typeface = Typeface.DEFAULT
})
~~~


## ImageComposer
ImageComposer is used to perform bitmap rendering or draw operations on the document.
| Method/Properties                                               | Usage         |
| --------------------------------------------------------------- |:-------------:|
| drawBitmap(Bitmap, ImageProperties, xMargin, yMargin)           | Draws the bitmap with the given properties and margins. |
| drawFromUrl(String, Context, ImageProperties, xMargin, yMargin) | Downloads the image from the URL and draws the bitmap. |

xMargin by default is set to 0.
yMargin by default is set to 0.

*Note*: The bitmap resource handling should be done by the caller. Any bitmaps received from outside the
library recycle() will not be called on them.

## ImageProperties
ImageProperties are used to assign various properties that are associated with the ImageComposer.
| Method/Properties              | Usage         |
| ------------------------------ |:-------------:|
| alignment                      | Alignment of the center like Start, Center, End |


*__Sample__*:
~~~
simplyPdfDocument.image.drawBitmap(bitmap, ImageProperties().apply {
    alignment = Composer.Alignment.START
})
~~~


## TableComposer
TableComposer is used to draw tables and show text and image contents inside it. It uses Cell objects
to render the contents. The library provides support for TextCell to draw texts and ImageCell to draw
bitmaps inside the table cell.
| Method/Properties                                               | Usage         |
| --------------------------------------------------------------- |:-------------:|
| draw(LinkedList<LinkedList<Cell>>)                              | Draws the cells in row and columns |

*__TextCell__*:
The TextCell encapsulates the TextComposer. It takes in the text content, the TextProperties and also the
width of the cell compared to the width of the table.

*__ImageCell__*:
The TextCell encapsulates the ImageComposer. It takes in the bitmap, the ImageProperties and also the
width of the cell compared to the width of the table.

## TableProperties
TableProperties are used to assign various properties that are associated with the TableComposer.
| Method/Properties              | Usage         |
| ------------------------------ |:-------------:|
| borderColor                    | The color of the border lines used to draw the table |
| borderWidth                    | The width of the border lines used to draw the table |
| drawBorder                     | Whether to draw the border or not |


*__Sample__*:
~~~
val rows = LinkedList<LinkedList<Cell>>().apply {

    //Row - 1
    add(LinkedList<Cell>().apply {

        //Column - 1
        add(TextCell("Hello World", TextProperties().apply {
            textSize = 12
            textColor = "#000000"
        }, Cell.MATCH_PARENT))
    })

    //Row - 2
    add(LinkedList<Cell>().apply {

        //Column - 1
        add(TextCell("Text Cell", TextProperties().apply {
            textSize = 12
            textColor = "#000000"
        }, simplyPdfDocument.usablePageWidth/2))

        //Column - 2
        add(ImageCell(bitmap, ImageProperties(), simplyPdfDocument.usablePageWidth/2))
    })
}

simplyPdfDocument.table.draw(rows, TableProperties().apply {
    borderColor = "#000000"
    borderWidth = 1
    drawBorder = true
})
~~~


## Generating the PDF
Once all the content are written, the developer needs to be call finish() on SimplyPdfDocument.
~~~
simplyPdfDocument.finish();
~~~
This would generate the document in the given path.

## Generating from JSON
There is a defined structure which can be used to generate the PDF document. In here no Composers are required. The library does everything internally based on the JSON provided. Consider the below example:
~~~
{
    "page" : [
        {
            "type" : "setup",
            "margin" : {
                "start" : 125,
                "top" : 125,
                "end" : 125,
                "bottom" : 125
            },
            "backgroundcolor" : "#C8C8C8"
        },
        {
            "type" : "header",
            "contents" : [
                {
                    "type" : "text",
                    "content" : "Simplypdf",
                    "properties" : {
                        "size" : 24,
                        "color" : "#000000",
                        "underline" : false,
                        "strikethrough" : false,
                        "alignment" : "ALIGN_CENTER"
                    }
                },
                {
                    "type" : "text",
                    "content" : "Version 2.0",
                    "properties" : {
                        "size" : 20,
                        "color" : "#000000",
                        "underline" : true,
                        "strikethrough" : false,
                        "alignment" : "ALIGN_CENTER"
                    }
                }
            ]
        }
    ],
    "contents" : [
        {
            "type" : "text",
            "content" : "Simplypdf developed by Soumya Kanti Kar",
            "properties" : {
                "size" : 24,
                "color" : "#000000",
                "underline" : true,
                "strikethrough" : false
            }
        },
        {
            "type" : "image",
            "source" : "https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4",
            "format" : "url"
        },
        {
            "type" : "text",
            "content" : "Source code published in GitHub",
            "properties" : {
                "size" : 20,
                "color" : "#000000",
                "underline" : true,
                "strikethrough" : false
            }
        },
        {
            "type" : "shape",
            "shape" : "circle",
            "radius" : 50,
            "properties" : {
                "linecolor" : "#000000",
                "linewidth" : 1,
                "shouldfill" : true
            }
        },
        {
            "type" : "shape",
            "shape" : "box",
            "width" : 50,
            "height" : 50,
            "properties" : {
                "linecolor" : "#000000",
                "linewidth" : 1,
                "shouldfill" : true
            }
        },
        {
            "type" : "newpage"
        },
        {
            "type" : "space",
            "height" : 25
        },
        {
            "type" : "table",
            "contents" : [
                {
                    "row" : [
                        {
                            "type" : "text",
                            "content" : "Version",
                            "width" : 50,
                            "properties" : {
                                "size" : 24,
                                "color" : "#000000",
                                "underline" : true,
                                "strikethrough" : false
                            }
                        },
                        {
                            "type" : "text",
                            "content" : "2.0.0",
                            "width" : 50,
                            "properties" : {
                                "size" : 24,
                                "color" : "#000000",
                                "underline" : true,
                                "strikethrough" : false
                            }
                        }
                    ]
                },
                {
                    "row" : [
                        {
                            "type" : "text",
                            "content" : "Source Code Available in GitHub",
                            "width" : 100,
                            "properties" : {
                                "size" : 24,
                                "color" : "#000000",
                                "underline" : true,
                                "strikethrough" : false
                            }
                        }
                    ]
                }
            ],
            "properties" : {
                "width" : 1,
                "color" : "#000000"
            }
        }
    ]
}
~~~

To use the above JSON to generate the PDF we need to just call:
~~~
val simplyPdfDocument = SimplyPdf.with(context,
        File(Environment.getExternalStorageDirectory().absolutePath + "/json_to_pdf.pdf"))
    .colorMode(DocumentInfo.ColorMode.COLOR)
    .paperSize(PrintAttributes.MediaSize.ISO_A4)
    .margin(Margin(start, top, end, bottom))
    .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
    .build()

val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e(MainActivity::class.java.simpleName, "JSON to PDF exception.", throwable)
}

CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
    SimplyPdf.usingJson(context, simplyPdfDocument, JSONStruct.payload)
    withContext(Dispatchers.Main) {
        Toast.makeText(context, "JSON to PDF Completed", Toast.LENGTH_SHORT).show()
    }
}
~~~  

This will generate the PDF. This JSON contains all the supported properties for each for the properties for the supported composers. 
