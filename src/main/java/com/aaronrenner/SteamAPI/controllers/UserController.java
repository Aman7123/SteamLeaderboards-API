package com.aaronrenner.SteamAPI.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.UserService;

@RestController
public class UserController {
	
	final private String BASEURL = "/users";
	final private String SELECTUSERURL = BASEURL + "/{steamID64}";
	final private String FRIENDBASEURL = SELECTUSERURL + "/friendslist";
	final private String SELECTUSERFRIENDURL =  FRIENDBASEURL + "/{friendSteamID64}";
	
	@Autowired
	private UserService userService;
	
	// Root URL
	@GetMapping(BASEURL)
	public List<User> getUserList() {
		// TODO add catches
		return this.userService.getUserList();
	}
	
	@PostMapping(BASEURL)
	public User createUser(@RequestBody User newUser) {
		// TODO add catches
		return this.userService.createUser(newUser);	
	}
	
	// USER SPECIFIC ENDPOINT
	@GetMapping(SELECTUSERURL)
	public User getUser(@PathVariable String steamID64) {
		// TODO add catches
		return this.userService.getUser(steamID64);
	}
	
	/**
	@PatchMapping(SELECTUSERURL)
	public User updateUser() {
		TODO Create this patch properly
	}
	*/
	
	@DeleteMapping(SELECTUSERURL)
	public boolean deleteUser(@PathVariable String steamID64) {
		// TODO add catches
		return this.userService.deleteUser(steamID64);
	}
	
	// USER FRIENDS LIST COMMANDS
	@GetMapping(FRIENDBASEURL)
	public List<FriendID> getFriend(@PathVariable String steamID64) {
		// TODO add catches
		return this.userService.getFriend(steamID64);
	}
	
	@PostMapping(SELECTUSERFRIENDURL)
	public FriendID createFriend(@PathVariable String steamID64, @PathVariable String friendSteamID64) {
		// TODO add catches
		return this.userService.createFriend(steamID64, friendSteamID64);
	}
	
	@DeleteMapping(SELECTUSERFRIENDURL)
	public boolean deleteFriend(@PathVariable String steamID64, @PathVariable String friendSteamID64) {
		// TODO add catches
		return this.userService.deleteFriend(steamID64, friendSteamID64);
	}
}
