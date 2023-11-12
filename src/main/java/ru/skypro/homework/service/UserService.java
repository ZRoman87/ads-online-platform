package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {
    UserDto getAuthenticatedUser();

    UpdateUserDto updateUser(UpdateUserDto updatedUser);

    void updateAvatar(MultipartFile file);

    boolean updatePassword(String email, String currentPassword, String newPassword);
}
