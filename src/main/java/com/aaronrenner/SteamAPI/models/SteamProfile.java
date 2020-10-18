package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * 
 * @author aaron renner
 * @version 1.0
 *
 */
public class SteamProfile {

	private String steamid;
	private String personaname;
	private String profileurl;
	private String avatar;
	private long level;
	private long lastlogoff;
	private long timecreated;
	private long recentlyPlayed_Count;
	private int recentlyPlayed_Playtime;
	private long gamesOwned_Count;
	private int totalPlaytime;
	private long badge_Count;

	public SteamProfile() {
		
	}
}
