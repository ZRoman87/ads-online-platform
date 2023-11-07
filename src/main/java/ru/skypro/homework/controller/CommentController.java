package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping(path = "ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;
    private final AdService adService;

    public CommentController(CommentService commentService, final AdService adService) {
        this.commentService = commentService;
        this.adService = adService;
    }

//    Получение комментариев объявления
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable(value = "id") Integer adId) {
        return ResponseEntity.ok(commentService.getComments(adId));
    }

//    Добавление комментария к объявлению
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable(value = "id") Integer adId,
                                                 @RequestBody CreateOrUpdateCommentDto comment) {
        ExtendedAdDto foundAd = adService.get(adId);
        if (foundAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CommentDto addedComment = commentService.addComment(adId, comment);
        return ResponseEntity.ok(addedComment);
    }

//    Удаление комментария
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "adId") Integer adId,
                                              @PathVariable(value = "commentId") Integer commentId) {
        ExtendedAdDto foundAd = adService.get(adId);
        if (foundAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

//    Обновление комментария
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "adId") Integer adId,
                                                    @PathVariable(value = "commentId") Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto comment) {
        ExtendedAdDto foundAd = adService.get(adId);
        if (foundAd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CommentDto commentDto = commentService.updateComment(adId, commentId, comment);
        return ResponseEntity.ok(commentDto);
    }

}
