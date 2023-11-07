package ru.skypro.homework.service;

import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

public interface AdService {
    AdDto create(CreateOrUpdateAdDto ad);
    ExtendedAdDto get(Integer id);
    AdsDto getAll();
    AdsDto getAuthorizedUserAds(Integer id);
    AdDto update(Integer id, CreateOrUpdateAdDto ad);
    void delete(Integer id);
}
