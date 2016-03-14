package tog.is.operation;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

public interface OperationBuilderInterface {
	public boolean build(MultivaluedMap<String, String> queryParams,IMOperation op, final List<String> overQueryKeys);
	public List<String> getRelatedQueryKeys();
}
