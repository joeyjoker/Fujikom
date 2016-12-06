package com.joey.Fujikom.modules.spi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.joey.Fujikom.common.config.Global;


@Controller
@RequestMapping("spi/file")
public class FileController {

	private Logger log = Logger.getLogger(FileController.class);
	/*private final static double DEFAULT_USERID = Math.random() * 100000.0;*/
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@RequestParam String url,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if ("local".equals(Global.getFileLocation())) {
			getFileFromLocal(url, request, response);
		} else {
			getFileFromS3(url, response);
		}
	}

	@RequestMapping(value = "/downloadFileGet", method = RequestMethod.GET)
	public void downloadFileGet(@RequestParam String url,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {


		if ("local".equals(Global.getFileLocation())) {
			getFileFromLocal(url, request, response);
		} else {
			getFileFromS3(url, response);
		}
	}

	private void getFileFromS3(String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		InputStream inputStream = null;
		OutputStream outStream = null;
		S3Object object = null;
		try {
			log.debug("getFileFromS3=>start");
			if (url == null) {
				return;
			}
			String fullPath =  url;

			AmazonS3 s3Client = S3Connection.getConnection();

			log.debug("getFileFromS3=>1");

			try {
				object = s3Client.getObject(Global.getBucket(), fullPath);
			} catch (AmazonS3Exception e) {
				log.error("fullPath=>" + fullPath,e);
				
			}

			log.debug("getFileFromS3=>1.5");

			inputStream = object.getObjectContent();

			log.debug("getFileFromS3=>2");
			ObjectMetadata objectMetadata = object.getObjectMetadata();
			response.setContentType(objectMetadata.getContentType());
			response.setContentLength((int) objectMetadata.getContentLength());

			log.debug("getFileFromS3=>3");
			// set headers for the response
			String headerValue = "noname";
			int lastIndexOf = url.lastIndexOf("/");
			if (lastIndexOf != -1) {
				String fileName = url.substring(lastIndexOf + 1);
				headerValue = String.format("attachment; filename=\"%s\"",
						fileName);
			}

			log.debug("getFileFromS3=>4");

			String headerKey = "Content-Disposition";
			response.setHeader(headerKey, headerValue);
			outStream = response.getOutputStream();

			log.debug("getFileFromS3=>5");

			byte[] buffer = new byte[8192];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

		} catch (AmazonServiceException e) {
			throw e;
		} catch (AmazonClientException e) {
			throw e;
		} finally {
			if (object != null)
				object.close();
			if (inputStream != null)
				inputStream.close();
			if (outStream != null)
				outStream.close();
			log.debug("getFileFromS3=>end");
		}

	}
	

	private void getFileFromLocal(String url, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			IOException {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			// construct the complete absolute path of the file
			if (url == null) {
				return;
			}
			String fullPath = Global.getServerFilePath()+ "/" + url;

			File downloadFile = new File(fullPath);

			inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			/*HttpSession session = request.getSession();*/
			ServletContext context = request.getSession().getServletContext();
			String mimeType = context.getMimeType(fullPath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "image/jpeg";
			}
			System.out.println("MIME type: " + mimeType);

			// set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();

			outStream.close();
		} catch (IOException e) {
			//log.error("getFileFromLocal", e);
			return;
		} finally {
			if (outStream != null)
				outStream.close();
			if (inputStream != null)
				inputStream.close();
		}

	}
}
