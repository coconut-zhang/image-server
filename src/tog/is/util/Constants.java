package tog.is.util;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.im4java.core.ImageCommand;

public class Constants {
	public static final String ItemImagePath = "D:\\javaPro\\test-images\\";
	public static final String UserUploadedImagePath = "D:\\javaPro\\test-images\\";
	public static final String TmpImagePath = System.getProperty("java.io.tmpdir") + "TogIS"+File.separator;
	public static final String MaskImagePath = "D:\\javaPro\\test-images\\";
	public static final String MaskImageExt = ".gif";
	public static final String TargetImageExt = ".jpg";
	public static final String UserUploadedImageExt = ".jpg";
	
	public static Map<String, String> ImageMediaTypes = null;
	
	public static final List<String> AcceptImageExts = Arrays.asList(".jpg",".png",".gif");
	
	static {
		ImageCommand.IMPath = "D:\\Program Files\\ImageMagick-6.9.3-Q16\\";
		
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put(".jpg", "image/jpeg");
		aMap.put(".png", "image/png");
		aMap.put(".gif", "image/gif");
		ImageMediaTypes = Collections.unmodifiableMap(aMap);
    
	}
}
