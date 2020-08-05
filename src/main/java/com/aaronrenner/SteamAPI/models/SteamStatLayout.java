package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamStatLayout {
	
	private String name;
	private long value;
	
	public SteamStatLayout() {
		
	}

}
