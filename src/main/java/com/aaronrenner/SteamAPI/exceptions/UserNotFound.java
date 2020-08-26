package com.aaronrenner.SteamAPI.exceptions;

public class UserNotFound extends RuntimeException {
	public UserNotFound(String steamID64){
        super(String.format("The user associated with steamID: %s was not found", steamID64));
    }
}
