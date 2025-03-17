package com.jmp.gestion_notes.dto;

public class ImportResult {
	
    private boolean success;
    private boolean needsConfirmation;
    private String message;
    
    
	public ImportResult() {
		super();
	}
	public ImportResult(boolean success, boolean needsConfirmation, String message) {
		super();
		this.success = success;
		this.needsConfirmation = needsConfirmation;
		this.message = message;
	}
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isNeedsConfirmation() {
		return needsConfirmation;
	}
	public void setNeedsConfirmation(boolean needsConfirmation) {
		this.needsConfirmation = needsConfirmation;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
    
    
}
