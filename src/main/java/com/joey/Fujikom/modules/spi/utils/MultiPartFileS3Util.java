package com.joey.Fujikom.modules.spi.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.apache.log4j.Logger;
import org.aspectj.util.FileUtil;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.joey.Fujikom.common.config.Global;

/**
 * 文件上传 按日期新建文件夹（20131219），上传的文件保存到服务器文件夹，文件名称为uuid 文件格式：图片、声音文件 传过来的参数 file
 * 应该是Base64编码后的二进制字符串 return URL
 * D:/upload\20131220\7f69a0932cbd489f9bd8ebe274457b47
 */
public class MultiPartFileS3Util {

	private static Logger logger = Logger.getLogger(FileUtil.class);

	private static String serverPath = Global.getServerFilePath();

	private static String s3Path = Global.getS3FilePath();

	private static String bucket = Global.getBucket();

	private static String fileLocation = Global.getFileLocation();

	public static String fileUpload(MultipartFile fileinfo,
			String type) throws Exception {
		
		if ("local".equals(fileLocation)) {
			return fileUploadToLocal(fileinfo, type);
		} else {
			return fileUploadToS3(fileinfo, type);
		}
	}

	public static String adFileUpload(MultipartFile fileinfo, String type)
			throws Exception {
		if ("local".equals(fileLocation)) {
			return fileUploadToLocal(fileinfo, type);
		} else {
			return fileUploadToS3(fileinfo, type);
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
	public static String fileUploadToLocalNew(Long EntityId,
			MultipartFile fileinfo, String type) throws Exception {

		logger.info("serverPath==>" + serverPath);
		String returnPath = getFolderName() + "/" + EntityId + "/" + getUUID()
				+ "." + type;
		String filePath = serverPath + "/" + returnPath;
		String folder = serverPath + "/" + getFolderName() + "/" + EntityId;

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
	 * @param memberId
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
		String folder = serverPath + "/" + getFolderName() ;

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
	
	/*public static String saveThumbnail(MultipartFile fileinfo, String type) throws IOException{
		String returnPath = getFolderName() + "/"  + getUUID()
				+ "." + type;
		 BufferedImage newImage=getNewImage(fileinfo,180,320);
		 ByteArrayOutputStream bs = new ByteArrayOutputStream();  
         
	        ImageOutputStream imOut; 
	        try { 
	            imOut = ImageIO.createImageOutputStream(bs); 
	             
	            ImageIO.write(newImage,type,imOut); 
	            InputStream is = null; 
	            is= new ByteArrayInputStream(bs.toByteArray()); 
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

	    		objectMetadata.setContentLength(bs.toByteArray().length);
	    		objectMetadata.setLastModified(new Date());

	    		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
	    				returnPath, is, objectMetadata); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        }  
		return returnPath;
	}*/
	
	
	public static  BufferedImage  getNewImage(MultipartFile oldImage,double width,double height) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(oldImage.getBytes());   
        MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(bais);        
        Image src = ImageIO.read(mciis);  
        BufferedImage tag = new BufferedImage((int)width,(int)height, BufferedImage.TYPE_3BYTE_BGR);  
            tag.getGraphics().drawImage(src, 0, 0, (int)width, (int)height, null); //绘制缩小后的图  
            return tag;
		
	}

	public static String fileUploadToS3(
			MultipartFile fileinfo, String type) throws IOException {
		
		/*String accessKeyID = Global.getAwsAccessKey();
		String secretKey = Global.getAwsSecretKey(); 
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		AWSCredentials credentials;  
        AmazonS3 s3Client;  
        credentials = new BasicAWSCredentials(accessKeyID, secretKey);  
        s3Client = new AmazonS3Client(credentials,clientConfig);*/ 

		String returnPath = getFolderName() + "/"  + getUUID()
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
		/*s3Client.setEndpoint("s3.cn-north-1.amazonaws.com.cn");
		s3Client.putObject(putObjectRequest);*/
		/*AmazonS3 s3Client = new AmazonS3Client(  
	            new BasicAWSCredentials(Global.getAwsAccessKey(),  
	                    Global.getAwsSecretKey())); 
		s3Client.setEndpoint("s3.amazonaws.com");
		s3Client.putObject(putObjectRequest);*/
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

}
