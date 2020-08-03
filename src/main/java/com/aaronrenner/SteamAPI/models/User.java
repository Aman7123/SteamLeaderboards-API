package com.aaronrenner.SteamAPI.models;

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
	/**
	 *  TODO: Add friends list and make it work below
	 */
	
	public User() {
	}
	
	public void addFriend() {
		
	}
	
	public void removeFriend() {
		
	}
	
}
