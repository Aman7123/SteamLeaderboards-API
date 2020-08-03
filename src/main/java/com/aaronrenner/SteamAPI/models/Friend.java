package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class Friend {
	
	private long id;
	private String SteamID64;
	
	public Friend() {
	}
	
}
