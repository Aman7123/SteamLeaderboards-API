package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * This model represents the layout of how the Steam "GetUserStatsForGame/v2" endpoint returns the data
 * @author aaronrenner
 *
 */
public class SteamGameInfo {

	private String steamID;
	private String gameName;
	private ArrayList<SteamStatLayout> stats;
	private ArrayList<SteamAchievementLayout> achievements;
	
	public SteamGameInfo() {
		stats = new ArrayList<>();
		achievements = new ArrayList<>();
	}
}
