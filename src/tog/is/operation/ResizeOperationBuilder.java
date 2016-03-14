package tog.is.operation;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class ResizeOperationBuilder implements OperationBuilderInterface {

	public static final String WidQueryKey = "wid";
	public static final String HeiQueryKey = "hei";
	
	private static final ResizeOperationBuilder instance = new ResizeOperationBuilder();
	public static ResizeOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?wid=225&hei=225
		
		Integer width = Util.parseQueryInt(queryParams, WidQueryKey);
		Integer height = Util.parseQueryInt(queryParams, HeiQueryKey);
		
		op.resize(width, height);
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(WidQueryKey);
		queryKeys.add(HeiQueryKey);
		return queryKeys;
	}

}
