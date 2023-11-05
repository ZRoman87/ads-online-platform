package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    Получение комментариев объявления
    @GetMapping("/{id}/comments")
    public List<CommentsDto> getComments(@PathVariable(value = "id") Integer adId) {
        List<CommentsDto> comments = commentService.getComments(adId);
        return ResponseEntity.ok(comments).getBody();
    }

//    Добавление комментария к объявлению
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable(value = "id") Integer adId,
                                                 @RequestBody CreateCommentDto comment) {
        CommentDto commentDto = commentService.addComment(adId, comment);
        return ResponseEntity.ok(commentDto);
    }

//    Удаление комментария
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "adId") Integer adId,
                                              @PathVariable(value = "commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

//    Обновление комментария
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "adId") Integer adId,
                                                    @PathVariable(value = "commentId") Integer commentId,
                                                    @RequestBody CreateCommentDto comment) {
        CommentDto commentDto = commentService.updateComment(adId, commentId, comment);
        return ResponseEntity.ok(commentDto);
    }

}
