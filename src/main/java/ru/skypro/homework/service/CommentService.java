package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentsDto> getComments(Integer adId);
    CommentDto addComment(Integer adId, CreateCommentDto comment);
    void deleteComment(Integer adId, Integer commentId);
    CommentDto updateComment(Integer adId, Integer commentId, CreateCommentDto comment);
}
