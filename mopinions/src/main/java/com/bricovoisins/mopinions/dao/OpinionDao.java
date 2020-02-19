package com.bricovoisins.mopinions.dao;

import com.bricovoisins.mopinions.model.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionDao extends JpaRepository<Opinion, Integer> {
    Opinion findById(int id);
    List<Opinion> findAllByAuthorId(int authorId);
    List<Opinion> findAllByUserIdOrderByIdDesc(int userId);
    void deleteById(int id);
}
