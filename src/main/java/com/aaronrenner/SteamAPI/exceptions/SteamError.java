package com.aaronrenner.SteamAPI.exceptions;

public class SteamError extends RuntimeException {
	public SteamError(int errorCode){
        super(String.format("There was a problem with data from Steam, check the URL - ERROR CODE: %d", errorCode));
    }
}
