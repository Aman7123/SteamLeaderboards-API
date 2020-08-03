package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamProfile {

	private String SteamID64;
	private String profileURL;
	private String avatar;
	private long lastLogOff;
	private long timeCreated;
	private int recentlyPlayed_Count;
	private int recentlyPlayed_Playtime;
	private int gamedOwned;
	private int TotalPlaytime;

	public SteamProfile() {
		
	}
}
