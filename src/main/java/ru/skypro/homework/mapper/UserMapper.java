package ru.skypro.homework.mapper;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UserPrincipalDto;
import ru.skypro.homework.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(@NonNull User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
        userDto.setImage(user.getImage());

        return userDto;
    }

    public UserPrincipalDto toUserPrincipalDto(@NonNull User user) {
        UserPrincipalDto userDto = new UserPrincipalDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());

        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setImage(userDto.getImage());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        return user;
    }

}
