package com.bricovoisins.mconventions;

import com.bricovoisins.mconventions.model.Convention;
import com.bricovoisins.mconventions.web.controller.ConventionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MconventionsApplicationTests {

	@Autowired
	ConventionController conventionController;

//	@Test
//	void testGetAllConventions() {
//		assertEquals(3, conventionController.getAllConventions().size());
//	}

//	@Test
//	void testGetOneConvention() {
//		assertEquals(70, conventionController.getOneConventionById(70).getId());
//	}

//	@Test
//	void testGetAllConventionsOfSender() {
//		assertEquals(1, conventionController.getAllConventionsOfSender().size());
//	}

	@Test
	void testDeleteConvention() {
		Convention convention = new Convention();
		convention.setSenderId(62);
		convention.setRecipientId(63);
		convention.setFirstNameRecipient("Jean");
		convention.setLastNameRecipient("J.");
		convention.setDateConvention(LocalDate.of(2020, 3, 1));
		convention.setBeginningHour(LocalTime.of(15, 0));
		convention.setTimeIntervention(LocalTime.of(3, 15));
		convention.setPlace("Adresse du lieu");
		convention.setMessage("Message de la convention");
		convention.setValidatedByRecipient(false);
		convention.setEndedBySender(false);
		conventionController.insertConvention(convention);
		int previousSize = conventionController.getAllConventions().size();
		Convention addedConvention = conventionController.getAllConventionsOfSender(62).get(0);
		conventionController.deleteConvention(addedConvention.getId());
		assertEquals(previousSize - 1, conventionController.getAllConventions().size());
	}

	@Test
	void testInsertConvention() {
		int previousSize = conventionController.getAllConventions().size();
		Convention convention = new Convention();
		convention.setSenderId(62);
		convention.setRecipientId(63);
		convention.setFirstNameRecipient("Jean");
		convention.setLastNameRecipient("J.");
		convention.setDateConvention(LocalDate.of(2020, 3, 1));
		convention.setBeginningHour(LocalTime.of(15, 0));
		convention.setTimeIntervention(LocalTime.of(3, 15));
		convention.setPlace("Adresse du lieu");
		convention.setMessage("Message de la convention");
		convention.setValidatedByRecipient(false);
		convention.setEndedBySender(false);
		conventionController.insertConvention(convention);
		assertEquals(previousSize + 1, conventionController.getAllConventions().size());
		Convention addedConvention = conventionController.getAllConventionsOfSender(62).get(0);
		conventionController.deleteConvention(addedConvention.getId());
	}

//	@Test
//	void testGetListCurrentConventionsUser() {
//		assertEquals(1, conventionController.getListCurrentConventionsUser(62).size());
//	}
//
//	@Test
//	void testGetListValidatedConventionsUser() {
//		assertEquals(1, conventionController.getListValidatedConventionsUser(62).size());
//	}
//
//	@Test
//	void testGetListEndedConventionsUser() {
//		assertEquals(1, conventionController.getListEndedConventionsUser(62).size());
//	}

//	@Test
//	void testGetListNoValidatedConventionsHelper() {
//		assertEquals(1, conventionController.getListNoValidatedConventionsHelper(63));
//	}
//
//	@Test
//	void testGetListValidatedConventionsHelper() {
//		assertEquals(1, conventionController.getListValidatedConventionsUser(63));
//	}

	@Test
	void testUpdateReservation() {
		Convention convention = new Convention();
		convention.setSenderId(62);
		convention.setRecipientId(63);
		convention.setFirstNameRecipient("Jean");
		convention.setLastNameRecipient("J.");
		convention.setFirstNameSender("Rémi");
		convention.setLastNameSender("M.");
		convention.setDateConvention(LocalDate.of(2020, 3, 1));
		convention.setBeginningHour(LocalTime.of(15, 0));
		convention.setTimeIntervention(LocalTime.of(3, 15));
		convention.setPlace("Adresse du lieu");
		convention.setMessage("Message de la convention");
		convention.setValidatedByRecipient(false);
		convention.setEndedBySender(false);
		conventionController.insertConvention(convention);
		convention.setValidatedByRecipient(true);
		conventionController.updateConvention(convention);
		assertTrue(conventionController.getAllConventionsOfSender(62).get(0).getValidatedByRecipient());
		conventionController.deleteConvention(conventionController.getAllConventionsOfSender(62).get(0).getId());
	}
}
