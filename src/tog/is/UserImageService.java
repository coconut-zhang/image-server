package tog.is;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

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

import tog.is.cache.data.ImageInfoCacheData;
import tog.is.entity.ImageInfoEntity;
import tog.is.util.Constants;
import tog.is.util.Util;

/*
 * 
 * TODO: slf4j -> Log4J
 * TODO: Exception
 * 
 *
 */

@Path("/{a: USER|user}")
public class UserImageService { 
	
	@PostConstruct
	public void init() {
		
	}
	
	@Path("/{imageName}")
	@GET	
	@Produces("image/jpeg")
	public Response handleRequest(@PathParam("imageName") final String imageName, @Context final UriInfo info) {
		//get data from DB cache
		final ImageInfoEntity imageInfo = ImageInfoCacheData.GetInstance().getCacheData(imageName, true);
		
		
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream out) {				
				
				System.out.println("path: " + info.getPath());
				System.out.println("abs path: " + info.getAbsolutePath().toString());
				System.out.println("base uri: " + info.getBaseUri().toString());
				System.out.println("current dir: " + System.getProperty("user.dir"));
				
				final MultivaluedMap<String, String> queryParams = info.getQueryParameters();
				//sort query parameters
				List<String> keys = new LinkedList<String>();
				keys.addAll(queryParams.keySet()); 
				keys = Util.SortParameterKeys(keys, info.getRequestUri().toString());
				
//				ImageServiceHandler.MakeImageStream(Constants.UserUploadedImagePath, imageName, queryParams, keys, out);
				ImageServiceHandler.MakeImageStream(imageInfo, Constants.UserUploadedImageExt , queryParams, keys, out);
			}
		};
		return Response.ok(stream).build();
	}
	

	
}
