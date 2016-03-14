package tog.is.operation;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class FlipOperationBuilder implements OperationBuilderInterface {

	public static final String FlipQueryKey = "flip";
		
	private static final FlipOperationBuilder instance = new FlipOperationBuilder();
	public static FlipOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?flip=lr/ud
		
		String flip = Util.parseQueryString(queryParams, FlipQueryKey);
		
		if(flip.equalsIgnoreCase("ud")){
			op.flip();		
		}else{
			op.flop();
		}
		
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(FlipQueryKey);		
		return queryKeys;
	}

}
