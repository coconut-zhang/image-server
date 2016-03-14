package tog.is.im.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class IMPool extends GenericObjectPool<IMToken>{

	public static IMPool Instance = null;
	
	public static synchronized IMPool GetInstance(){
		if(Instance == null){
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
	        config.setMaxIdle(3);
	        config.setMaxTotal(5);
	        config.setTestOnBorrow(true);
	        config.setTestOnReturn(true);
//	        pool = new PPool(new PFactory(), config);
			Instance = new IMPool(new IMTokenFactory(), config);
		}
		
		return Instance;
	}
	
    /**
     * Constructor.
     * 
     * It uses the default configuration for pool provided by
     * apache-commons-pool2.
     * 
     * @param factory
     */
    public IMPool(PooledObjectFactory<IMToken> factory) {
        super(factory);
    }

    /**
     * 
     * 
     * @param factory
     * @param config
     */
    public IMPool(PooledObjectFactory<IMToken> factory,
            GenericObjectPoolConfig config) {
        super(factory, config);
    }
}



