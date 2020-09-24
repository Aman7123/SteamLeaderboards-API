package com.aaronrenner.SteamAPI.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import java.net.*;
import java.util.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.LoginService;
import com.aaronrenner.SteamAPI.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	
	private String adminToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJzdGVhbUlENjQiOiIwMDciLCJ1c2VybmFtZSI6InRlc3RfdXNlciJ9.EfMMLb7UX40A_x4bJAzQVhdaOxFe4jtNAUYFXlvp3qw";
	private String userEndpoint = "/users";
	private String userSpecificEndpoint = userEndpoint + "/007";
	private String userEndpointOfFriends = userSpecificEndpoint + "/friends-list";
	private String userEndpointModifyFriends = userEndpointOfFriends + "76561198440364879";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private UserService userService;
	
	@Mock
	private LoginService loginService;
	
	@Before
	public void setUp() throws Exception {
		UserController userController = new UserController();
		userController.setUserService(userService);
		userController.setLoginService(loginService);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void lookupAllUsers() throws Exception {
		List<User> allUserList = new ArrayList<>();
		allUserList.add(fakeUserModel());
		allUserList.add(fakeAdminModel());
		Mockito.when(userService.getUserList()).thenReturn(allUserList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", adminToken)).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		ObjectMapper om = new ObjectMapper();
		User[] users = om.readValue(mvcResult.getResponse().getContentAsString(), User[].class);
		assert(users.length > 0);
	}
	
	@Test 
	public void lookupSelf() throws Exception {
		User testUser = new User();
		testUser.setSteamID64("007");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users" + "/" + testUser.getSteamID64()).header("Authorization", adminToken)).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		ObjectMapper om = new ObjectMapper();
		User responseUser = om.readValue(mvcResult.getResponse().getContentAsString(), User.class);
		assert(responseUser.getUsername().equals("test_user"));
	}
	
	@Test
	public void createUser() throws Exception {
		//TODO finish this post
	}
	
	/**
	@Test
	public void updateUser() throws Exception {
		
	}
	
	@Test
	public void deleteUser() throws Exception {
		//Mockito.when(mockRepository.delete(Mockito.any(User.class))).thenReturn(createAdminModel());
	}
	
	@Test
	public void getFriends() throws Exception {
		//this.mockMvc.perform(get(uriFromString(userEndpointOfFriends)).header("Authorization", "Bearer " + adminToken)).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void addFriend() throws Exception {
		
	}
	
	@Test
	public void deleteFriend() throws Exception {
	}
	*/
	
	private URI uriFromString(String address) {
		URI newURI = null;
		
		try {
			newURI = new URI(address);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newURI;
	}
	
	private User fakeUserModel() {
		User newUser = new User();
		newUser.setUsername("mock_user");
		newUser.setSteamID64("000000");
		newUser.setPassword("1234");
		return newUser;
	}
	
	private User fakeAdminModel() {
		User newUser = new User();
		newUser.setUsername("mock_admin");
		newUser.setSteamID64("000000");
		newUser.setPassword("1234");
		return newUser;
	}
	
	private User fake_realUser() {
		User newUser = new User();
		newUser.setUsername("fake_user");
		newUser.setSteamID64("12345678901234567");
		newUser.setPassword("1234");
		return newUser;
	}

}
