package com.bonc.turing.cms.common.utils;
/**
*   
*   @Description	load config file and read config     
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
	
	private static final Log LOG = LogFactory.getLog("file");
	private static Properties property = null;
	private static final LoadConfig config = new LoadConfig();
	//singleton
	private LoadConfig(){}
	
	static String configurePath = "application.properties";
	// load config 
	static
	{
		property = new Properties();
		InputStream fileStream = null;
		try {
			fileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configurePath);
			property.load(fileStream);
			if(!property.containsKey("spring.profiles.active"))
			{
				try {
					throw new NoSuchFieldException("spring.profiles.active");
				} catch (NoSuchFieldException e) {
					LOG.error("NoSuchFieldException: "+"spring.profiles.active",e);
				}
			}
			String value = (String) property.get("spring.profiles.active");
			configurePath = "application-"+value+".properties";
			fileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configurePath);
			property.load(fileStream);
		} catch (Exception e) {
			LOG.error("error:", e);
		}
	}
	
	/**
	 * return value by key
	 * @param key
	 * @return value mapping with key
	 */
	public static String lookUpValueByKey(String key)
	{
		if(!property.containsKey(key))
		{
			try {
				throw new NoSuchFieldException(key);
			} catch (NoSuchFieldException e) {
				LOG.error("NoSuchFieldException: "+key,e);
			}
		}
		String value = (String) property.get(key);
		return value.trim();
	}
	
	
	/**
	 * 重新指定一个路径，加载配置
	 * @param path
	 */
	public static void reLoadPath(String path){
		property = new Properties();
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(path);
			property.load(fileStream);
		} catch (FileNotFoundException e) {
			LOG.error("error:", e);
		} catch (IOException e) {
			LOG.error("error:", e);
		}
	}
	
	public static LoadConfig getInstance()
	{
		return config;
	}

}
