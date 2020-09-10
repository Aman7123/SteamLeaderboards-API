package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * This is the layout for what a statistic looks like inside of the array returned by Steam
 * @author aaronrenner
 *
 */
public class SteamStatLayout {
	
	private String name;
	private long value;

}
