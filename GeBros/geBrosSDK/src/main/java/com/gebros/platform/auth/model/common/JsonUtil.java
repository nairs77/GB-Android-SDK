package com.gebros.platform.auth.model.common;

import android.annotation.SuppressLint;

import com.gebros.platform.auth.net.Request;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.exception.GBExceptionType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonUtil {
	
    static void jsonObjectClear(JSONObject jsonObject) {
    	
        @SuppressWarnings("unchecked")
        Iterator<String> keys = (Iterator<String>) jsonObject.keys();
        while (keys.hasNext()) {
            keys.next();
            keys.remove();
        }
    }

    static boolean jsonObjectContainsValue(JSONObject jsonObject, Object value) {
    	
        @SuppressWarnings("unchecked")
        Iterator<String> keys = (Iterator<String>) jsonObject.keys();
        while (keys.hasNext()) {
            Object thisValue = jsonObject.opt(keys.next());
            if (thisValue != null && thisValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private final static class JSONObjectEntry implements Map.Entry<String, Object> {
    	
        private final String key;
        private final Object value;

        JSONObjectEntry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @SuppressLint("FieldGetter")
        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getValue() {
            return this.value;
        }

        @Override
        public Object setValue(Object object) {
            throw new UnsupportedOperationException("JSONObjectEntry is immutable");
        }

    }

    static Set<Map.Entry<String, Object>> jsonObjectEntrySet(JSONObject jsonObject) {
    	
        HashSet<Map.Entry<String, Object>> result = new HashSet<Map.Entry<String, Object>>();

        @SuppressWarnings("unchecked")
        Iterator<String> keys = (Iterator<String>) jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.opt(key);
            result.add(new JSONObjectEntry(key, value));
        }

        return result;
    }

    static Set<String> jsonObjectKeySet(JSONObject jsonObject) {
    	
        HashSet<String> result = new HashSet<String>();

        @SuppressWarnings("unchecked")
        Iterator<String> keys = (Iterator<String>) jsonObject.keys();
        while (keys.hasNext()) {
            result.add(keys.next());
        }

        return result;
    }

    static void jsonObjectPutAll(JSONObject jsonObject, Map<String, Object> map) {
    	
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            try {
                jsonObject.putOpt(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    static Collection<Object> jsonObjectValues(JSONObject jsonObject) {
    	
        ArrayList<Object> result = new ArrayList<Object>();

        @SuppressWarnings("unchecked")
        Iterator<String> keys = (Iterator<String>) jsonObject.keys();
        while (keys.hasNext()) {
            result.add(jsonObject.opt(keys.next()));
        }

        return result;
    }
    
	public static JSONObject getResponseErrorTemplate() {
    	
		JSONObject errorInfo = new JSONObject();
		try {
			errorInfo.put(Response.ERROR_CODE_KEY, GBExceptionType.NOT_EXISTS_ERROR.getErrorCode());
			errorInfo.put(Response.ERROR_MESSAGE_KEY, GBExceptionType.NOT_EXISTS_ERROR.toString());
		} catch (JSONException e) {}
		return errorInfo;
	}
	
	public static JSONObject getResponseErrorTemplate(GBExceptionType exceptionType) {
    	
		JSONObject errorInfo = new JSONObject();
		try {
			
			errorInfo.put(Response.ERROR_CODE_KEY, exceptionType.getErrorCode());
			errorInfo.put(Response.ERROR_MESSAGE_KEY, exceptionType.toString());
			
		} catch (JSONException e) {}
		return errorInfo;
	}

    public static JSONObject getResponseTemplate(Request request) {
    	
		JSONObject response = new JSONObject();
		try {
			
			response.put(Response.API_RETURN_CODE, Response.CLIENT_ON_ERROR);
			response.put(Response.API_RESULT_KEY, "{}");
			response.put(Response.API_ERROR_KEY, "{}");
			
		} catch (JSONException e) {}
		return response;
	}
}
