package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

public interface CommentService {
    CommentsDto getComments(Integer adId);
    CommentDto addComment(Integer adId, CreateOrUpdateCommentDto commentText);
    void deleteComment(Integer adId, Integer commentId);
    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto comment);
}
