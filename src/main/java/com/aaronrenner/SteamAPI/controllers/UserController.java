package com.aaronrenner.SteamAPI.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.aaronrenner.SteamAPI.models.User;

@RestController
public class UserController {
	
	final private String BASEURL = "/users";
	final private String SELECTUSERURL = BASEURL + "/{steamID64}";
	final private String FRIENDUSERURL = SELECTUSERURL + "friendslist/{friends_steamID64}";
	
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
	
	/**
	 * TODO Code this user controller
	 */

}
