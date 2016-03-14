package tog.is.im.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class IMTokenFactory extends BasePooledObjectFactory<IMToken> {
	private static int Num = 0;
    @Override
    public IMToken create() throws Exception {
        // TODO Auto-generated method stub
        return new IMToken(Num==Integer.MAX_VALUE? 0 : ++Num );
    }

    @Override
    public PooledObject<IMToken> wrap(IMToken token) {
        // TODO Auto-generated method stub
        return new DefaultPooledObject<IMToken>(token);
    }

	

}