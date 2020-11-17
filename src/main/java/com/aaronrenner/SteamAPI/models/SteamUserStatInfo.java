package com.aaronrenner.SteamAPI.models;

import java.util.ArrayList;
import lombok.Data;

@Data
/**
 * 
 * @author aaron renner
 * @version 1.0
 *
 */
public class SteamUserStatInfo {

	private String SteamID64;
	private String gameName;
	private ArrayList<SteamStatLayout> stats = new ArrayList<>();

	public SteamUserStatInfo(String SteamID64, String gameName, ArrayList<SteamStatLayout> stats) {
		this.SteamID64 = SteamID64;
		this.gameName = gameName;
		this.stats = stats;
	}
}
