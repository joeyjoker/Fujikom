package com.joey.Fujikom.modules.spi.utils;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.joey.Fujikom.common.config.Global;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileS3Utils {

	private static Logger logger = Logger.getLogger(FileS3Utils.class);

	private static String serverPath = Global.getServerFilePath();

	private static String s3Path = Global.getS3FilePath();

	private static String bucket = Global.getBucket();

	private static String fileLocation = Global.getFileLocation();
	
	public static String ImageUpload(byte[] bytes,
			String type) throws Exception {
		
		if ("local".equals(fileLocation)) {
			return imageUploadToLocal(bytes, type);
		} else {
			return imageUploadToS3(bytes, type);
		}
	}

	public static String fileUpload( MultipartFile fileinfo,
			String type) throws Exception {
		
		if ("local".equals(fileLocation)) {
			return fileUploadToLocal( fileinfo, type);
		} else {
			return fileUploadToS3(fileinfo, type);
		}
	}

	public static String adFileUpload(MultipartFile fileinfo, String type)
			throws Exception {
		if ("local".equals(fileLocation)) {
			return fileUploadToLocal(fileinfo, type);
		} else {
			return fileUploadToS3( fileinfo, type);
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param memberId
	 * @param fileinfo
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String fileUploadToLocalNew(Long memberId,
			MultipartFile fileinfo, String type) throws Exception {

		logger.info("serverPath==>" + serverPath);
		String returnPath = getFolderName() + "/" + memberId + "/" + getUUID()
				+ "." + type;
		String filePath = serverPath + "/" + returnPath;
		String folder = serverPath + "/" + getFolderName() + "/" + memberId;

		logger.info("filePath==>" + filePath);

		// step1:创建文件夹
		File folderFile = new File(folder);
		if (!folderFile.isDirectory()) {
			boolean mkdirs = folderFile.mkdirs();
			logger.info("mkDIR==>" + mkdirs);
		}

		// step 2: 写入文件
		FileOutputStream fos = null;
		try {
			if (!fileinfo.isEmpty()) {
				// 解码
				byte[] bytes = fileinfo.getBytes();
				fos = new FileOutputStream(filePath);
				fos.write(bytes); // 写入文件
			}
		} catch (Exception e) {
			logger.error("FileUpload.fileUpload error", e);
			return null;
		} finally {
			if (fos != null)
				fos.close();
		}
		return returnPath;
	}

	/**
	 * 文件上传
	 * 
	 * @param fileinfo
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String fileUploadToLocal(
			MultipartFile fileinfo, String type) throws Exception {

		logger.info("serverPath==>" + serverPath);
		String returnPath = getFolderName() +  "/" + getUUID()
				+ "." + type;
		String filePath = serverPath + "/" + returnPath;
		String folder = serverPath + "/" + getFolderName();

		logger.info("filePath==>" + filePath);

		// step1:创建文件夹
		File folderFile = new File(folder);
		if (!folderFile.isDirectory()) {
			boolean mkdirs = folderFile.mkdirs();
			logger.info("mkDIR==>" + mkdirs);
		}

		// step 2: 写入文件
		FileOutputStream fos = null;
		try {
			if (!fileinfo.isEmpty()) {
				// 解码
				byte[] bytes = fileinfo.getBytes();
				fos = new FileOutputStream(filePath);
				fos.write(bytes); // 写入文件
			}
		} catch (Exception e) {
			logger.error("FileUpload.fileUpload error", e);
			return null;
		} finally {
			if (fos != null)
				fos.close();
		}
		return returnPath;
	}

	public static String fileUploadToS3(
			MultipartFile fileinfo, String type) throws IOException {

		String returnPath = getFolderName()  + "/" + getUUID()
				+ "." + type;
		String filePath = s3Path + "/" + returnPath;

		logger.debug("filePath==>" + filePath);

		byte[] bytes = fileinfo.getBytes();
		java.io.ByteArrayInputStream imageInputStream = new java.io.ByteArrayInputStream(
				bytes);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		if ("jpg".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
		} else if ("gif".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_GIF_VALUE);
		} else if ("png".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
		} else {
			objectMetadata
					.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		}

		logger.debug("bytes.length==>" + bytes.length);
		objectMetadata.setContentLength(bytes.length);
		objectMetadata.setLastModified(new Date());

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
				returnPath, imageInputStream, objectMetadata);
		// putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		S3Connection.getConnection().putObject(putObjectRequest);
		return returnPath;
	}

	/**
	 * 已时间为文件夹名 格式：yyyymmdd
	 * 
	 * @return
	 */
	private static String getFolderName() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(date);
	}

	/**
	 * 
	 * 获取UUID作为文件名
	 * 
	 * @return
	 */
	private static String getUUID() {
		return (UUID.randomUUID().toString().replace("-", ""));
	}

	public static String webFileUploadToS3(byte[] bytes, String type) {

		String returnPath = getFolderName() + "/" + "ad" + "/" + getUUID()
				+ "." + type;
		String filePath = s3Path + "/" + returnPath;

		logger.debug("filePath==>" + filePath);

		// byte[] bytes = fileinfo.getBytes();
		java.io.ByteArrayInputStream imageInputStream = new java.io.ByteArrayInputStream(
				bytes);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		if ("jpg".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
		} else if ("gif".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_GIF_VALUE);
		} else if ("png".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
		} else {
			objectMetadata
					.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		}

		logger.debug("bytes.length==>" + bytes.length);
		objectMetadata.setContentLength(bytes.length);
		objectMetadata.setLastModified(new Date());

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
				returnPath, imageInputStream, objectMetadata);
		S3Connection.getConnection().putObject(putObjectRequest);
		return returnPath;
	}

	/**
	 * 文件上传
	 * 
	 * @param memberId
	 * @param fileinfo
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String webFileUploadToLocal(byte[] bytes, String type)
			throws IOException {

		String returnPath = getFolderName() + "/" + "ad" + "/" + getUUID()
				+ "." + type;
		String filePath = serverPath + "/" + returnPath;

		logger.debug("filePath==>" + filePath);
		String folder = serverPath + "/" + getFolderName() + "/" + "ad";
		// step1:创建文件夹
		File folderFile = new File(folder);
		if (!folderFile.isDirectory()) {
			boolean mkdirs = folderFile.mkdirs();
			logger.info("mkDIR==>" + mkdirs);
		}

		// step 2: 写入文件
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(bytes);// 写入文件
		} catch (Exception e) {
			logger.error("FileUpload.fileUpload error", e);
			return null;
		} finally {
			if (fos != null)
				fos.close();
		}
		return returnPath;
	}
	
	
	
	public static String imageUploadToLocal(byte[] bytes, String type)
			throws IOException {

		String returnPath = getFolderName() + "/" + "ad" + "/" + getUUID()
				+ "." + type;
		String filePath = serverPath + "/" + returnPath;

		logger.debug("filePath==>" + filePath);
		String folder = serverPath + "/" + getFolderName() + "/" + "ad";
		// step1:创建文件夹
		File folderFile = new File(folder);
		if (!folderFile.isDirectory()) {
			boolean mkdirs = folderFile.mkdirs();
			logger.info("mkDIR==>" + mkdirs);
		}

		// step 2: 写入文件
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(bytes);// 写入文件
		} catch (Exception e) {
			logger.error("FileUpload.fileUpload error", e);
			return null;
		} finally {
			if (fos != null)
				fos.close();
		}
		return returnPath;
	}
	
	
	public static String imageUploadToS3(byte[] bytes, String type)
			throws IOException {

		String returnPath = getFolderName()  + "/" + getUUID()
				+ "." + type;
		String filePath = s3Path + "/" + returnPath;

		logger.debug("filePath==>" + filePath);

		java.io.ByteArrayInputStream imageInputStream = new java.io.ByteArrayInputStream(
				bytes);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		if ("jpg".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
		} else if ("gif".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_GIF_VALUE);
		} else if ("png".equalsIgnoreCase(type)) {
			objectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
		} else {
			objectMetadata
					.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		}

		logger.debug("bytes.length==>" + bytes.length);
		objectMetadata.setContentLength(bytes.length);
		objectMetadata.setLastModified(new Date());

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
				returnPath, imageInputStream, objectMetadata);
		// putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		S3Connection.getConnection().putObject(putObjectRequest);
		return returnPath;
	}
}
