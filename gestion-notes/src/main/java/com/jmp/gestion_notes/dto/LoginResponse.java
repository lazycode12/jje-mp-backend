package com.jmp.gestion_notes.dto;

public class LoginResponse {
    private String accestoken;

    
    
	public LoginResponse() {
		super();
	}



	public LoginResponse(String accestoken) {
		super();
		this.accestoken = accestoken;
	}



	public String getAccestoken() {
		return accestoken;
	}



	public void setAccestoken(String accestoken) {
		this.accestoken = accestoken;
	}
    
	
    
}
