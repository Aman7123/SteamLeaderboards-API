package com.aaronrenner.SteamAPI.exceptions;

public class LoginError extends RuntimeException {
	public LoginError(){
        super(String.format("Login failed, username or password is invalid"));
    }
}
