package com.tivenwang.blog.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tivenwang.blog.config.GlobalConfig;


/** 
 * @author Pecan 
 * 类说明 
 * 加载配置文件
 */
public class CustomConfigPropertites {
	private static Logger logger=Logger.getLogger(CustomConfigPropertites.class);
	private static Properties p ;
	private static String testUsers=null;
	static{
		loadConfFromFile();
	}
    private static void loadConfFromFile(){
        p  = new Properties();
        try {
			p.load(CustomConfigPropertites.class.getResourceAsStream("/custom.properties"));
		} catch (IOException e) {
			logger.error(GlobalConfig.EMPTY_STRING, e);
		}
    }
    /**
     * 
     * @param key
     */
    public static String getPropertites(String key){
    	return p.getProperty(key);
    }
    
    
    /**
     * 用来判断用户是否是测试用户
     * @param Uuid
     * @return
     */
    public static boolean isTestUser(String Uuid){
    	if (null==testUsers) {
    		testUsers=p.getProperty("testUsers");
		}
    	return testUsers.contains(Uuid);
    }
    
    public static void main(String[] args) {
		System.out.println(CustomConfigPropertites.getPropertites("testUsers"));
	}
}
