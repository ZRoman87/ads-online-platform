package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, AdRepository adRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    /**
     * Приватный метод, который вытаскивает авторизованного пользователя
     * @return возращает пользователя
     */
    private User getCurrentUser() {
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principalUser = (UserDetails) authenticationUser.getPrincipal();
        return userRepository.findByPassword(principalUser.getPassword());
    }

    /**
     * Метод, который выводит все комментарии к определенному объявлению
     * @param adId (id объявления)
     * @return возращает List комментариев
     */
    @Override
    public List<CommentsDto> getComments(Integer adId) {
        return commentRepository
                .findByAds_Pk(adId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    /**
     * Метод, который добавляет комментарий к определенному объявлению
     * @param adId  (id объявления)
     * @param comment (текст комментария)
     * @return
     */
    @Override
    public CommentDto addComment(Integer adId, CreateCommentDto comment) {
        Comment entity = new Comment();
        entity.setText(comment.getText());
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setAuthor(getCurrentUser());
        entity.setAd(adRepository.findById(adId).get());
        commentRepository.save(entity);
        return commentMapper.toDto(entity);
    }

    /**
     * Метод, который удаляет комментарий
     * @param adId (id объявления)
     * @param commentId (id комментария)
     */
    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteByAdsIdAndId(adId, commentId);
    }

    /**
     * Метод, который изменяет текст комментария
     * @param adId (id объявления)
     * @param commentId (id комментария)
     * @param comment (текст комментария)
     * @return
     */
    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateCommentDto comment) {
        Comment entity = commentRepository.findByAdsIdAndId(adId, commentId);
        entity.setText(comment.getText());
        commentRepository.save(entity);
        return commentMapper.toDto(entity);
    }
}
