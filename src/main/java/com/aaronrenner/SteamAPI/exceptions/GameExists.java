package com.aaronrenner.SteamAPI.exceptions;

public class GameExists extends RuntimeException {
	public GameExists(long appID){
		super(String.format("The game associated with ID: %d already exists", appID));
	}
}
