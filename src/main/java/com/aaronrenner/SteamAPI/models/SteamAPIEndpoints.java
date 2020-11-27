package com.aaronrenner.SteamAPI.models;

/**
 * 
 * @author aaron renner
 * @version 1.0
 *
 */
public enum SteamAPIEndpoints {
	steamProfileEndpoint("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key="),
	steamGameStatsEndpoint("http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v2/?key="),
	steamRecentlyPlayedEndpoint("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?key="),
	steamOwnedGamesEndpoint("http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?include_appinfo=true&include_played_free_games=true&key="),
	steamProfileLevelEndpoint("http://api.steampowered.com/IPlayerService/GetSteamLevel/v1/?key="),
	steamProfileBadgeEndpoint("http://api.steampowered.com/IPlayerService/GetBadges/v1/?key=");

	private String url;

	SteamAPIEndpoints(String string) {
		this.url = string;
	}

	public String getURL() {
		return url;
	}
}
