package tog.is.operation;

import javax.ws.rs.core.MultivaluedMap;

import tog.is.util.Constants;
import tog.is.util.Util;

public class FmtOperationBuilder { //implements OperationBuilderInterface {

	public static final String FmtQueryKey = "fmt";
		
	private static final FmtOperationBuilder instance = new FmtOperationBuilder();
	public static FmtOperationBuilder getInstance(){
		return instance;
	}
	
	
//	@Override
//	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
//		//http://localhost/is/image/TOP/girl?wid=225&hei=225
//		
//		Double zoom = Util.parseQueryDouble(queryParams, ZoomQueryKey);
//		
//		if(zoom!=null && zoom > 0 ){
//			op.resizeByScale(zoom);			
//		}
//		
//		System.out.println( op.toString());
//		return true;
//	}

//	@Override
//	public List<String> getRelatedQueryKeys() {
//		LinkedList<String> queryKeys = new LinkedList<String>();
//		queryKeys.add(ZoomQueryKey);		
//		return queryKeys;
//	}

	public String checkFmtOperation(String defaultExt, MultivaluedMap<String, String> queryParams){
		//check fmt parameter		
		String fmt = Util.parseQueryString(queryParams, FmtOperationBuilder.FmtQueryKey);
		if(fmt!=null && fmt.length()>0){			
			String askedExt = "."+fmt.trim().toLowerCase();
			if(Constants.AcceptImageExts.contains(askedExt)){
				return askedExt;
			}
		}
		
		return defaultExt;
	}
}
