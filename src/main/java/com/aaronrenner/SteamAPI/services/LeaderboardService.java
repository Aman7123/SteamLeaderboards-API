package com.aaronrenner.SteamAPI.services;

import java.util.List;

import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamUserProfileInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;

public interface LeaderboardService {
	
	List<SteamUserProfileInfo> getSteamProfile(String SteamID64);
	List<SteamUserStatInfo> getSteamStats(String SteamID64, String appID);
	List<SteamUserAchievementInfo> getSteamAchievements(String SteamID64, String appID);

}
