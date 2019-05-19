[![](https://jitpack.io/v/wwdablu/SimplyPDF.svg)](https://jitpack.io/#wwdablu/SimplyPDF)  

# SimplyPDF  
SimplyPDF is an android library that allows developers to generate PDF documents from within their application. It provides various composers which are used to draw various elements. It also provides a JSON template which can be used to create PDF documents. This will be useful to control the template from remote server and replace content provided by user, like forms.  

## Using it  
You can use it in gradle as following:  
~~~

maven { url 'https://jitpack.io' }

dependencies {
    implementation 'com.github.wwdablu:SimplyPDF:x.y.z'
}
~~~  
  
## PDF Document  
To obtain the PDF document, the developer needs to use the SimplyPdf class to obtain the SimplyPdfDocument. This contains the methods to access the various properties and methods for the PDF document. This is also used by the Composers to perform the required operation to write the data on it.  
  
~~~
simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
    .colorMode(DocumentInfo.ColorMode.COLOR)
    .paperSize(PrintAttributes.MediaSize.ISO_A4)
    .margin(DocumentInfo.Margins.DEFAULT)
    .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
    .build();
~~~  

The above setup means that a PDF document named test will be created in the external directory. The PDF document will be in color mode with the paper size as A4. The margins will be default and the print orientation will be portrait.  

*Note:* The document will be created once finish() method is called. Till then everything is occuring in-memory.  

## Composers  
The classes which are responsible for drawing content onto the PDF are called Composers in this library. Currently there are the supported composers:  
* TextComposer  
* ShapeComposer  
* ImageComposer 
* TableComposer  
These composers allow the developer to write text, draw shapes and bitmaps on the PDF document. Currently TableComposer only supports displaying text. In future it will support both Image and Shape.  

## Composer Properties  
Each composer class has it properties. They can be used to set the behavior of the composer. For example:  
~~~
TextProperties textProperties = new TextProperties();
textProperties.textSize = 24;
textProperties.alignment = Layout.Alignment.ALIGN_CENTER;
textProperties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

TextComposer textComposer = new TextComposer(simplyPdfDocument);
textComposer.write("Demonstrate the usage of TextComposer.", textProperties);
~~~  
This will write the text in text size 16 in bold font. But, if we change the `textSize` property to 24 and call `textComposer.write` once again, then the text will now be drawn with font size as 24.  

## Generating the PDF  
Once all the content are written, the developer needs to be call finish().  
~~~
simplyPdfDocument.finish();
~~~  
This would generate the document in the given path.  

## Generating from JSON  
There is a defined structure which can be used to generate the PDF document. In here no Composers are required. The library does everything internally based on the JSON provided. Consider the below example:  
~~~  
{
  "contents" : [
    {
      "type" : "text",
      "content" : "SimplyPdf developer, Soumya Kanti Kar",
      "properties" : {
        "size" : 24,
        "color" : "#000000",
        "underline" : true,
        "strikethrough" : false
      }
    },
    {
      "type" : "image",
      "imageurl" : "https://avatars0.githubusercontent.com/u/28639189?s=400&u=bd9a720624781e17b9caaa1489345274c07566ac&v=4"
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
      "width" : 100,
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
      "type" : "shape",
      "shape" : "freeform",
      "points" : [
        {
          "line" : [0,0]
        },
        {
          "line" : [100, 0]
        },
        {
          "line" : [100,100]
        },
        {
          "line" : [0,0]
        }
      ],
      "properties" : {
        "linecolor" : "#FF0000",
        "linewidth" : 4,
        "shouldfill" : true
      }
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
                "size" : 12,
                "color" : "#000000",
                "underline" : false,
                "strikethrough" : false
              }
            },
            {
              "type" : "text",
              "content" : "1.1.0",
              "width" : 50,
              "properties" : {
                "size" : 12,
                "color" : "#000000",
                "underline" : false,
                "strikethrough" : false
              }
            }
          ]
        },
        {
          "row" : [
            {
              "type" : "text",
              "content" : "Source code is available in GitHub",
              "width" : 100,
              "properties" : {
                "size" : 18,
                "color" : "#0000FF",
                "underline" : false,
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
SimplyPdfDocument simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_json.pdf"))
    .colorMode(DocumentInfo.ColorMode.COLOR)
    .paperSize(PrintAttributes.MediaSize.ISO_A4)
    .margin(DocumentInfo.Margins.DEFAULT)
    .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
    .build();

final DisposableObserver<Boolean> disposableObserver = SimplyPdf.use(this, simplyPdfDocument, jsonPayload)
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
~~~  

This will generate the PDF. This JSON contains all the supported properties for each for the properties for the supported composers. 
