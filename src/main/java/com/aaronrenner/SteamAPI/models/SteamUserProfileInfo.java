package com.aaronrenner.SteamAPI.models;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class SteamUserProfileInfo {

	private String steamid;
	private int communityvisibilitystate;
	private int profilestate;
	private String personaname;
	private String profileurl;
	private String avatar;
	private String avatarmedium;
	private String avatarfull;
	private String avatarhash;
	private long lastlogoff;
	private int personastate;
	private String realname;
	private String primaryclanid;
	private long timecreated;
	private int personastateflags;
	private String loccountrycode;
	private String locstatecode;
	
	public SteamUserProfileInfo() {
		
	}
	
}
