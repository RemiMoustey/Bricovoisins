package com.bricovoisins.mconventions.dao;

import com.bricovoisins.mconventions.model.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConventionDao extends JpaRepository<Convention, Integer> {
    List<Convention> findBySenderIdAndIsValidatedByRecipientIsFalse(int senderId);
    List<Convention> findBySenderIdAndIsValidatedByRecipientIsTrueAndIsEndedBySenderIsFalse(int userId);
    List<Convention> findBySenderIdAndIsEndedBySenderIsTrue(int userId);
    List<Convention> findByRecipientIdAndIsEndedBySenderIsTrue(int userId);
    List<Convention> findAllBySenderId(int senderId);
    List<Convention> findByRecipientIdAndIsValidatedByRecipientIsFalse(int recipientId);
    List<Convention> findByRecipientIdAndIsValidatedByRecipientIsTrueAndIsEndedBySenderIsFalse(int recipientId);
    Convention findById(int id);
}
