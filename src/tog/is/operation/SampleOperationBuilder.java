package tog.is.operation;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

import tog.is.util.Util;

public class SampleOperationBuilder implements OperationBuilderInterface {

	public static final String SampleWidQueryKey = "sampleWid";
	public static final String SampleHeiQueryKey = "sampleHei";
	
	private static final SampleOperationBuilder instance = new SampleOperationBuilder();
	public static SampleOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/girl?wid=225&hei=225
		
		Integer width = Util.parseQueryInt(queryParams, SampleWidQueryKey);
		Integer height = Util.parseQueryInt(queryParams, SampleHeiQueryKey);
		
		op.sample(width, height);
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(SampleWidQueryKey);
		queryKeys.add(SampleHeiQueryKey);
		return queryKeys;
	}

}
