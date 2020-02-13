package com.bricovoisins.musers;

import com.bricovoisins.musers.model.User;
import com.bricovoisins.musers.web.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MusersApplicationTests {

	@Autowired
	UserController userController;

	@Test
	void testInsertUser() {
		int previousSize = userController.getAllUsers().size();
		User newUser = new User();
		newUser.setFirstName("Test");
		newUser.setLastName("Nom");
		newUser.setAge(50);
		newUser.setPassword("test");
		newUser.setEmail("test@example.com");
		newUser.setTown("Paris");
		newUser.setPoints(10);
		newUser.setAvatar("/avatars/default.png");
		newUser.setLevelGardening(null);
		newUser.setLevelElectricity(null);
		newUser.setLevelPlumbing(null);
		newUser.setLevelCarpentry("expert-carpentry");
		newUser.setLevelPainting(null);
		newUser.setLevelMasonry(null);
		newUser.setLevelDiy(null);
		newUser.setDescription("Description");
		userController.insertUser(newUser);
		assertEquals(previousSize + 1, userController.getAllUsers().size());
		userController.deleteUser(userController.getUserByEmail("test@example.com").getId());
	}

	@Test
	void testDeleteUser() {
		User newUser = new User();
		newUser.setFirstName("Test");
		newUser.setLastName("Nom");
		newUser.setAge(50);
		newUser.setPassword("test");
		newUser.setEmail("test@example.com");
		newUser.setTown("Paris");
		newUser.setPoints(10);
		newUser.setAvatar("/avatars/default.png");
		newUser.setLevelGardening(null);
		newUser.setLevelElectricity(null);
		newUser.setLevelPlumbing(null);
		newUser.setLevelCarpentry("expert-carpentry");
		newUser.setLevelPainting(null);
		newUser.setLevelMasonry(null);
		newUser.setLevelDiy(null);
		newUser.setDescription("Description");
		userController.insertUser(newUser);
		int previousSize = userController.getAllUsers().size();
		userController.deleteUser(userController.getUserByEmail("test@example.com").getId());
		assertEquals(previousSize - 1, userController.getAllUsers().size());
	}

//	@Test
//	void testGetAllUsers() {
//		assertEquals(30, userController.getAllUsers().size());
//	}

//	@Test
//	void testGetUserByEmail() {
//		assertEquals(13, (int) userController.getUserByEmail("msanchez@test.com").getId());
//	}

//	@Test
//	void testGetSearchedUser() {
//		assertEquals(3, userController.getSearchedUsers("gardening").size());
//	}
//
//	@Test
//	void testGetSearchedUserEmptyList() {
//		assertEquals(0, userController.getSearchedUsers("wrong search").size());
//	}

//	@Test
//	void testUpdateUser() {
//		User newUser = new User();
//		newUser.setFirstName("Test");
//		newUser.setLastName("Nom");
//		newUser.setAge(50);
//		newUser.setPassword("test");
//		newUser.setEmail("test@example.com");
//		newUser.setTown("Paris");
//		newUser.setPoints(10);
//		newUser.setAvatar("/avatars/default.png");
//		newUser.setLevelGardening(null);
//		newUser.setLevelElectricity(null);
//		newUser.setLevelPlumbing(null);
//		newUser.setLevelCarpentry("expert-carpentry");
//		newUser.setLevelPainting(null);
//		newUser.setLevelMasonry(null);
//		newUser.setLevelDiy(null);
//		newUser.setDescription("Description");
//		userController.insertUser(newUser);
//		User addedUser = userController.getUserByEmail("test@example.com");
//		addedUser.setPoints(999);
//		userController.updateUser(addedUser);
//		//assertEquals(70, userController.getUserByEmail("test@example.com").getId());
//		assertEquals(999, userController.getUserByEmail("test@example.com").getPoints());
//		userController.deleteUser(userController.getUserByEmail("test@example.com").getId());
//	}
}
