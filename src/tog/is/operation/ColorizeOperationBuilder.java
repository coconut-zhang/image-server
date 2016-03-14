package tog.is.operation;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class ColorizeOperationBuilder implements OperationBuilderInterface {

	public static final String ColorizeQueryKey = "op_colorize";
//	private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
	
	private static Pattern HexColorPattern = Pattern.compile("^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
	
	private static final ColorizeOperationBuilder instance = new ColorizeOperationBuilder();
	public static ColorizeOperationBuilder getInstance(){
		return instance;
	}
	
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?op_colorize=A28A65 | A5ACB0
		
		String fillColor = Util.parseQueryString(queryParams, ColorizeQueryKey);
		
		if(fillColor==null || !HexColorPattern.matcher(fillColor).matches() ){
			return false;
		}
		  
		//convert rose_grey.jpg -fill blue -tint 100 rose_blue.jpg
		op.colorspace("gray");
		op.fill("#"+fillColor);
		op.tint((double) 100);
		
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(ColorizeQueryKey);		
		return queryKeys;
	}

}
