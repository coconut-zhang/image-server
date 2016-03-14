package tog.is.cache.data;

import tog.is.cache.base.AbstractCallCache;
import tog.is.entity.ImageInfoEntity;
import tog.is.util.Constants;

public class ImageInfoCacheData  extends AbstractCallCache<String, ImageInfoEntity, Boolean>{  
	  
	//singleton
	public static ImageInfoCacheData Instance = null;
	public static synchronized ImageInfoCacheData GetInstance(){
		if(Instance == null){
			Instance = new ImageInfoCacheData();
		}
		return Instance;
	}	
	private ImageInfoCacheData(){}
	
    @Override  
    public ImageInfoEntity getData(String imageName, Boolean isUserImage) {  
        System.out.println("getData mocking...");
        ImageInfoEntity entity = new ImageInfoEntity();
        entity.setName(imageName);
        if(isUserImage){
        	entity.setPath(Constants.ItemImagePath);
        }else{
        	entity.setPath(Constants.UserUploadedImagePath);
        }
        entity.setType(Constants.TargetImageExt);
        return entity;  
    }  
  
//    public static void main(String[] args) {  
//        ImageInfoCacheData cacheData = new ImageInfoCacheData();  
//        for (int i = 0; i < 20; i++) {  
//            String str = cacheData.getCacheData("1", "args");  
//            System.out.println(str);  
//            try {
//				Thread.sleep(400);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//        
//    }  
      
}  