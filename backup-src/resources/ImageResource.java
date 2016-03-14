package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;

/*
 * TODO:
 * 1. ImageResource -- commander
 * 2. OptionBuilder -- common class
 * 			\-- build()
 * 			\-- ArgumentName
 * 			\-- getInstance()
 * 3. static Map -- ( name, OptionBuilder)
 * 
 * TODO: slf4j -> Log4J
 * TODO: Exception
 * 
 *
 */

@Path("/{imageName}")
public class ImageResource { 
	public static final String ImagePath = "D:\\tog\\test-images\\";
	static {
		ImageCommand.IMPath = "C:\\Program Files\\ImageMagick-6.9.3-Q16\\";
		
	}
	
	@PostConstruct
	public void init() {
		
	}
	
	@GET
	@Produces("image/jpg")
	public Response handleRequest(@PathParam("imageName") final String imageName, @Context final UriInfo info) {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream out) {				
				
				System.out.println("path: " + info.getPath());
				System.out.println("abs path: " + info.getAbsolutePath().toString());
				System.out.println("base uri: " + info.getBaseUri().toString());
				System.out.println("current dir: " + System.getProperty("user.dir"));
				
				String sourceFile = (ImagePath + imageName + ".jpg");//.replace("/", File.separator)
				
				String tmpPath = System.getProperty("java.io.tmpdir");
				System.out.println("tmpPath: " + tmpPath);
				
				
				
				//String imPath="C:\\Program Files\\ImageMagick-6.9.3-Q16";
				ConvertCmd cmd = new ConvertCmd();
				
//				cmd.setSearchPath(imPath);
				
				IMOperation op = new IMOperation();
				
				op.addImage(sourceFile);
				
				if( buildResizeOperation(info, op) ){
					//resize command
				}else if( buildZoomOperation(info, op) ){
					//zoom command
					
				}else if( buildRotateOperation(info, op) ){
					//rotate command
					
				}else if( buildCropOperation(info, op) ){
					//crop command
					
				}else if( buildColorizeOperation(info, op)){
					//colorize command
				
				}else if( buildTrimOperation(info, op)){
					//Trim command				
			
				}else if( buildFlipOperation(info, op)){
					//Flip command
					
				}else if( buildBCSOperation(info, op)){
					//Bright Contrast Sharpen command					
				}
				else{
										
				}
				
				//cache ??
				boolean needCache = false;
				String targetFile = tmpPath;				
				MultivaluedMap<String, String> queryParams = info.getQueryParameters();	
				if(queryParams.containsKey("cache")){
					targetFile += Util.parseStrToMd5L32(op.toString()) + ".jpg";
					needCache = true;
					File image = new File(targetFile);
					if(image.exists()){
						if (((new Date()).getTime() - image.lastModified()) / 1000 < 60 * 60) { // in one hour
							// output cache file
							try {
								outputImage(targetFile, out);
							} catch (IOException e) {
								e.printStackTrace();
							}
							return;
						}
					}
				}else{
					targetFile += UUID.randomUUID().toString() + ".jpg";
				}
				
				op.addImage(targetFile);
				
				/* TODO break this up and handle exceptions */
				try {	
					
					cmd.run(op);
					//output new file
					outputImage(targetFile, out);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				if(!needCache){
					new File(targetFile).delete();
				}
			}
		};
		return Response.ok(stream).build();
	}
	
	private void outputImage(String targetFile, OutputStream out) throws IOException{
		FileInputStream fis = null;										
		fis = new FileInputStream(targetFile);
							
		byte[] imageBytes = new byte[1024 * 2^20 * 5];

		while (fis.read(imageBytes) != -1) {
			out.write(imageBytes);
		}
		out.flush();
		fis.close();
		
	}

	//http://localhost/is/image/TOP/girl?wid=225&hei=225
	private boolean buildResizeOperation(UriInfo info, IMOperation op) {	
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();	
		
		String wid = queryParams.getFirst("wid");
		String hei = queryParams.getFirst("hei");
		if(wid==null && hei==null){
			//not a resize command
			return false;
		}
		
		int width = (wid==null)? 0 : Integer.parseInt(wid);
		int height = (hei==null)? 0 : Integer.parseInt(hei);
				
		op.resize(width>0 ? width:null, height>0 ? height : null);
		System.out.println( op.toString());
		return true;	
		
	}
	
	//http://localhost/is/image/TOP/girl?wid=225&hei=225
	private boolean buildZoomOperation(UriInfo info, IMOperation op) {	
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();	
		
		String zoom = queryParams.getFirst("zoom");		
		if(zoom==null){
			//not a zoom command
			return false;
		}
		
		int scale = Integer.parseInt(zoom);

		op.resizeByScale(scale);
		System.out.println( op.toString());
		return true;	
		
	}
	
	//http://localhost/is/image/TOP/girl?rotate=90
	private boolean buildRotateOperation(UriInfo info, IMOperation op) {		
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();		
		
		List<String> rotates = queryParams.get("rotate");
		if(rotates==null){
			//not a rotate command
			return false;
		}
		
		int rotate = Integer.parseInt(queryParams.get("rotate").get(0));		
		if(!(rotate>0)){
			//not a rotate command
			return false;
		}
		  
		op.rotate((double)rotate);
		return true;
		
	}
	
	//http://localhost/is/image/TOP/girl?crop=900,900,1016,25
	private  boolean buildCropOperation(UriInfo info, IMOperation op) {		
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();		
		
		List<String> rotates = queryParams.get("crop");
		if(rotates==null){
			//not a rotate command
			return false;
		}
		
		String cropWholeStr = queryParams.get("crop").get(0);
		String[] cropSubStrs = cropWholeStr.split(",");		
		if(cropSubStrs==null || cropSubStrs.length!=4 ){
			return false;
		}
		
		List<Integer> crops = new ArrayList<Integer>();
		for (String str : cropSubStrs) {
			crops.add( Integer.parseInt(str) );
		}
		  
		//String commandFormat = "convert %1$s -crop %2$dx%3$d+%4$d+%5$d +profile \"*\" %6$s";
		op.crop(crops.get(0), crops.get(1), crops.get(2), crops.get(3));
		return true;
	}
	
	//http://localhost/is/image/TOP/girl?op_brightness=10&op_contrast=20&op_usm=3.4
	private  boolean buildBCSOperation(UriInfo info, IMOperation op) {		
		
		boolean ret = false;
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();		
		
		//brightnessContrast
		String op_brightness = queryParams.getFirst("op_brightness");	
		String op_contrast = queryParams.getFirst("op_contrast");
		double brightness = 0;
		double contrast = 0;
		
		if(op_brightness!=null) {
			brightness = Double.parseDouble(op_brightness);
			ret = true;
		}
		if(op_contrast!=null){
			contrast = Double.parseDouble(op_contrast);
			ret = true;
		}		
		
		if(ret){
			op.brightnessContrast(brightness, contrast);
		}
		
		//sharpen
		String op_usm = queryParams.getFirst("op_usm");
		if(op_usm!=null) {
			double sharpenSigma = (double) Double.parseDouble(op_usm);
			op.sharpen(0.0, sharpenSigma);
			ret = true;
		}

		return ret;
	}
	
	//http://localhost/is/image/TOP/girl?op_colorize=A28A65 | A5ACB0
	private boolean buildColorizeOperation(UriInfo info, IMOperation op) {	
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();	
		
		List<String> fillColors = queryParams.get("op_colorize");
		
		if(fillColors==null){			
			return false;
		}
		
		String fillColor = fillColors.get(0);
		
		if(fillColor.isEmpty() ){
			return false;
		}
		  
		//convert rose_grey.jpg -fill blue -tint 100 rose_blue.jpg
		op.colorspace("gray");
		op.fill("#"+fillColor);
		op.tint((double) 100);
		
		return true;	
		
	}
	
	//http://localhost/is/image/TOP/girl?flip=lr/ud
	private boolean buildFlipOperation(UriInfo info, IMOperation op) {	
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();	
		
		List<String> flips = queryParams.get("flip");
		
		if(flips==null || flips.isEmpty()){			
			return false;
		}
		
		String flipDirection = flips.get(0);

		if(flipDirection.equalsIgnoreCase("ud")){
			op.flip();		
		}else{
			//(flipDirection.equalsIgnoreCase("h")){
			op.flop();
		}
		return true;	
	}
	
	
	
	//http://localhost/is/image/TOP/3283_LL35995WHLB2zm?mask=mask
	private boolean buildTrimOperation(UriInfo info, IMOperation op) {	
		
		MultivaluedMap<String, String> queryParams = info.getQueryParameters();	
		
		List<String> masks = queryParams.get("mask");
		
		if(masks==null){			
			return false;
		}
		
		String mask = masks.get(0);
			
		
		if(mask.isEmpty() ){
			return false;
		}
		
		String maskFile = (ImagePath + mask + ".gif");
		if(!(new File(maskFile)).exists()){
			maskFile = (ImagePath + "mask.gif");			
		}		
		  
		//convert 3283_LL35995WHLB2zm.jpg -alpha on mask.gif -compose Dst_In -composite -background White -alpha remove trim.png
		op.alpha("on");
		op.addImage(maskFile);
		op.compose("Dst_In").composite().gravity("center").background("White").alpha("remove");
		
		return true;	
		
	}
	
	
	/*
	 * Test
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
				
	}
	
}
