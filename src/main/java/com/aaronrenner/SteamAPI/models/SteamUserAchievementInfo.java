package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamUserAchievementInfo {
	
	private String SteamID64;
	private String gameName;
	private ArrayList<SteamAchievementLayout> achievements;
	
	public SteamUserAchievementInfo() {
		achievements = new ArrayList<>();
	}
	
	public SteamUserAchievementInfo(String SteamID64, String gameName, ArrayList<SteamAchievementLayout> achievements) {
		this.SteamID64 = SteamID64;
		this.gameName = gameName;
		this.achievements = achievements;
	}

}
