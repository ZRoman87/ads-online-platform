package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository repository;
    private final AdMapper mapper;

    @Override
    public AdDto create(AdDto ad) {

        //--------------------------------
        // Need to fix on next week - Author should be set automatically
        AdDto adDto  = new AdDto();
        adDto = ad;
        ad.setAuthor(1);
        //--------------------------------
        return mapper.toDto(
                repository.save(
                        mapper.toEntityFromDto(ad)
                )
        );
    }

    @Override
    public ExtendedAdDto get(Integer id) {
        return repository
                .findById(id)
                .map(mapper::toExtendedDto)
                .orElseThrow(() -> new AdNotFoundException());
    }
    @Override
    public AdsDto getAll(){
        return mapper.toAdsDto(
                repository
                        .findAll()
                        .size(),
                repository
                        .findAll()
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList()));
    }
    @Override
    public AdsDto getAuthorizedUserAds(Integer id) {
        return mapper.toAdsDto(
                repository
                        .findByAuthorId(id)
                        .size(),
                repository
                        .findByAuthorId(id)
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList()));
    }

    public AdDto update(Integer id, CreateOrUpdateAdDto ad) {
        
        return repository
                .findById(id)
                .map(oldAd -> {
                    oldAd.setPrice(ad.getPrice());
                    oldAd.setTitle(ad.getTitle());
                    oldAd.setDescription(ad.getDescription());
                    return mapper.toDto(repository.save(oldAd));
                })
                .orElseThrow(AdNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).orElseThrow(AdNotFoundException::new);
        repository.deleteById(id);
    }





}
