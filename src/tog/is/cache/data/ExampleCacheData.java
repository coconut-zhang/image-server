package tog.is.cache.data;

import tog.is.cache.base.AbstractCallCache;

public class ExampleCacheData  extends AbstractCallCache<String, String, String>{  
	  
	//singleton
	public static ExampleCacheData Instance = null;
	public static synchronized ExampleCacheData GetInstance(){
		if(Instance == null){
			Instance = new ExampleCacheData();
		}
		return Instance;
	}	
	private ExampleCacheData(){}
	
    @Override  
    public String getData(String key, String t) {  
        System.out.println("getData method...");  
        return "{key:"+key+", value:"+key+", args:"+t+"}";  
    }  
  
    public static void main(String[] args) {  
        ExampleCacheData cacheData = new ExampleCacheData();  
        for (int i = 0; i < 20; i++) {  
            String str = cacheData.getCacheData("1", "args");  
            System.out.println(str);  
            try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    }  
      
}  