package ru.skypro.homework.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

import java.util.Objects;

@RestController
@RequestMapping(path = "ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;
    private final AdService adService;

    public CommentController(final CommentService commentService,
                             final AdService adService) {
        this.commentService = commentService;
        this.adService = adService;
    }

    /**
     * Получение всех комментариев объявления для авторизованного пользователя
     *
     * @param adId           Integer
     * @param authentication Authentication
     * @return CommentsDto
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable(value = "id") Integer adId,
                                                   @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto foundAd = adService.findAdById(adId);
            if (foundAd == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                CommentsDto comments = commentService.getComments(adId);
                return ResponseEntity.ok(comments);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Добавление комментария к объявлению
     *
     * @param adId           Integer
     * @param comment        CreateOrUpdateCommentDto
     * @param authentication Authentication
     * @return CommentDto
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable(value = "id") Integer adId,
                                                 @RequestBody CreateOrUpdateCommentDto comment,
                                                 @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto foundAd = adService.findAdById(adId);
            if (foundAd == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                CommentDto addedComment = commentService.addComment(adId, comment);
                return ResponseEntity.ok(addedComment);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Удаление комментария по ID объявления и ID комментария для авторизованного пользователя
     *
     * @param adId           Integer
     * @param commentId      Integer
     * @param authentication Authentication
     * @return Void (статус 200 OK)
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "adId") Integer adId,
                                              @PathVariable(value = "commentId") Integer commentId,
                                              @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto foundAd = adService.findAdById(adId);
            CommentDto foundComment = commentService.findCommentByAdIdAndCommentId(adId, commentId);
            if (foundAd == null || foundComment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                if (commentBelongsToCurrentUser(foundComment)) {
                    commentService.deleteComment(adId, commentId);
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновление комментария
     *
     * @param adId           Integer
     * @param commentId      Integer
     * @param comment        CreateOrUpdateCommentDto
     * @param authentication Authentication
     * @return CommentDto
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "adId") Integer adId,
                                                    @PathVariable(value = "commentId") Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto comment,
                                                    @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto foundAd = adService.findAdById(adId);
            CommentDto foundComment = commentService.findCommentByAdIdAndCommentId(adId, commentId);
            if (foundAd == null || foundComment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                if (commentBelongsToCurrentUser(foundComment)) {
                    CommentDto commentDto = commentService.updateComment(adId, commentId, comment);
                    return ResponseEntity.ok(commentDto);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean commentBelongsToCurrentUser(CommentDto comment) {
        User user = adService.getCurrentUser();
        Integer userId = user.getId();
        return Objects.equals(userId, comment.getAuthor());
    }

}
