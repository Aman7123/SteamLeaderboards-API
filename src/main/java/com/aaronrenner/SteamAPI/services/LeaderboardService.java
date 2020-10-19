package com.aaronrenner.SteamAPI.services;

import java.util.List;

import com.aaronrenner.SteamAPI.models.SteamProfile;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;

/**
 * 
 * @author aaron renner
 * @version 3.0
 *
 */
public interface LeaderboardService {
	
	/**
	 * WARNING: THIS METHOD CONTAINS A COMPLEX NETWORK OF MULTI-THREADED METHODS
	 * 
	 * Time Clocks (Before Rework) - {7.59s, 7.46s, 7.70s, 9.04s, 7.89s, 7.96s, 6.99s}
	 * Time clocks (After MiltiThreading) - {2.21s, 2.25s, 4.65s, 2.28s, 1201ms, 2.31s}}
	 * 
	 * @since 3.0
	 * @param SteamID64 Any string, hopefully it is that of a user from the Steam system which also has friends in my system.
	 * @return Collection of SteamProfile objects creates for the provided parameter user and their friends.
	 */
	List<SteamProfile> getSteamProfile(String SteamID64);
	
	/**
	 * 
	 * @since 1.0
	 * @param SteamID64
	 * @param appID
	 * @return
	 */
	List<SteamUserStatInfo> getSteamStats(String SteamID64, String appID);
	
	/**
	 * 
	 * @since 1.0
	 * @param SteamID64
	 * @param appID
	 * @return
	 */
	List<SteamUserAchievementInfo> getSteamAchievements(String SteamID64, String appID);

}
