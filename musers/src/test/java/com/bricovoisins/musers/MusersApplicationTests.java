package com.bricovoisins.musers;

import com.bricovoisins.musers.model.User;
import com.bricovoisins.musers.web.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
		newUser.setPassword("test");
		newUser.setEmail("test@example.com");
		newUser.setAddress("Address");
		newUser.setPostalCode(75000);
		newUser.setTown("Paris");
		newUser.setPoints(8);
		userController.insertUser(newUser);
		assertEquals(previousSize + 1, userController.getAllUsers().size());
		userController.deleteUser(userController.getUserByEmail("test@example.com").getId());
	}

	@Test
	void testDeleteUser() {
		User newUser = new User();
		newUser.setFirstName("Test");
		newUser.setLastName("Nom");
		newUser.setPassword("test");
		newUser.setEmail("test@example.com");
		newUser.setAddress("Address");
		newUser.setPostalCode(75000);
		newUser.setTown("Paris");
		newUser.setPoints(8);
		userController.insertUser(newUser);
		int previousSize = userController.getAllUsers().size();
		userController.deleteUser(userController.getUserByEmail("test@example.com").getId());
		assertEquals(previousSize - 1, userController.getAllUsers().size());
	}

	@Test
	void testGetAllUsers() {
		assertEquals(2, userController.getAllUsers().size());
	}

	@Test
	void testGetUserByEmail() {
		assertEquals(13, (int) userController.getUserByEmail("msanchez@test.com").getId());
	}
}
