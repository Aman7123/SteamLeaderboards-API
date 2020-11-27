package com.aaronrenner.SteamAPI.exceptions;

public class ServerError extends RuntimeException {
	public ServerError(String message){
		super(String.format(message));
	}
}
