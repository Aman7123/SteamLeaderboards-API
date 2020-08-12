package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class FriendID {

	private long id;
	private String SteamID64;
	
	public FriendID() {
	}
	
	public FriendID(long id) {
		this.id = id;
	}
	
}
