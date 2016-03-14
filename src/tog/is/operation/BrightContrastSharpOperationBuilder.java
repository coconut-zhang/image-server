package tog.is.operation;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class BrightContrastSharpOperationBuilder implements OperationBuilderInterface {

	public static final String BrightQueryKey = "op_brightness";
	public static final String ContrastQueryKey = "op_contrast";
	public static final String SharpenQueryKey = "op_usm";
		
	private static final BrightContrastSharpOperationBuilder instance = new BrightContrastSharpOperationBuilder();
	public static BrightContrastSharpOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?op_brightness=10&op_contrast=20&op_usm=3.4
		
		boolean ret = false;
		
		//brightness Contrast
		Double brightness = Util.parseQueryDouble(queryParams, BrightQueryKey);	
		Double contrast = Util.parseQueryDouble(queryParams, ContrastQueryKey);	
		
		if(brightness !=null || contrast !=null){
			op.brightnessContrast(brightness, contrast);
			ret = true;
		}
		
		//sharpen
		Double sharpenSigma = Util.parseQueryDouble(queryParams, SharpenQueryKey);		
		if(sharpenSigma!=null) {			
			op.sharpen(0.0, sharpenSigma);
			ret = true;
		}

		return ret;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(BrightQueryKey);		
		queryKeys.add(ContrastQueryKey);
		queryKeys.add(SharpenQueryKey);
		return queryKeys;
	}

}
