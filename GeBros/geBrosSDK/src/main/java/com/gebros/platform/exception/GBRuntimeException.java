package com.gebros.platform.exception;



@SuppressWarnings("serial")
public class GBRuntimeException extends RuntimeException implements BaseException {

	private GBExceptionType errorType;
	
	public GBRuntimeException(String message) {
		
		super(message);

		// TODO : Send Error to Server
		//GBLogger.clientErrorToServer(this);
	}
	
	public GBRuntimeException(String message, Exception e) {
		
		super(message, e);

		// TODO : Send Error to Server
		//GBLogger.clientErrorToServer(this);
	}
	
	public GBRuntimeException(GBExceptionType errorType) {
		
		this.errorType = errorType;

		// TODO : Send Error to Server
		//GBLogger.clientErrorToServer(this);
	}
	
	public void setErrorType(GBExceptionType errorType) {
		this.errorType = errorType;
	}

	@Override
	public int getErrorCode() {
		return errorType.getErrorCode();
	}

	@Override
	public String getDetailError() {
		return errorType.getErrorDetail();
	}

	@Override
	public String getMessage() {
		return errorType == null ? super.getMessage() : errorType.getErrorMessage();
	}
}
