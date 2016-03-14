package tog.is.operation;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.im4java.core.IMOperation;

import tog.is.util.Constants;
import tog.is.util.Util;

public class TrimOperationBuilder implements OperationBuilderInterface {

	public static final String MaskQueryKey = "mask";
		
	private static final TrimOperationBuilder instance = new TrimOperationBuilder();
	public static TrimOperationBuilder getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean build(MultivaluedMap<String, String> queryParams, IMOperation op, final List<String> overQueryKeys) {
		//http://localhost/is/image/TOP/3283_LL35995WHLB2zm?wid=500&mask=mask
		
		String mask = Util.parseQueryString(queryParams, MaskQueryKey);		
		if(mask==null || mask.isEmpty() ){
			return false;
		}
		
		String maskFile = (Constants.MaskImagePath + mask + Constants.MaskImageExt);
		if(!(new File(maskFile)).exists()){
			maskFile = (Constants.MaskImagePath + "mask.gif");// just for test
		}		
		  
		//convert 3283_LL35995WHLB2zm.jpg -alpha on mask.gif -compose Dst_In -composite -background White -alpha remove trim.png
		//convert D:\tog\test-images\3283_LL35995WHLB2zm.jpg -alpha on -resize 300 D:\tog\test-images\mask1.gif -geometry 300 -compose Dst_In -composite  -background White -alpha remove D:\tog\test-images\trim2.png
		//convert /home/CORP/htliu/3283_LL35995WHLB2zm.jpg -alpha on /home/CORP/htliu/mask.gif -compose Dst_In -composite  -background White -alpha Background -alpha off /home/CORP/htliu/trim3.png
		//convert /home/CORP/htliu/3283_LL35995WHLB2zm.jpg -alpha on /home/CORP/htliu/mask.gif -compose Dst_In -composite  -background White -alpha Background -alpha off -resize 500 /home/CORP/htliu/trim3.jpg
		op.alpha("on");
		op.addImage(maskFile);
		if(overQueryKeys.contains(ResizeOperationBuilder.WidQueryKey) || overQueryKeys.contains(ResizeOperationBuilder.HeiQueryKey)){
			Integer width = Util.parseQueryInt(queryParams, ResizeOperationBuilder.WidQueryKey);
			Integer height = Util.parseQueryInt(queryParams, ResizeOperationBuilder.HeiQueryKey);
			op.geometry(width, height);
		}
//		op.compose("Dst_In").composite().gravity("center").background("White").alpha("Background");
		op.compose("Dst_In").composite().gravity("center").background("White").alpha("remove");
		
		System.out.println( op.toString());
		return true;
	}

	@Override
	public List<String> getRelatedQueryKeys() {
		LinkedList<String> queryKeys = new LinkedList<String>();
		queryKeys.add(MaskQueryKey);		
		return queryKeys;
	}

}
