package com.joey.Fujikom.modules.spi.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.joey.Fujikom.common.config.Global;

public class S3Connection {
    private static AmazonS3 s3client = null;

    /**
     * 与S3建立连接
     * 
     * @return
     */
    public static AmazonS3 getConnection() {
	if (s3client == null) {
	    /*s3client = new AmazonS3Client(
		    new InstanceProfileCredentialsProvider());
	    s3client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
	    s3client.setEndpoint("s3-ap-northeast-1.amazonaws.com");*/
	    
	    String accessKeyID = Global.getAwsAccessKey();
		String secretKey = Global.getAwsSecretKey(); 
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		AWSCredentials credentials;   
        credentials = new BasicAWSCredentials(accessKeyID, secretKey);  
        s3client = new AmazonS3Client(credentials,clientConfig);
        s3client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        s3client.setEndpoint("s3-ap-northeast-1.amazonaws.com");
       
	}
	return s3client;
    }

    //
    // public static void main(String[] args) {
    // try {
    // S3Service s3Service = S3Connection.getConnection();
    // S3Bucket joeybucket;
    // joeybucket = s3Service.getBucket("joeyangle");
    // String stringData = "Hello World!";
    // S3Object stringObject = new S3Object("new/Second2.txt", stringData);
    // stringObject = s3Service.putObject(joeybucket, stringObject);
    // System.out.println("S3Object after upload:" + stringObject);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
