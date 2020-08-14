package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		return this.userService.getUserList();
	}
	
	@PostMapping(BASEURL)
	@ResponseStatus(value= HttpStatus.CREATED)
	public void createUser(@RequestBody User newUser) {
		this.userService.createUser(newUser);	
	}
	
	// USER SPECIFIC ENDPOINT
	@GetMapping(SELECTUSERURL)
	public User getUser(@PathVariable String steamID64) {
		return this.userService.getUser(steamID64);
	}
	
	/**
	@PatchMapping(SELECTUSERURL)
	public User updateUser() {
		TODO Create this patch properly
	}
	*/
	
	@DeleteMapping(SELECTUSERURL)
	public void deleteUser(@PathVariable String steamID64) {
		this.userService.deleteUser(steamID64);
	}
	
	// USER FRIENDS LIST COMMANDS
	@GetMapping(FRIENDBASEURL)
	public List<FriendID> getFriend(@PathVariable String steamID64) {
		return this.userService.getFriend(steamID64);
	}
	
	@PostMapping(SELECTUSERFRIENDURL)
	@ResponseStatus(value= HttpStatus.CREATED)
	public void createFriend(@PathVariable String steamID64, @PathVariable String friendSteamID64) {
		this.userService.createFriend(steamID64, friendSteamID64);
	}
	
	@DeleteMapping(SELECTUSERFRIENDURL)
	public void deleteFriend(@PathVariable String steamID64, @PathVariable String friendSteamID64) {
		this.userService.deleteFriend(steamID64, friendSteamID64);
	}
}
