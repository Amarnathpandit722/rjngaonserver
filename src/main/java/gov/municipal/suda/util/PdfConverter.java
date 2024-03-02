package gov.municipal.suda.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PdfConverter {

	public static String appraisalPdf(String outputFile, String username, String designation, String department,String appraisal_dt, String period_from, String period_to,String appraiser_comments,String action_plan,String recommendation,String hr_comments,String appraiser, String appraiser_designation) {
		String result = outputFile;
		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(outputFile));
			document.open();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Font bold = new Font(FontFamily.HELVETICA, 14, Font.BOLD | Font.UNDERLINE);
		Font bold2 = new Font(FontFamily.HELVETICA, 12);
		
		Font bold3 = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
		Font bold4 = new Font(FontFamily.HELVETICA, 12, Font.BOLD);

		Paragraph paragraph = new Paragraph("APPRAISAL FORM", bold);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		paragraph.setSpacingBefore(50);
		paragraph.setSpacingAfter(25);

		Paragraph p2 = new Paragraph("Name :- " + username, bold2);
		p2.setSpacingAfter(5);

		Paragraph p3 = new Paragraph("Designation :- " + designation, bold2);
		p3.setSpacingAfter(5);

		Paragraph p4 = new Paragraph("Department :- " + department + "        Appraisal Date :-" + appraisal_dt, bold2);
		p4.setSpacingAfter(5);

		Paragraph p5 = new Paragraph("Period covered for this Appraisal From :- " + period_from + " to " + period_to,
				bold2);
		p5.setSpacingAfter(5);

		PdfPTable table = new PdfPTable(6);

		PdfPCell cell1 = new PdfPCell(new Paragraph("Unsatisfactory"));
		PdfPCell cell2 = new PdfPCell(new Paragraph("Poor"));
		PdfPCell cell3 = new PdfPCell(new Paragraph("Marginal"));
		PdfPCell cell4 = new PdfPCell(new Paragraph("Satisfactory"));
		PdfPCell cell5 = new PdfPCell(new Paragraph("Highly satisfactory"));
		PdfPCell cell6 = new PdfPCell(new Paragraph("Exceptional"));
		PdfPCell cell7 = new PdfPCell(new Paragraph("0"));
		PdfPCell cell8 = new PdfPCell(new Paragraph("1"));
		PdfPCell cell9 = new PdfPCell(new Paragraph("2"));
		PdfPCell cell10 = new PdfPCell(new Paragraph("3"));
		PdfPCell cell11 = new PdfPCell(new Paragraph("4"));
		PdfPCell cell12 = new PdfPCell(new Paragraph("5"));
		table.setWidthPercentage(100);
		table.addCell(cell1);
		table.addCell(cell2);
		table.addCell(cell3);
		table.addCell(cell4);
		table.addCell(cell5);
		table.addCell(cell6);
		table.addCell(cell7);
		table.addCell(cell8);
		table.addCell(cell9);
		table.addCell(cell10);
		table.addCell(cell11);
		table.addCell(cell12);
		table.setSpacingAfter(10);

		Paragraph p6 = new Paragraph(
				"*Note:  Ratings can be given according to the above mentioned assessment scale and N/A can be ",
				bold2);
		p6.setSpacingAfter(5);

		try {
			document.add(paragraph);
			document.add(p2);
			document.add(p3);
			document.add(p4);
			document.add(p5);
			document.add(table);
			document.add(p6);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PdfPTable table2 = new PdfPTable(5);
		PdfPCell cell_2_1 = new PdfPCell(new Paragraph("Sr No.", bold4));
		PdfPCell cell_2_2 = new PdfPCell(new Paragraph("Rating Factors", bold4));
		PdfPCell cell_2_3 = new PdfPCell(new Paragraph("Ratings", bold4));
		PdfPCell cell_2_4 = new PdfPCell(new Paragraph("Rating %", bold4));
		PdfPCell cell_2_5 = new PdfPCell(new Paragraph("Comments", bold4));
		table2.setWidthPercentage(100);
		table2.addCell(cell_2_1);
		table2.addCell(cell_2_2);
		table2.addCell(cell_2_3);
		table2.addCell(cell_2_4);
		table2.addCell(cell_2_5);

		int Sr = 65, cnt = 1;
		int totalPoint = 0;
		double totalPer = 0;
//		for (Map.Entry mapElement : map.entrySet()) {
//			String key = (String) mapElement.getKey();
//			char ch = (char) Sr;
//			// Add some bonus marks
//			// to all the students and print it
//			
//
//			PdfPCell cell_1 = new PdfPCell(new Paragraph("" + ch,bold3));
//			
//			// tableRow.getCell(1).setText(""+key);
//			PdfPCell cell_2 = new PdfPCell(new Paragraph("" + key,bold3));
//			
//			PdfPCell cell_3 = new PdfPCell(new Paragraph(""));
//			PdfPCell cell_4 = new PdfPCell(new Paragraph(""));
//			PdfPCell cell_5 = new PdfPCell(new Paragraph(""));
//			List<AppraisalFormBean> l = (List<AppraisalFormBean>) mapElement.getValue();
//			table2.addCell(cell_1);
//			table2.addCell(cell_2);
//			table2.addCell(cell_3);
//			table2.addCell(cell_4);
//			table2.addCell(cell_5);
//			
//			
//			for (AppraisalFormBean obj : l) {
//
//				
//				PdfPCell cell__I1 = new PdfPCell(new Paragraph("" + cnt));
//				PdfPCell cell__I2 = new PdfPCell(new Paragraph(obj.getAnswer()));
//				PdfPCell cell__I3 = new PdfPCell(new Paragraph(obj.getRatings()));
//				PdfPCell cell__I4 = new PdfPCell(new Paragraph(obj.getRatingsPer() + "%"));
//				PdfPCell cell__I5 = new PdfPCell(new Paragraph(obj.getComment()));
//				totalPoint = totalPoint + Integer.parseInt(obj.getRatings());
//				totalPer = totalPer + Double.parseDouble(obj.getRatings());
//				cnt = cnt + 1;
//				table2.addCell(cell__I1);
//				table2.addCell(cell__I2);
//				table2.addCell(cell__I3);
//				table2.addCell(cell__I4);
//				table2.addCell(cell__I5);
//			}
//			Sr = Sr + 1;
//		}

		
		table2.setSpacingAfter(10);
		
		
		Paragraph p7 = new Paragraph( "Total Points:- " + totalPoint,bold4);
		p7.setSpacingAfter(10);
		
		Paragraph p8 = new Paragraph( "Total Percentage:- " + totalPer + "%",bold4);
		p8.setSpacingAfter(10);
		
		
		Paragraph p9 = new Paragraph("OVERALL ASSESSMENT",bold);
		p9.setAlignment(Element.ALIGN_CENTER);
		p9.setSpacingAfter(10);
		
		PdfPTable table3 = new PdfPTable(4);
		table3.setWidthPercentage(100);
		table3.setSpacingAfter(10);
//		for (int i = 0; i < category.size(); i++) {
//			
//			
//				
//				PdfPCell cell__I1 = new PdfPCell(new Paragraph(category.get(i)));
//				PdfPCell cell__I2 = new PdfPCell(new Paragraph(points.get(i)));
//				PdfPCell cell__I3 = new PdfPCell(new Paragraph(perc.get(i)));
//				PdfPCell cell__I4 = new PdfPCell(new Paragraph(des.get(i)));
//			table3.addCell(cell__I1);
//			table3.addCell(cell__I2);
//			table3.addCell(cell__I3);
//			table3.addCell(cell__I4);
//		}
		
		PdfPTable table4 = new PdfPTable(1);
		table4.setWidthPercentage(100);
		table4.addCell(new PdfPCell(new Paragraph("Comments and suggestions by the Appraiser: "+appraiser_comments)));
		table4.setSpacingAfter(10);
		
		PdfPTable table5 = new PdfPTable(1);
		table5.setWidthPercentage(100);
		table5.addCell(new PdfPCell(new Paragraph("Action plans for development: "+action_plan)));
		table5.setSpacingAfter(10);
		
		Paragraph p10 = new Paragraph("Appraiser's Name: "+appraiser +"                Designation: "+appraiser_designation);
		p10.setSpacingAfter(10);
		
		Paragraph p11 = new Paragraph("Signature                                                          Date:");
		p11.setSpacingAfter(10);
		
		PdfPTable table6 = new PdfPTable(1);
		table6.setWidthPercentage(100);
		table6.addCell(new PdfPCell(p10));
		table6.addCell(new PdfPCell(p11));
		table6.setSpacingAfter(10);
		
		PdfPTable table7 = new PdfPTable(1);
		table7.setWidthPercentage(100);
		table7.addCell(new Paragraph("Recommendations: "+recommendation));
		
		table7.setSpacingAfter(10);
		
		PdfPTable table8 = new PdfPTable(1);
		table8.setWidthPercentage(100);
		table8.addCell(new Paragraph("HR Comments:"+hr_comments));
		table8.setSpacingAfter(20);
		table8.addCell(new Paragraph("Signature:                                                   Date:"));
		
		Paragraph p12 = new Paragraph("Approved by CEO:",bold);
		p12.setSpacingAfter(10);
		Paragraph p13 = new Paragraph("DATE:",bold);
		
		
		try {
			document.add(table2);
			document.add(p7);
			document.add(p8);
			document.add(p9);
			document.add(table3);
			document.add(table4);
			document.add(table5);
			document.add(table6);
			document.add(table7);
			document.add(table8);
			document.add(p12);
			document.add(p13);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document.close();
		return result;
	}
}
