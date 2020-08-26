package com.aaronrenner.SteamAPI.exceptions;

public class FriendExists extends RuntimeException {
	public FriendExists(String user_steamID64, String friend_steamID64){
        super(String.format("The user with steamID: %s has a friend with steamID: %s associated", user_steamID64, friend_steamID64));
    }
}
