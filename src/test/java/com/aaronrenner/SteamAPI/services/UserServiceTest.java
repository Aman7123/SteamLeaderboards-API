package com.aaronrenner.SteamAPI.services;

import static org.junit.Assert.assertNotNull;

import java.util.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.UserRepository;

@SpringBootTest
public class UserServiceTest {
	
	@InjectMocks
	private UserServiceIMPL userService;
	
	@InjectMocks
	private UserRepository userRepository;
	
	private List<User> fakeUserList;
	private User fakeUser;
	private List<FriendID> fakeFriendList;
	private FriendID fakeFriend;
	
	@BeforeEach
	public void setUp() {
		fakeUserList = fakeUserList();
		fakeUser = fakeUser(3);
		fakeFriendList = fakeFriendList();
		fakeFriend = fakeFriend("12345678901234567");
		//getUserList
		Mockito.when(userRepository.findAll()).thenReturn(fakeUserList);
		//getUserIndividual
		Optional<User> fakeUser_Optional = Optional.of(fakeUser);
		Mockito.when(userRepository.findBySteamID64(Mockito.anyString())).thenReturn(fakeUser_Optional);
		//createUser
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(fakeUser);
	}
	
	@AfterEach
	public void tearDown() {
		fakeUserList = null;
		fakeUser = null;
		fakeFriendList = null;
		fakeFriend = null;
	}
	
	
	@Test
	public void getUserList_Success() {
		List<User> realUserList = userService.getUserList();
		
		assertNotNull(realUserList);
		assert(fakeUserList.size() == realUserList.size());
	}
	
	@Test
	public void getUserBySteamID_Success() {
		// Any ID should work
		User realUser = userService.getUser("007");
		assertNotNull(realUser);
		assert(fakeUser.getSteamID64() == realUser.getSteamID64());
	}
	
	@Test
	public void createUser_Success() {
		
		User realUser = userService.createUser(fakeUser);
		int userCountBefore = fakeUserList.size();
		fakeUserList.add(fakeUser);
		
		assert(realUser.getSteamID64() == fakeUser.getSteamID64());
		assert(userCountBefore < fakeUserList.size());
	}
	
	@Test
	public void updateUser_Success() {
		User fakeUser_updated = fakeUser;
		fakeUser_updated.setUsername("updated_username");
		
		fakeUser_updated = userService.updateUser(fakeUser_updated.getSteamID64(), fakeUser_updated);
		
		assert(fakeUser.getUsername() != fakeUser_updated.getUsername());
	}
	
	@Test
	public void getFriends_Success() {
		List<FriendID> realFriendList = userService.getFriend("007");
		
		assertNotNull(realFriendList);
		assert(realFriendList.size() == fakeFriendList.size());
	}
	
	@Test
	public void addFriend_Success() {
		FriendID realFriend = userService.createFriend("007", "007");
		
		assertNotNull(realFriend);
		assert(realFriend.getSteamID64() == fakeFriend.getSteamID64());
	}

	// TODO create delete test
	
	private List<User> fakeUserList() {
		List<User> userList = new ArrayList<>();
		userList.add(fakeUser(1));
		userList.add(fakeUser(2));
		return userList;
	}
	
	private User fakeUser(long number) {
		User newUser = new User();
		newUser.setId(number);
		newUser.setSteamID64("00" + number);
		newUser.setPassword("1234");
		newUser.setUsername("test_user");
		newUser.getFriendList().add(fakeFriend("12345678901234567"));
		return newUser;
	}
	
	private List<FriendID> fakeFriendList() {
		List<FriendID> friendList = new ArrayList<>();
		friendList.add(fakeFriend("12345678901234567"));
		return friendList;
	}
	
	private FriendID fakeFriend(String IDToHave) {
		FriendID newFriend = new FriendID();
		newFriend.setSteamID64(IDToHave);
		return newFriend;
	}
}
