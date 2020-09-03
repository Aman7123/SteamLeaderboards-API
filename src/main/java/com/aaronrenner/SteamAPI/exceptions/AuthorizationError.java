package com.aaronrenner.SteamAPI.exceptions;

public class AuthorizationError extends RuntimeException {
	public AuthorizationError(String message){
        super(String.format(message));
    }
}
