package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * This model represents the global data returned for an achievement
 * @author aaronrenner
 *
 */

public class SteamGlobalAchievementLayout {

	private String name;
	private long precent;
	
	public SteamGlobalAchievementLayout() {
		
	}
}
