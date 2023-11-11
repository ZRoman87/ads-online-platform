package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AdMapper mapper;

    @Override
    public User getCurrentUser() {
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principalUser = (UserDetails) authenticationUser.getPrincipal();
        return userRepository.findByEmail(principalUser.getUsername());
    }

    @Override
    public AdDto create(CreateOrUpdateAdDto ad) {
        Ad entity = new Ad();
        entity.setAuthor(getCurrentUser());
        entity.setPrice(ad.getPrice());
        entity.setTitle(ad.getTitle());
        entity.setDescription(ad.getDescription());

        return mapper.toDto(
                repository.save(entity)
        );
    }

    @Override
    public ExtendedAdDto get(Integer id) {
        return repository
                .findById(id)
                .map(mapper::toExtendedDto)
                .orElse(null);
    }

    @Override
    public AdsDto getAll() {
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
    public AdsDto getAuthorizedUserAds() {
        User user = this.getCurrentUser();
        Integer id = user.getId();
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

    @Override
    public AdDto update(Integer id, CreateOrUpdateAdDto ad) {
        return repository
                .findById(id)
                .map(oldAd -> {
                    oldAd.setPrice(ad.getPrice());
                    oldAd.setTitle(ad.getTitle());
                    oldAd.setDescription(ad.getDescription());
                    return mapper.toDto(repository.save(oldAd));
                })
                .orElse(null);
    }

    @Override
    public void delete(Integer id) {
        List<Comment> comments = commentRepository.findCommentsByAd_Pk(id);
        comments.forEach(comment -> {
            commentRepository.deleteById(comment.getPk());
        });
        repository.deleteById(id);
    }

    @Override
    public AdDto findAdById(Integer id) {
        return repository
                .findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

}
