package com.gebros.platform.exception;

import com.gebros.platform.auth.net.Response;


public enum GBExceptionType {

	/**
	 * Runtime Error Type
	 */
	
	SESSION_ACTIVE(Response.CLIENT_ON_ERROR, "JoypleSession has been active.", ""),
	NOT_FOUND_TOKEN(Response.CLIENT_ON_ERROR, "Not found accessToken.", ""),
	NOT_EXISTS_BODY(Response.CLIENT_ON_ERROR, "Not exists response body", ""),
	JSON_PARSE_ERROR(Response.CLIENT_ON_ERROR, "JSON parse error.", ""),
	NOT_EXISTS_RETURN(Response.CLIENT_ON_ERROR, "API status(or result) is not exists, status/result is required.", ""),
	NOT_EXISTS_ERROR(Response.CLIENT_ON_ERROR, "API error is not exists.", ""),
	NOT_EXISTS_FILE(Response.CLIENT_ON_ERROR, "This file is not exists.", ""),
	CALLBACK_NULL(Response.CLIENT_ON_ERROR, "ResponseCallback is NULL", ""),
	RESPONSE_NULL(Response.CLIENT_ON_ERROR, "ResponseObject is NULL", ""),
	ANNOTATIONS_INVALID(Response.CLIENT_ON_ERROR, "Annotations is not exists.", ""),
	DEVICE_UTIL_ERROR(Response.CLIENT_ON_ERROR, "DeviceUtils is NULL", ""),
	SESSION_NULL(Response.CLIENT_ON_ERROR, "Session object is NULL.", ""),
	SESSION_INVALID(Response.CLIENT_ON_ERROR, "Session has been closed.", ""),
	DEVICE_ACCESS_ERROR(Response.CLIENT_ON_ERROR, "Device access error.", ""),
	FACEBOOK_USER_CANCELED(Response.CLIENT_ON_ERROR, "User canceled log in.", ""),
	FACEBOOK_USER_DENIED(Response.CLIENT_ON_ERROR, "The user denied the app", ""),
	FACEBOOK_ERROR(Response.CLIENT_ON_ERROR, "Facebook authorization error.", ""),
	GOOGLE_ERROR(Response.CLIENT_ON_ERROR, "Google authorization error.", ""),
	GOOGLE_PLAY_ERROR(Response.CLIENT_ON_ERROR, "Google Play authorization error.", ""),
	NAVER_ERROR(Response.CLIENT_ON_ERROR, "Naver authorization error.", ""),
	TWITTER_ERROR(Response.CLIENT_ON_ERROR, "Twitter authorization error.", ""),
	
	USER_LOGIN_CANCELED(Response.CLIENT_LOGIN_CANCELED, "USER_LOGIN_CANCELED", ""),
	
	BAD_REQUEST(Response.CLIENT_HTTP_BAD_REQUEST, "Not found API resource path.", ""),
	CONNECTION_ERROR(Response.CLIENT_HTTP_BAD_REQUEST, "HTTP connection error, please check for network status.", ""),
	INVALID_AUTHENTICATION(Response.CLIENT_HTTP_BAD_REQUEST, "Invalid authentication.", ""),
	NOT_FOUND_URL(Response.CLIENT_HTTP_BAD_REQUEST, "Could not construct protocol", ""),
	
	SERVER_RESPONSE_FAILED(Response.API_ON_FAILED, "API response is on error, status must be 1 as success code.", ""),
	
	SERVER_EXPIRED_ACCESS_TOKEN(Response.API_TOKEN_EXPIRES, "AccessToken has been expired.", "");
	
	private int errorCode;
	private String errorMessage;
	private String errorDetail;
	
	GBExceptionType(int errorCode, String errorMessage, String errorDatail) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDetail = errorDatail;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
}
