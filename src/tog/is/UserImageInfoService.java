package tog.is;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

import tog.is.im.pool.IMPool;
import tog.is.im.pool.IMToken;
import tog.is.util.Constants;

/*
 * 
 * TODO: slf4j -> Log4J
 * TODO: Exception
 * 
 *
 */

@Path("/userimageinfo/{imageName}")
public class UserImageInfoService {

	@PostConstruct
	public void init() {

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleRequest(@PathParam("imageName") final String imageName, @Context final UriInfo info) {

		//date string
		String uploadedFilePath = Constants.UserUploadedImagePath + imageName + Constants.UserUploadedImageExt;
		
		BufferedImage bimg;
		int width = 0;
		int height = 0;
//		try {
//			bimg = ImageIO.read(new File(uploadedFilePath));
//			width	= bimg.getWidth();
//			height	= bimg.getHeight();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		IdentifyCmd idCmd = new IdentifyCmd();
		IMOperation op = new IMOperation();
		op.addImage(uploadedFilePath);
		
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        idCmd.setOutputConsumer(output);
        
        IMToken token = null;
        try {
        	token = IMPool.GetInstance().borrowObject();
        	idCmd.run(op);
        } catch (Exception e) {
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

        // now read the output
        Pattern MY_PATTERN = Pattern.compile("\\s*(\\d*x\\d*)\\s*");
        ArrayList<String> out = output.getOutput();
        for (String outStr : out){
        	Matcher m = MY_PATTERN.matcher(outStr);
        	if (m.find()) {
        	    String s = m.group(1);
        	    System.out.println("@@@"+s);
        	    String[] widnHei = s.split("x");
        	    if(widnHei.length==2){
        	    	width = Integer.parseInt(widnHei[0]);
        	    	height = Integer.parseInt(widnHei[1]);
        	    	break;
        	    }
        	}
        	
        }
		
				
		//make ret json
		String jsonStr = String.format("{result:\"ok\" , width:\"%1$d\" , height:\"%2$d\"}", width, height);
		
		return Response.status(200).entity(jsonStr).build();

	}	

}


