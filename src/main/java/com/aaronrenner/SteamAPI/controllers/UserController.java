package com.aaronrenner.SteamAPI.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;

@RestController
public class UserController {
	
	final private String BASEURL = "/users";
	final private String SELECTUSERURL = BASEURL + "/{steamID64}";
	final private String FRIENDUSERURL = SELECTUSERURL + "friendslist/{friendSteamID64}";
	
	// Root URL
	@GetMapping(BASEURL)
	public List<User> getUserList() {
		// TODO Code this
		return null;
	}
	@PostMapping(BASEURL)
	public User createUser(@RequestBody User newUser) {
		// TODO Code this
		return null;
	}
	
	// USER SPECIFIC ENDPOINT
	@GetMapping(SELECTUSERURL)
	public User getUser(@PathVariable String steamID64) {
		// TODO Code this
		return null;
	}
	/**
	@PatchMapping(SELECTUSERURL)
	public User updateUser() {
		TODO Create this patch properly
	}
	*/
	@DeleteMapping(SELECTUSERURL)
	public void deleteUser(@PathVariable String steamID64) {
		// TODO code this
	}
	
	// USER FRIENDS LIST COMMANDS
	@GetMapping(FRIENDUSERURL)
	public List<FriendID> getFriend(@PathVariable String steamID64,
										@PathVariable String friendSteamID64) {
		// TODO CREATE THIS
		return null;
	}
	
	@PostMapping(FRIENDUSERURL)
	public FriendID createFriend(@PathVariable String steamID64,
								@PathVariable String friendSteamID64) {
		// TODO code this
		return null;
	}
	
	@DeleteMapping(FRIENDUSERURL)
	public void deleteFriend(@PathVariable String steamID64,
								@PathVariable String friendSteamID64) {
		// TODO code this
	}
}
