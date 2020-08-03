package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class Game {
	
	private long id;
	private String title;
	
	public Game() {
	}

}
