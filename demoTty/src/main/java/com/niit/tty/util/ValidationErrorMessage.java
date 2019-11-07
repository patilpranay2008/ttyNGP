package com.niit.tty.util;

public class ValidationErrorMessage {
	private String errorMessage;
	private Boolean errorStatus;

	public ValidationErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		this.errorStatus = true;
	}

	public ValidationErrorMessage(String errorMessage, Boolean errorStatus) {
		this.errorMessage = errorMessage;
		this.errorStatus = errorStatus;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Boolean getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(Boolean errorStatus) {
		this.errorStatus = errorStatus;
	}
	
	
}
