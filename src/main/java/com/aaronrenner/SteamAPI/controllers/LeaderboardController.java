package com.aaronrenner.SteamAPI.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.models.SteamProfile;

@RestController
public class LeaderboardController {

	final private String BASEURL = "/leaderboards/{steamID64}";
	final private String PROFILEURL = BASEURL + "/profile";
	final private String STATURL = BASEURL + "/statistics";
	final private String ACHIEVEMENTURL = BASEURL + "/achievements";
	
	// Root commands
	@GetMapping(PROFILEURL)
	public List<SteamProfile> getProfile(@PathVariable String steamID64) {
		// TODO code this
		return null;
	}
	
	// TODO finish adding get methods for stat and achievement
	
	
}
