package com.aaronrenner.SteamAPI.services;

import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.aaronrenner.SteamAPI.models.Token;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.UserRepository;
import com.aaronrenner.SteamAPI.security.PasswordEncoder;

@SpringBootTest
public class LoginServiceTest {
	/**
	@InjectMocks
	private LoginServiceIMPL loginService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	private User fakeUser;
	private User databaseRecord;
	
	private String localKey;
	
	@BeforeEach
	public void setUp() {
		fakeUser = fakeUser(3);
		databaseRecord = fakeUserWithPassword(3, "1234");
		
		Optional<User> fakeUser_Optional = Optional.of(databaseRecord);
		Mockito.when(userRepository.findBySteamID64(Mockito.anyString())).thenReturn(fakeUser_Optional);
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(fakeUser_Optional);
	}
	
	
	@AfterEach
	public void tearDown() {
		fakeUser = null;
		databaseRecord = null;
	}
	
	@Test
	public void createToken() {
		Token newToken = loginService.createToken(fakeUser);
		localKey = newToken.getToken();
		assert(newToken.getToken() != null);
	}
	
	private User fakeUser(long number) {
		User newUser = new User();
		newUser.setId(number);
		newUser.setSteamID64("0000000000000000" + number);
		newUser.setPassword("1234");
		newUser.setUsername("test_user");
		newUser.setRole("admin");
		return newUser;
	}
	
	private User fakeUserWithPassword(long number, String passwordToEncode) {
		User newUser = new User();
		newUser.setId(number);
		newUser.setSteamID64("0000000000000000" + number);
		newUser.setPassword(passwordEncoder.encodePassword("1234"));
		newUser.setUsername("test_user");
		newUser.setRole("admin");
		return newUser;
	}
	*/
}
