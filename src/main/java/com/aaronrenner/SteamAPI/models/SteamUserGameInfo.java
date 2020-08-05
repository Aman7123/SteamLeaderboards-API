package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamUserGameInfo {

	private String SteamID64;
	private String gameName;
	private ArrayList<SteamStatLayout> stats;
	private ArrayList<SteamAchievementLayout> achievements;
	
	public SteamUserGameInfo() {
		stats = new ArrayList<>();
		achievements = new ArrayList<>();
	}
}
