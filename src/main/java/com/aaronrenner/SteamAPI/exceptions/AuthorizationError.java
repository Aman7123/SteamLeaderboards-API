package com.aaronrenner.SteamAPI.exceptions;

public class AuthorizationError extends RuntimeException {
	public AuthorizationError(){
        super(String.format("Be sure to include proper jwt authorization in the header"));
    }
}
