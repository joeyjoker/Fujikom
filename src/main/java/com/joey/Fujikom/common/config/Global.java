/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/Fujikom">Fujikom</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.joey.Fujikom.common.config;

import com.google.common.collect.Maps;
import com.joey.Fujikom.common.utils.PropertiesLoader;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2013-03-23
 */
public class Global {
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("Fujikom.properties");
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = propertiesLoader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}

	/////////////////////////////////////////////////////////
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 获取CKFinder上传文件的根目录
	 * @return
	 */
	public static String getCkBaseDir() {
		String dir = getConfig("userfiles.basedir");
		Assert.hasText(dir, "配置文件里没有配置userfiles.basedir属性");
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
	
	public static String getServerFilePath(){
		return getConfig("serverPath");
		
	}
	
	public static String getFileLocation(){
		return getConfig("fileLocation");
		
	}
	
	
	public static String getBucket(){
		return getConfig("bucket");
		
	}
	
	public static String getS3FilePath(){
		return getConfig("s3.filepath");
		
	}
	
   public static String getAwsAccessKey(){
		
		return getConfig("aws_accessKey");
	}
	
   public static String getAwsSecretKey(){
		
		return getConfig("aws_secretKey");
	}
   
   public static String getPagesize(){
	   
	   return getConfig("pagesize");
   }
   
   public static String getMailSmtpHost(){
		return getConfig("MailSmtpHost");
		
	}
	
	public static String getMailSmtpUser(){
		
		return getConfig("MailSmtpUser");
		
		
	}
	
	public static String getMailSmtpPassword(){
		
		return getConfig("MailSmtpPassword");
		
	}
	public static String getMailFrom() {
		return getConfig("MailFrom");
		
	}
	
	public static String getServerPath() {
		return getConfig("ServerPath");
		
	}
	
	public static String getPayMethod() {
		return getConfig("PayMethod");
		
	}
	
	public static String getMerchantId() {
		return getConfig("MerchantId");
		
	}
	
	public static String getServiceId() {
		return getConfig("ServiceId");
		
	}
	
	
	public static String getSpsCustNo() {
		return getConfig("SpsCustNo");
		
	}
	
	public static String getTerminalType() {
		return getConfig("TerminalType");
		
	}
	
	public static String getSuccessUrl() {
		return getConfig("SuccessUrl");
		
	}
	
	public static String getCancelUrl() {
		return getConfig("CancelUrl");
		
	}
	
	public static String getErrorUrl() {
		return getConfig("ErrorUrl");
		
	}
	
	public static String getPageConUrl() {
		return getConfig("PageConUrl");
		
	}
	
	public static String getFree1() {
		return getConfig("free1");
		
	}
	
	public static String getFree2() {
		return getConfig("free2");
		
	}
	
	public static String getFree3() {
		return getConfig("free3");
		
	}
	
	public static String getLimitSecond() {
		return getConfig("LimitSecond");
		
	}
	
	
	
	
	
}
