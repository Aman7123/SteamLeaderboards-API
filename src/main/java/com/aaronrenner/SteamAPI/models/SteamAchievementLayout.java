package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * This model represents the layout for a simple achievement in the steam endpoint
 * @author aaron renner
 * @version 1.0
 *
 */
public class SteamAchievementLayout {

	private String name;
	private boolean achieved;

}
