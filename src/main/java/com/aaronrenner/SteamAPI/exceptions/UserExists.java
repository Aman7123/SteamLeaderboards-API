package com.aaronrenner.SteamAPI.exceptions;

public class UserExists extends RuntimeException {
	public UserExists(String steamID64){
		super(String.format("The user associated with the SteamID: %s already exists", steamID64));
	}
}
