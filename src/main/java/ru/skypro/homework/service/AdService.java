package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.User;

public interface AdService {
    User getCurrentUser();

    AdDto create(CreateOrUpdateAdDto ad);

    ExtendedAdDto get(Integer id);

    AdsDto getAll();

    AdsDto getAuthorizedUserAds();

    AdDto update(Integer id, CreateOrUpdateAdDto ad);

    void delete(Integer id);

    AdDto findAdById(Integer id);

    String updateImage(MultipartFile file);
}
