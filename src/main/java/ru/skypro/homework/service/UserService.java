package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {
    UserDto getAuthenticatedUser();

    boolean updatePassword(NewPasswordDto newPassword);

    UpdateUserDto updateUser(UpdateUserDto updatedUser);

    void updateAvatar(MultipartFile file);
}
