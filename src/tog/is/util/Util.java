package tog.is.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public class Util {
	public static String parseStrToMd5L32(String str){  
        String reStr = null;  
        try {  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] bytes = md5.digest(str.getBytes());  
            StringBuffer stringBuffer = new StringBuffer();  
            for (byte b : bytes){  
                int bt = b&0xff;  
                if (bt < 16){  
                    stringBuffer.append(0);  
                }   
                stringBuffer.append(Integer.toHexString(bt));  
            }  
            reStr = stringBuffer.toString();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return reStr;  
    }
	
	public static String parseQueryString(MultivaluedMap<String, String> queryParams, String key){
		String value = queryParams.getFirst(key);
		return value;
		
	}
	
	public static Integer parseQueryInt(MultivaluedMap<String, String> queryParams, String key){
		String value = queryParams.getFirst(key);
		if(value==null){
			return null;		
		}
		
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			return null;
		}		
	}
	
	public static Double parseQueryDouble(MultivaluedMap<String, String> queryParams, String key){
		String value = queryParams.getFirst(key);
		if(value==null){
			return null;		
		}
		
		try{
			return Double.parseDouble(value);
		}catch(Exception e){
			return null;
		}		
	}
	
	public static void MakeMissingDir(String path){
		File theDir = new File(path);
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		        //handle it
		    } 
		}
		
	}
	
	public static void outputImage(String targetFile, OutputStream out) throws IOException{
		FileInputStream fis = null;										
		fis = new FileInputStream(targetFile);
							
		byte[] imageBytes = new byte[1024 * 2^20 * 5];

		while (fis.read(imageBytes) != -1) {
			out.write(imageBytes);
		}
		out.flush();
		fis.close();
		
	}
	
	public static List<String> SortParameterKeys (List<String> keys, String uri){
		Collections.sort(keys,(new Comparator<String>() {
			private String uri = "";
			public Comparator<String> setUri(String uri){
				this.uri = uri;
				return this;
			}
		    public int compare(String key1, String key2) {
		        Integer i1 = uri.lastIndexOf(key1+"=");
		        Integer i2 = uri.lastIndexOf(key2+"=");
		        return (i1 > i2 ? 1 : (i1 == i2 ? 0 : -1));
		    }
		}).setUri(uri));
		
		return keys;
		
	}
}
