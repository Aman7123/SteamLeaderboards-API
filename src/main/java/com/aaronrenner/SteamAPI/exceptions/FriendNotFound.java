package com.aaronrenner.SteamAPI.exceptions;

public class FriendNotFound extends RuntimeException {
	public FriendNotFound(String user_steamID64, String friend_steamID64){
        super(String.format("The user with steamID: %s has no friend with steamID: %s associated", user_steamID64, friend_steamID64));
    }
}
