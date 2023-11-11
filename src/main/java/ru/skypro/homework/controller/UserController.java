package ru.skypro.homework.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService service;

    public UserController(final UserService service) {
        this.service = service;
    }

    @PostMapping("/set_password") // POST http://localhost:8080/users/set_password
    public ResponseEntity<String> setPassword(@RequestBody NewPasswordDto newPasswordDto) {
        UserDto user = service.getAuthenticatedUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
        }
        if (service.updatePassword(newPasswordDto)) {
            return ResponseEntity.ok("Password was updated");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Passwords do not match!");
        }
    }

    @GetMapping("/me") // GET http://localhost:8080/users/me
    public ResponseEntity<UserDto> getUser(@NonNull Authentication authentication) {
        return authentication.isAuthenticated()
                ? ResponseEntity.ok(service.getAuthenticatedUser())
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("/me") // PATCH http://localhost:8080/users/me
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody UpdateUserDto updateUserDto,
                                                    @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            UpdateUserDto updatedUser = service.updateUser(updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping(value = "/me/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // PATCH http://localhost:8080/users/me/image
    public ResponseEntity<String> updateAvatar(@RequestParam MultipartFile image,
                                               @NonNull Authentication authentication) throws IOException {
        if (authentication.isAuthenticated()) {
            service.updateAvatar(image);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
