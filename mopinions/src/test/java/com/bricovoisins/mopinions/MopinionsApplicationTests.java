package com.bricovoisins.mopinions;

import com.bricovoisins.mopinions.controller.OpinionController;
import com.bricovoisins.mopinions.model.Opinion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MopinionsApplicationTests {

	@Autowired
	OpinionController opinionController;

	@Test
	void testInsertOpinion() {
		int previousSize = opinionController.getAllOpinions().size();
		Opinion newOpinion = new Opinion();
		newOpinion.setAuthor("Rémi Moustey");
		newOpinion.setOpinion("Avis");
		newOpinion.setUserId(63);
		newOpinion.setAuthorId(62);
		opinionController.insertOpinion(newOpinion);
		assertEquals(previousSize + 1, opinionController.getAllOpinions().size());
		Opinion addedOpinion = opinionController.getAllOpinionsByAuthor(62).get(1);
		opinionController.deleteOpinion(addedOpinion.getId());
	}

	@Test
	void testGetAllOpinionsByUser() {
		assertEquals(1, opinionController.getAllOpinionsByAuthor(62).size());
	}

	@Test
	void testGetOpinion() {
		Opinion newOpinion = new Opinion();
		newOpinion.setAuthor("Rémi Moustey");
		newOpinion.setOpinion("Avis");
		newOpinion.setUserId(63);
		newOpinion.setAuthorId(62);
		opinionController.insertOpinion(newOpinion);
		Opinion opinion = opinionController.getAllOpinionsByAuthor(62).get(0);
		assertEquals("Avis", opinionController.getOpinion(opinion.getId()).getOpinion());
		opinionController.deleteOpinion(opinion.getId());
	}

	@Test
	void testDeleteOpinion() {
		Opinion newOpinion = new Opinion();
		newOpinion.setAuthor("Rémi Moustey");
		newOpinion.setOpinion("Avis");
		newOpinion.setUserId(63);
		newOpinion.setAuthorId(62);
		opinionController.insertOpinion(newOpinion);
		Opinion addedOpinion = opinionController.getAllOpinionsByAuthor(62).get(1);
		int previousSize = opinionController.getAllOpinions().size();
		opinionController.deleteOpinion(addedOpinion.getId());
		assertEquals(previousSize - 1, opinionController.getAllOpinions().size());
	}
}
