package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamAchievementLayout {

	private String name;
	private boolean achieved;
	
	public SteamAchievementLayout() {
		
	}
}
