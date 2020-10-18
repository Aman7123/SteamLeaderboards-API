package com.aaronrenner.SteamAPI.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
/**
 * 
 * @author aaron renner
 * @since 1.0
 *
 */
public class PasswordEncoder {

	private BCryptPasswordEncoder psswdEncoder = new BCryptPasswordEncoder(11);

	public String encodePassword(String rawPassword) {
		return psswdEncoder.encode(rawPassword);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return psswdEncoder.matches(rawPassword, encodedPassword);
	}

}
