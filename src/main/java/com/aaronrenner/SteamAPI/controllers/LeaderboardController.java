package com.aaronrenner.SteamAPI.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.models.SteamProfile;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.aaronrenner.SteamAPI.services.LeaderboardService;

@RestController
public class LeaderboardController {

	final private String BASEURL = "/leaderboards/{steamID64}";
	final private String PROFILEURL = BASEURL + "/profiles";
	final private String GAMEAPPID = "/games/{appID}";
	final private String STATURL = BASEURL + "/statistics" + GAMEAPPID;
	final private String ACHIEVEMENTURL = BASEURL + "/achievements" + GAMEAPPID;
	
	@Autowired
	private LeaderboardService leaderboardService;
	
	// Root commands
	@GetMapping(PROFILEURL)
	public List<SteamProfile> getProfile(@PathVariable String steamID64) {
		return this.leaderboardService.getSteamProfile(steamID64);
	}
	
	@GetMapping(STATURL)
	public List<SteamUserStatInfo> getStatistic(@PathVariable String steamID64, @PathVariable String appID) {
		return this.leaderboardService.getSteamStats(steamID64, appID);
	}
	
	@GetMapping(ACHIEVEMENTURL)
	public List<SteamUserAchievementInfo> getAchievement(@PathVariable String steamID64, @PathVariable String appID) {
		return this.leaderboardService.getSteamAchievements(steamID64, appID);
	}
	
	
}
