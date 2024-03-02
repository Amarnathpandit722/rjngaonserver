package gov.municipal.suda.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Image;
import com.google.zxing.WriterException;

public class PdfWriteExample {

    private static final String FILE_NAME = "C:/Users/USER/Desktop/JMC/itext.pdf";

//    public static void main(String[] args) {
//        writeUsingIText();
//    }

    private static void writeUsingIText() {

        Document document = new Document();
        //String txt="Subhamay";
        byte[] image = new byte[0];
        try {
			image = QRCodeGenerator.getQRCodeImage(FILE_NAME,250,250);
			
			//QRCodeGenerator.generateQRCodeImage(FILE_NAME,250,250,FILE_NAME);
		} catch (WriterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        // Generate and Save Qr Code Image in static/image folder
        //String qrcode = Base64.getEncoder().encodeToString(image);
        //System.out.println("QR Code is....................."+qrcode);
        try {

            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

            document.open();
            
            String imageFilePath = "C:/Users/USER/Desktop/JMC/New folder/images.png";
            Image jpg = Image.getInstance(imageFilePath);
            jpg.scalePercent(80, 80);
            jpg.setAlignment(Element.ALIGN_CENTER);
            document.add(jpg);
            
            Paragraph p = new Paragraph();
            p.add("JHARKHAND MUNICIPAL CORPORATION");
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);
            
            Image img1=Image.getInstance(image);
            img1.scalePercent(80, 80);
            img1.setAlignment(Element.ALIGN_CENTER);
            document.add(img1);
            //close
            document.close();
            System.out.println("Done");
         
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
