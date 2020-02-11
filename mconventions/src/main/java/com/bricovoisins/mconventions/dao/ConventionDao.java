package com.bricovoisins.mconventions.dao;

import com.bricovoisins.mconventions.model.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConventionDao extends JpaRepository<Convention, Integer> {
}
