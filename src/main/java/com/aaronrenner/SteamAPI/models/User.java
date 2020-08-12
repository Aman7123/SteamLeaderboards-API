package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class User {

	private long id;
	private String username;
	private String steamID64;
	private String password;
	private String role;
	private ArrayList<FriendID> friendList;
	
	public User() {
		friendList = new ArrayList<>();
	}
	
	public boolean addFriend(FriendID friend) {
		return friendList.add(friend);
	}
	
	public boolean removeFriend(FriendID friend) {
		return friendList.remove(friend);
	}
	
}
