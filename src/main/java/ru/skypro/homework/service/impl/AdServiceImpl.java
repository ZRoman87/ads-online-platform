package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
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
                adRepository.save(entity)
        );
    }

    @Override
    public ExtendedAdDto get(Integer id) {
        return adRepository
                .findById(id)
                .map(mapper::toExtendedDto)
                .orElse(null);
    }

    @Override
    public AdsDto getAll() {
        return mapper.toAdsDto(
                adRepository
                        .findAll()
                        .size(),
                adRepository
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
                adRepository
                        .findByAuthorId(id)
                        .size(),
                adRepository
                        .findByAuthorId(id)
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public AdDto update(Integer id, CreateOrUpdateAdDto ad) {
        AdDto adDto = findAdById(id);
        if (adBelongsToCurrentUserOrIsAdmin(adDto)) {
            return adRepository
                    .findById(id)
                    .map(oldAd -> {
                        oldAd.setPrice(ad.getPrice());
                        oldAd.setTitle(ad.getTitle());
                        oldAd.setDescription(ad.getDescription());
                        return mapper.toDto(adRepository.save(oldAd));
                    })
                    .orElse(null);
        }
        return null;
    }

    @Override
    public boolean delete(AdDto adDto) {
        if (adBelongsToCurrentUserOrIsAdmin(adDto)) {
            List<Comment> comments = commentRepository.findCommentsByAd_Pk(adDto.getPk());
            comments.forEach(comment -> {
                commentRepository.deleteById(comment.getPk());
            });
            adRepository.deleteById(adDto.getPk());
            return true;
        }
        return false;
    }

    @Override
    public AdDto findAdById(Integer id) {
        return adRepository
                .findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public String updateImage(final Integer id, final MultipartFile file) {
        AdDto adDto = findAdById(id);
        if (adBelongsToCurrentUserOrIsAdmin(adDto)) {
            // TODO: code for ad image uploading
        }
        return null;
    }

    private void writeToFile(Path path, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            fos.write(data);
        } catch (IOException e) {
            // throw new UserAvatarProcessingException();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean adBelongsToCurrentUserOrIsAdmin(AdDto adDto) {
        User user = this.getCurrentUser();
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        Integer userId = user.getId();
        return isAdmin || Objects.equals(userId, adDto.getAuthor());
    }

}
