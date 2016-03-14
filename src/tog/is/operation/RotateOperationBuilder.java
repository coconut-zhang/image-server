package tog.is.operation;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class RotateOperationBuilder implements OperationBuilderInterface {

	public static final String RotateQueryKey = "rotate";
		
	private static final RotateOperationBuilder instance = new RotateOperationBuilder();
	public static RotateOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?rotate=90
		
		Double rotate = Util.parseQueryDouble(queryParams, RotateQueryKey);		
		
		op.rotate(rotate);
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(RotateQueryKey);		
		return queryKeys;
	}

}
