package tog.is.operation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

import tog.is.exception.TogISException;
import tog.is.util.Util;

public class CropOperationBuilder implements OperationBuilderInterface {

	public static final String CropQueryKey = "crop";
		
	private static final CropOperationBuilder instance = new CropOperationBuilder();
	public static CropOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?crop=900,900,1016,25
		
		String cropWholeStr = Util.parseQueryString(queryParams, CropQueryKey);		
		
		if(cropWholeStr != null){
			String[] cropSubStrs = cropWholeStr.split(",");		
			if(cropSubStrs==null || cropSubStrs.length!=4 ){
				return false;
			}
			
			List<Integer> crops = new ArrayList<Integer>();
			for (String str : cropSubStrs) {
				try{
					crops.add( Integer.parseInt(str) );
				}catch(NumberFormatException e){
					throw new TogISException("error arguments for crop Operation");
				}
			}
			  
			//String commandFormat = "convert %1$s -crop %2$dx%3$d+%4$d+%5$d +profile \"*\" %6$s";
			op.crop(crops.get(0), crops.get(1), crops.get(2), crops.get(3));
			
			System.out.println( op.toString());
		}else{
			return false;
		}
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(CropQueryKey);		
		return queryKeys;
	}

}
