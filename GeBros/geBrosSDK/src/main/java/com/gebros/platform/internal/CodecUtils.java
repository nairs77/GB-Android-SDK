package com.gebros.platform.internal;

import android.util.Base64;

import com.gebros.platform.log.GBLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class CodecUtils {
	
	private static final String ENCODING_TYPE = "UTF-8";
	private static final String HASH_ALGORITHM_MD5 = "MD5";
	private static final String HASH_ALGORITHM_SHA256 = "SHA-256";

	public static final int DEFAULT_STREAM_BUFFER_SIZE = 8192;

	public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
        	GBLog.e(e, "%s", e.getMessage());
        }
    }
	
	public static String readStreamToString(InputStream inputStream) throws IOException {
        
		BufferedInputStream bufferedInputStream = null;
        InputStreamReader reader = null;
        
        try {
        	
            bufferedInputStream = new BufferedInputStream(inputStream);
            reader = new InputStreamReader(bufferedInputStream);
            StringBuilder stringBuilder = new StringBuilder();

            final int bufferSize = DEFAULT_STREAM_BUFFER_SIZE;
            char[] buffer = new char[bufferSize];
           
            int n = 0;
            while ((n = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, n);
            }

            return stringBuilder.toString();
            
        } finally {
            closeQuietly(bufferedInputStream);
            closeQuietly(reader);
        }
    }
	
	public static String encodeURLParameters(Map<String, Object> params) throws UnsupportedEncodingException {
        
    	if (params == null || params.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        
        for (String key : params.keySet()) {
            if (!isFirst) sb.append('&');
            else isFirst = false;
            sb.append(RFC3986Encoder(key));
            sb.append('=');
            sb.append(RFC3986Encoder(params.get(key).toString()));
        }
        
        return sb.toString();
    }
	
	public static String md5hash(String key) {
		
		MessageDigest hash = null;
		
		try {
			hash = MessageDigest.getInstance(HASH_ALGORITHM_MD5);
		} catch (NoSuchAlgorithmException e) {
			GBLog.e(e, "%s", e.getMessage());
			return null;
		}

		hash.update(key.getBytes());
		byte[] digest = hash.digest();
		
		StringBuilder builder = new StringBuilder();
		for (int b : digest) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString((b >> 0) & 0xf));
		}
		
		return builder.toString();
	}
	
	public static String sha256hash(String key) {
		
		String SHA = "";
		try {
			
			MessageDigest sh = MessageDigest.getInstance(HASH_ALGORITHM_SHA256);
			sh.update(key.getBytes());
			
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			SHA = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
			SHA = null;
		}
		
		return SHA;
	}
	
	public static String RFC3986Encoder(String normal) throws UnsupportedEncodingException {
		
	   if(normal == null) return "";
	   return URLEncoder.encode(normal, ENCODING_TYPE).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	   
	}
	
	public static String RFC3986Decoder(String normal) throws UnsupportedEncodingException {
		
	   if(normal == null) return "";
	   return URLDecoder.decode(normal, ENCODING_TYPE);
	   
	}
	
	public static String encodeBase64(String data) throws UnsupportedEncodingException {
		
		if(data == null) return null;
		byte[] dataBinary = data.getBytes("utf-8");
		return Base64.encodeToString(dataBinary, 1);
		
	}
	
	public static String decodeBase64(String encodedData) throws UnsupportedEncodingException {
		
		if(encodedData == null) return null;
		byte[] decodedResult = Base64.decode(encodedData, 1);
		return new String(decodedResult, "utf-8");
		
	}
			
	public static Map<String, Object> convertJSONObjectToHashMap(JSONObject jsonObject) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONArray keys = jsonObject.names();
		
		for (int i = 0; i < keys.length(); ++i) {
			String key = null;
			try {
				
				key = keys.getString(i);
				Object value = jsonObject.get(key);
				if (value instanceof JSONObject) {
					value = convertJSONObjectToHashMap((JSONObject) value);
				}
				map.put(key, value);
				
			} catch (JSONException e) {
				GBLog.e(e, "%s", e.getMessage());
			}
		}
		return map;
	}
}
