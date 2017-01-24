package com.gebros.platform.auth.model.common;

import org.json.JSONArray;

import java.util.List;

public interface GBObjectList<T> extends List<T> {
	
	abstract public <U extends GBObject> GBObjectList<U> castToAsList(Class<U> GBObjectClass);
	abstract public JSONArray getInnerJSONArray();
	
}
