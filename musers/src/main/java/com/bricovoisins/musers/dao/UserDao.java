package com.bricovoisins.musers.dao;

import com.bricovoisins.musers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    User findById(int id);
    User findByEmail(String email);
    List<User> findAllByLevelGardeningIsNotNullOrLevelDiyIsNotNull();
    List<User> findAllByLevelElectricityIsNotNullOrLevelDiyIsNotNull();
    List<User> findAllByLevelPlumbingIsNotNullOrLevelDiyIsNotNull();
    List<User> findAllByLevelCarpentryIsNotNullOrLevelDiyIsNotNull();
    List<User> findAllByLevelPaintingIsNotNullOrLevelDiyIsNotNull();
    List<User> findAllByLevelMasonryIsNotNullOrLevelDiyIsNotNull();
    List<User> findAllByLevelDiyIsNotNull();
}
