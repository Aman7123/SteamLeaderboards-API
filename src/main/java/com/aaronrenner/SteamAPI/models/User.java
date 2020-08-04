package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class User {

	private long id;
	private String username;
	private String SteamID64;
	private String password;
	private String role;
	private ArrayList<FriendID> friendList;
	
	public User() {
	}
	
	public boolean addFriend(FriendID friend_SteamID) {
		return friendList.add(friend_SteamID);
	}
	
	public boolean removeFriend(FriendID friend_SteamID) {
		return friendList.remove(friend_SteamID);
	}
	
}
