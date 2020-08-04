package com.aaronrenner.SteamAPI.services;

import com.aaronrenner.SteamAPI.models.SteamProfile;

public interface LeaderboardService {
	
	SteamProfile getSteamProfile(String SteamID64);

}
