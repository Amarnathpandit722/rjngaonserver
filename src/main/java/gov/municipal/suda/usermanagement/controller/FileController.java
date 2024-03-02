package gov.municipal.suda.usermanagement.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gov.municipal.suda.modules.property.model.master.PaymentReciptDto;
import gov.municipal.suda.usermanagement.service.FileStorageServiceImpl;
import gov.municipal.suda.usermanagement.dao.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.zxing.WriterException;
import com.itextpdf.text.pdf.PdfWriter;
import gov.municipal.suda.message.ResponseFile;
import gov.municipal.suda.message.ResponseMessage;
import gov.municipal.suda.usermanagement.model.FileDB;
import gov.municipal.suda.util.QRCodeGenerator;

@RestController
@CrossOrigin
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class FileController {
	private static final Logger logger = Logger.getLogger(FileController.class.getName());
	private static final String FILE_NAME = "C:/Users/USER/Desktop/RMC/itext.pdf";
	@Autowired
	private FileStorageServiceImpl storageService;
	@Autowired
	private FileDBRepository fileDBRepository;
	@Autowired
	private QRCodeGenerator qRCodeGenerator;

	//@PostMapping("/upload")
	public ResponseMessage uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return new ResponseMessage(message);
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return new ResponseMessage(message);
		}
	}

	@GetMapping("/files")
	public List<ResponseFile> getListFiles() {
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getId()).toUriString();

			return new ResponseFile(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return files;
	}

	@GetMapping("/files/{id}")
	public byte[] getFile(@PathVariable String id) {
		FileDB fileDB = storageService.getFile(id);
		return fileDB.getData();
	}
//	  @PostMapping("/uploadMultipleFiles")
//	    public List<ResponseFile> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//	        return Arrays.asList(files)
//	                .stream()
//	                .map(file -> upload(file))
//	                .collect(Collectors.toList());
//	    }

	//@PostMapping("/uploadMultipleFiles")
	public ResponseMessage uploadData(@RequestParam("files") MultipartFile[] files) throws Exception {
		String message = "";
		try {
			if (files == null || files.length == 0) {
				throw new RuntimeException("You must select at least one file for uploading");
			}

			StringBuilder sb = new StringBuilder(files.length);

			for (int i = 0; i < files.length; i++) {
				InputStream inputStream = files[i].getInputStream();
				String originalName = files[i].getOriginalFilename();
				String name = files[i].getName();
				String contentType = files[i].getContentType();
				long size = files[i].getSize();

				sb.append("File Name: " + originalName + "\n");

				logger.info("InputStream: " + inputStream);
				logger.info("OriginalName: " + originalName);
				logger.info("Name: " + name);
				logger.info("ContentType: " + contentType);
				logger.info("Size: " + size);
				String fileName = StringUtils.cleanPath(originalName);
				FileDB FileDB = new FileDB(fileName, contentType, files[i].getBytes());
				message = "Uploaded the file successfully: " + files[i].getOriginalFilename();
				fileDBRepository.save(FileDB);
				// message = "Uploaded the file successfully: " +
				// files[i].getOriginalFilename();
			}

			return new ResponseMessage(message);
		} catch (Exception e) {
			message = "Could not upload the file!";
			return new ResponseMessage(message);
		}
	}

	@GetMapping("/createPdf")
	public void pdfConvert() throws WriterException, IOException {
		Document document = new Document();
		byte[] image = new byte[0];
		try {
			image = qRCodeGenerator.getQRCodeImage(FILE_NAME, 250, 250);
			PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

			document.open();

			String imageFilePath = "C:/Users/USER/Desktop/RMC/New folder/images.png";
			Image jpg = Image.getInstance(imageFilePath);
			jpg.scalePercent(80, 80);
			jpg.setAlignment(Element.ALIGN_CENTER);
			document.add(jpg);

			Paragraph p = new Paragraph();
			p.add("CHHATISHGRAH MUNICIPAL CORPORATION");
			p.setAlignment(Element.ALIGN_CENTER);

			document.add(p);

			Font f = new Font();
			f.setStyle(Font.BOLD);
			f.setSize(8);

			Image img1 = Image.getInstance(image);
			img1.scalePercent(80, 80);
			img1.setAlignment(Element.ALIGN_CENTER);
			document.add(img1);

//          qRCodeGenerator.setLayout(new GridBagLayout());
//          GridBagConstraints cl;
//          cl = new GridBagConstraints();
//          cl.gridy = 0;
//          qRCodeGenerator.add(new JLabel("Hello"), cl);
			// close
			document.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
