package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamStatLayout {
	
	private String name;
	private long value;
	
	public SteamStatLayout() {
		
	}

}
