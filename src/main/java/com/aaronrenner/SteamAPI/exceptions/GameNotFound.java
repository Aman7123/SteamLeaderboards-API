package com.aaronrenner.SteamAPI.exceptions;

public class GameNotFound extends RuntimeException {
	public GameNotFound(long appID){
		super(String.format("The game associated with ID: %d was not found", appID));
	}
}
