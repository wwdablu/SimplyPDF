# SimplyPDF  
SimplyPDF is an android library that allows developers to generate PDF documents from within their application. It provides various composers which are used to draw various elements.  

## Composers  
The classes which are responsible for drawing content onto the PDF are called Composers in this library. Currently there are three different composers:  
* TextComposer  
* ShapeComposer  
* ImageComposer  
These composers allow the developer to write text, draw shapes and bitmaps on the PDF document.  

## Composer Properties  
Each composer class has it properties. They can be used to set the behavior of the composer. For example:  
~~~
TextComposer.Properties properties = new TextComposer.Properties();
properties.textSize = 16;
properties.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
textComposer.write("Demonstrate the usage of TextComposer.", properties);
~~~  
This will write the text in text size 16 in bold font. But, if we change the `textSize` property to 24 and call `textComposer.write` once again, then the text will now be drawn with font size as 24.  

## Using SimplyPDF  
To use SimplyPDF, we need to create a SimplyPDFDocument. Here is the sample code for it:  
~~~
simplyPdfDocument = SimplyPdf.with(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf"))
    .colorMode(DocumentInfo.ColorMode.COLOR)
    .paperSize(PrintAttributes.MediaSize.ISO_A4)
    .margin(DocumentInfo.Margins.DEFAULT)
    .paperOrientation(DocumentInfo.Orientation.PORTRAIT)
    .build();
~~~  
  
This provides the information to SimplyPDF about the PDF document that is going to be generated. Once done, SimplyPDFDocument provides the composers to write content onto the PDF.  

~~~
textComposer = simplyPdfDocument.getTextComposer();
shapeComposer = simplyPdfDocument.getShapeComposer();
imageComposer = simplyPdfDocument.getImageComposer();
~~~  

## Generating the PDF  
Once all the content are written, the developer needs to be call finish().  
~~~
simplyPdfDocument.finish();
~~~  
This would generate the document in the given path.  

All these are applicable in Version 1.x.x.  
