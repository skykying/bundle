package com.lembed.lite.studio.report.pdf;


import java.awt.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * https://www.tutorialspoint.com/pdfbox/pdfbox_adding_rectangles.htm
 * @author Administrator
 *
 */
public class Verify {

	
	public void build() {

        String fileName = "c:/pdfWithImage.pdf";         //$NON-NLS-1$

        PDPage page = new PDPage();
        
        try (PDDocument doc = new PDDocument();
             PDPageContentStream content = new PDPageContentStream(doc, page)
            ){

            
            doc.addPage(page);

//            PDImageXObject pdImage = PDImageXObject.createFromFile("C:/PdfBox_Examples/logo.png", doc);
//            content.drawImage(pdImage, 70, 250);
            
          //Setting the non stroking color
            content.setNonStrokingColor(Color.DARK_GRAY);

            //Drawing a rectangle 
            content.addRect(200, 650, 100, 100);

            //Drawing a rectangle
            content.fill();
            doc.save(fileName);


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
	
}
