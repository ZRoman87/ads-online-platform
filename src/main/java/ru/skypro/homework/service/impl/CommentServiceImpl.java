package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.stream.Collectors;

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
     *
     * @return возвращает пользователя
     */
    private User getCurrentUser() {
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principalUser = (UserDetails) authenticationUser.getPrincipal();
        return userRepository.findByEmail(principalUser.getUsername());
    }

    /**
     * Метод, который выводит все комментарии к определенному объявлению
     *
     * @param adId (id объявления)
     * @return возвращает List комментариев
     */
    @Override
    public CommentsDto getComments(Integer adId) {
        return commentMapper.toCommentsDto(
                commentRepository
                        .findCommentsByAd_Pk(adId)
                        .size(),
                commentRepository
                        .findCommentsByAd_Pk(adId)
                        .stream()
                        .map(commentMapper::toDto)
                        .collect(Collectors.toList()));
    }

    /**
     * Метод, который добавляет комментарий к определенному объявлению
     *
     * @param adId    (id объявления)
     * @param commentText (текст комментария)
     * @return CommentDto (объект комментария)
     */
    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto commentText) {
        Comment comment = new Comment();
        comment.setText(commentText.getText());
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAuthor(getCurrentUser());
        comment.setAd(adRepository.findById(adId).get());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    /**
     * Метод, который удаляет комментарий
     *
     * @param adId      (id объявления)
     * @param commentId (id комментария)
     */
    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteCommentByAd_PkAndPk(adId, commentId);
    }

    /**
     * Метод, который изменяет текст комментария
     *
     * @param adId      (id объявления)
     * @param commentId (id комментария)
     * @param comment   (текст комментария)
     * @return CommentDto (объект комментария)
     */
    @Override
    public CommentDto updateComment(Integer adId,
                                    Integer commentId,
                                    CreateOrUpdateCommentDto comment) {
        Comment entity = commentRepository.findCommentByAd_PkAndPk(adId, commentId);
        entity.setText(comment.getText());

        return commentMapper.toDto(commentRepository.save(entity));
    }

}
