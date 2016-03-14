package tog.is;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import tog.is.entity.ImageInfoEntity;
import tog.is.exception.TogISException;
import tog.is.im.pool.IMPool;
import tog.is.im.pool.IMToken;
import tog.is.operation.BrightContrastSharpOperationBuilder;
import tog.is.operation.ColorizeOperationBuilder;
import tog.is.operation.CropOperationBuilder;
import tog.is.operation.FlipOperationBuilder;
import tog.is.operation.OperationBuilderInterface;
import tog.is.operation.ResizeOperationBuilder;
import tog.is.operation.RotateOperationBuilder;
import tog.is.operation.SampleOperationBuilder;
import tog.is.operation.TrimOperationBuilder;
import tog.is.operation.ZoomOperationBuilder;
import tog.is.util.Constants;
import tog.is.util.Util;

public class ImageServiceHandler {
	
	
	private static final Map<List<String>,OperationBuilderInterface> OpBuilders = new HashMap<List<String>,OperationBuilderInterface>();
	
	static {		
		//check & create temp folder
		Util.MakeMissingDir(Constants.TmpImagePath);

		//make builder collection
		OpBuilders.put(BrightContrastSharpOperationBuilder.getInstance().getRelatedQueryKeys(), BrightContrastSharpOperationBuilder.getInstance());		
		OpBuilders.put(ColorizeOperationBuilder.getInstance().getRelatedQueryKeys(), ColorizeOperationBuilder.getInstance());
		OpBuilders.put(CropOperationBuilder.getInstance().getRelatedQueryKeys(), CropOperationBuilder.getInstance());
		OpBuilders.put(FlipOperationBuilder.getInstance().getRelatedQueryKeys(), FlipOperationBuilder.getInstance());
		OpBuilders.put(ResizeOperationBuilder.getInstance().getRelatedQueryKeys(), ResizeOperationBuilder.getInstance());
		OpBuilders.put(RotateOperationBuilder.getInstance().getRelatedQueryKeys(), RotateOperationBuilder.getInstance());
		OpBuilders.put(TrimOperationBuilder.getInstance().getRelatedQueryKeys(), TrimOperationBuilder.getInstance());
		OpBuilders.put(ZoomOperationBuilder.getInstance().getRelatedQueryKeys(), ZoomOperationBuilder.getInstance());	
		//sample operation for quick thumbnails
		OpBuilders.put(SampleOperationBuilder.getInstance().getRelatedQueryKeys(), SampleOperationBuilder.getInstance());
	}
	 
	
	public static void MakeImageStream(final ImageInfoEntity imageInfo, final String targetType,  final MultivaluedMap<String, String> queryParams, List<String> keys, OutputStream out) {
		
		try{
			
			//TODO - Log		
			String sourceFile = imageInfo.getPath() + imageInfo.getName() + imageInfo.getType();
			
			System.out.println("tmpPath: " + Constants.TmpImagePath);
			
			//init command & operation
			ConvertCmd cmd = new ConvertCmd();		
			IMOperation op = new IMOperation();		
			op.addImage(sourceFile);
			
			//build Operation 
			List<String> overQueryKeys = new LinkedList<String>();
			for(String queryKey : keys){
								
				if(overQueryKeys.contains(queryKey)){
					continue;
				}
				
				for(List<String> opBuilderKey : OpBuilders.keySet()){
					if(opBuilderKey.contains(queryKey)){
						OperationBuilderInterface opBuilder = OpBuilders.get(opBuilderKey);
						opBuilder.build(queryParams, op, overQueryKeys);
						overQueryKeys.addAll(opBuilderKey);
						break;
					}
					
				}			
			}
			
			
			//cache ??
//			boolean needCache = false;
//			String targetFile = Constants.TmpImagePath;	
//			if(queryParams.containsKey("cache")){
//				targetFile += Util.parseStrToMd5L32(op.toString()) + Constants.TargetImageExt;
//				needCache = true;
//				File image = new File(targetFile);
//				if(image.exists()){
//					if (((new Date()).getTime() - image.lastModified()) / 1000 < 60 * 60) { // cache expired in one hour
//						// output cache file
//						try {
//							Util.outputImage(targetFile, out);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}					
//						return;
//					}
//				}
//			}else{
//				targetFile += UUID.randomUUID().toString() + Constants.TargetImageExt;
//			}
			
			String targetFile = Constants.TmpImagePath;	
			targetFile += UUID.randomUUID().toString() + targetType;
			
			op.addImage(targetFile);
			
			IMToken token = null;
			try {			
				token = IMPool.GetInstance().borrowObject();
				cmd.run(op);
				//output new file
				Util.outputImage(targetFile, out);
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally{
	        	try {
	                if (null != token) {
	                	IMPool.GetInstance().returnObject(token);
	                }
	            } catch (Exception e) {
	                // ignored
	            }
	        }
			
//			if(!needCache){
//				new File(targetFile).delete();
//			}
			
			new File(targetFile).delete();
			
		}catch(TogISException isE){
			throw isE;
		}catch(Exception e){
			//TODO - log
		}
	}
			
	
}
