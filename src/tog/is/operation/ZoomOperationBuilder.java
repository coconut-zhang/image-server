package tog.is.operation;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class ZoomOperationBuilder implements OperationBuilderInterface {

	public static final String ZoomQueryKey = "zoom";
		
	private static final ZoomOperationBuilder instance = new ZoomOperationBuilder();
	public static ZoomOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?wid=225&hei=225
		
		Double zoom = Util.parseQueryDouble(queryParams, ZoomQueryKey);
		
		if(zoom!=null && zoom > 0 ){
			op.resizeByScale(zoom);			
		}
		
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(ZoomQueryKey);		
		return queryKeys;
	}

}
