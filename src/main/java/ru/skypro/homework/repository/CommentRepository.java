package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByAds_Pk(Integer pk);
    Comment findByAdsIdAndId(Integer adId, Integer id);
    void deleteByAdsIdAndId(Integer adId, Integer id);

}
