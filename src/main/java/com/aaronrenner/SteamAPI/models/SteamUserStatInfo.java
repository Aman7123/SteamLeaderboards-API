package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamUserStatInfo {
	
	private String SteamID64;
	private String gameName;
	private ArrayList<SteamStatLayout> stats;
	
	public SteamUserStatInfo() {
		stats = new ArrayList<>();
	}
}
