package com.aaronrenner.SteamAPI.exceptions;

public class BadRequestError extends RuntimeException {
	public BadRequestError(String message){
		super(String.format(message));
	}
}
