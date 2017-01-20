package com.gebros.platform.auth.net;

import com.gebros.platform.auth.model.common.JsonUtil;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.internal.CodecUtils;
import com.gebros.platform.log.GBLog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;


public class MultipartResponse extends Response {

	private static final String BOUNDARAY = MultipartRequest.BOUNDARY;
	private static final String PREFIX = "--";
	private static final String END_LINE = "\r\n";
	private static final String NAME = "file";
	
	private static final int BYTE_OFFSET = 0;
	private static final int MAX_BUFFER_SIZE = 1024;
	private static final String JPEG = "image/jpeg";
	private static final String PNG = "image/png";

	protected MultipartResponse(Builder builder) {
		super(builder);
	}
	
	public static Response fromHttpConnection(MultipartRequest request, HttpURLConnection connection) throws IOException {
		
		FileInputStream fileInputStream = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		
		int responseCode = HTTP_OK;
		String fileType = JPEG;
		String responseBody = null;
		
	    try {
	    	
	    	if(request == null || connection == null)
	    		throw new GBException(GBExceptionType.BAD_REQUEST);
	    	
	    	String fileName = request.getFileName();
	    	File file = new File(fileName);
	    	
	    	if(!file.exists())
	    		throw new GBRuntimeException(GBExceptionType.NOT_EXISTS_FILE);
	    	
	    	if(file.getName().toLowerCase().contains("png"))
	    		fileType = PNG;
	    	
	    	fileInputStream = new FileInputStream(file);
	    	GBLog.d(TAG + "File informations, form-name:%s file-path:%s", NAME, file.getAbsolutePath());
	    	
	    	// Write
    		outputStream = new DataOutputStream(connection.getOutputStream());
    		outputStream.writeBytes(PREFIX + BOUNDARAY + END_LINE);
    		outputStream.writeBytes("Content-Disposition: form-data; name=\"" + NAME + "\"; filename=\"" + file.getAbsolutePath() + "\"" + END_LINE);
    		outputStream.writeBytes("Content-Type: " + fileType + END_LINE);
    		outputStream.writeBytes(END_LINE);
    		
    	    // Read File
    		// * Available replace to ByteArrayOutputStream & Bitmap 
    	    int bytesAvailable = fileInputStream.available();
    	    int maxBufferSize = MAX_BUFFER_SIZE;
    	    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
    	    
    	    byte[] buffer = new byte[bufferSize];
    	    int bytesRead = fileInputStream.read(buffer, BYTE_OFFSET, bufferSize);
    	    
    	    StringBuilder sb = new StringBuilder();
    	    while(bytesRead > BYTE_OFFSET) {
    	    	
    	    	outputStream.write(buffer, BYTE_OFFSET, bufferSize);
    	    	bytesAvailable = fileInputStream.available();
    	    	bufferSize = Math.min(bytesAvailable, maxBufferSize);
    	    	bytesRead = fileInputStream.read(buffer, BYTE_OFFSET, bufferSize);
    	    	
    	    	sb.append(buffer);
    	    }
    	    
    	    outputStream.writeBytes(END_LINE);
    	    outputStream.writeBytes(PREFIX + BOUNDARAY + PREFIX + END_LINE);  	    
    	    
    	    // Get response
	    	responseCode = connection.getResponseCode();
	    	if(responseCode >= HTTP_BAD_REQUEST)
	    		inputStream = connection.getErrorStream();
	    	else
	    		inputStream = connection.getInputStream();
	        
	    	responseBody = CodecUtils.readStreamToString(inputStream);
	    	
	    	if(GBLog.isDebug()) {

				GBLog.d("------------------------- HTTP %s ------------------------------------------------------------", "Request");
				GBLog.d("URL: %s", connection.getURL());
				GBLog.d("Method: %s", connection.getRequestMethod());
				GBLog.d("[Headers]\n%s", getArrangeHeaders(request.getHeaders()));
				GBLog.d("[RequestBody]\n%s", "");

				GBLog.d("------------------------- HTTP %s ------------------------------------------------------------", "Response");
				GBLog.d("responseCode: %d", connection.getResponseCode());
				GBLog.d("[Headers]\n%s", getArrangeHeaders(connection.getHeaderFields()));
				GBLog.d("[ResponseBody]\n%s", responseBody);
				GBLog.d("---------------------------------------------------------------------------------------------------");
	    	}
	    	
	    	Response response = createResponseFromString(request, connection, responseBody);
	    	response.setResponseCode(responseCode);
			response.setResponseBody(responseBody);
			
			return response;
			
	    } catch (SocketTimeoutException e) {

			GBLog.e(e, TAG + "%s", e.toString());
        	return new Response.Builder(request, connection)
        			.responseCode(Response.HTTP_BAD_REQUEST)
        			.responseBody(responseBody)
        			.exception(new GBException(GBExceptionType.CONNECTION_ERROR)).build();
        	
	    } catch (IOException e) {

			GBLog.e(e, TAG + "%s", e.toString());
        	return new Response.Builder(request, connection)
        			.responseCode(Response.HTTP_BAD_REQUEST)
        			.responseBody(responseBody)
        			.exception(new GBException(GBExceptionType.BAD_REQUEST)).build();
        	
        } catch (GBException e) {

			GBLog.e(e, TAG + "%s", e.getMessage());
        	return new Response.Builder(request, connection)
        			.responseCode(responseCode)
        			.state(JsonUtil.getResponseTemplate(request))
        			.responseBody(responseBody)
        			.exception(e).build();
        	
        }  finally {
        	
        	CodecUtils.closeQuietly(inputStream);
        	CodecUtils.closeQuietly(outputStream);
        	CodecUtils.closeQuietly(fileInputStream);
        	connection.disconnect();
        }
	}
}
