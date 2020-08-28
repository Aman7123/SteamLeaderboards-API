package com.aaronrenner.SteamAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class SteamProfileGameInfo {
	private long appid;
	private String name;
	private String img_logo_url;
	private int playtime_forever;
	private int playtime_2weeks;
	
	public SteamProfileGameInfo(long appid, String name, int playtime_forever) {
		this.appid=appid;
		this.name=name;
		this.playtime_forever=playtime_forever;
	}
}
