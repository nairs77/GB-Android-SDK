package com.gebros.platform.auth.model.common;


import com.gebros.platform.auth.model.annotation.GBErrorInfo;
import com.gebros.platform.auth.model.annotation.PropertyName;

@GBErrorInfo
public interface GBAPIError extends GBObject {

	@PropertyName(value="errorType")
	abstract public String getErrorType();
	abstract public void setErrorType(String errorType);
	
	@PropertyName(value="errorCode")
	abstract public int getErrorCode();
	abstract public void setErrorCode(int errorCode);

	abstract public String getDetailError();
	abstract public void setDetailError(String detailError);
}
