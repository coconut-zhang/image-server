package tog.is;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

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

@Path("/upload")
public class UploadImageService {

	@PostConstruct
	public void init() {

	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleRequest(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

		//date string
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
		Date today = Calendar.getInstance().getTime(); 
		String reportDate = df.format(today);

		String originalFilePath = Constants.TmpImagePath + UUID.randomUUID().toString()	+ contentDispositionHeader.getFileName();
		String uploadedFilePathWithoutExt = Constants.UserUploadedImagePath + UUID.randomUUID().toString() + "_" + reportDate;
		String uploadedFilePath = uploadedFilePathWithoutExt + Constants.UserUploadedImageExt;
		// save original file to the server
		saveFile(fileInputStream, originalFilePath);
		
		//convert to .jpg
		ConvertCmd cmd = new ConvertCmd();
		IMOperation op = new IMOperation();		
//		op.addImage(originalFilePath);
//		op.addImage(uploadedFilePath);
		//convert rgb_image.jpg +profile icm -profile sRGB.icc  -profile USCoat.icm cmyk_image.jpg
		op.addImage(originalFilePath);
		op.p_profile("icm").profile("sRGB.icc"); //.profile("USCoat.icm"); //TODO test
		op.addImage(uploadedFilePath);
		
		IMToken token = null;
		try {			
			token = IMPool.GetInstance().borrowObject();
			cmd.run(op);
			new File(originalFilePath).delete();
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
				
		//make ret json
		String jsonStr = String.format("{result:\"ok\" , imagename:\"%1$s\"}", uploadedFilePathWithoutExt);
		
		return Response.status(200).entity(jsonStr).build();

	}

	// save uploaded file to a defined location on the server
	private void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	

}


