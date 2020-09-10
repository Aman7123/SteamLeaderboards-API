package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * This model represents the layout for a simple achievement in the steam endpoint
 * @author aaronrenner
 *
 */
public class SteamAchievementLayout {

	private String name;
	private boolean achieved;
	
}
