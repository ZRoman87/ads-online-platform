package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping(path = "/ads")
@CrossOrigin(value = "http://localhost:3000")
@AllArgsConstructor
public class AdController {

    private final AdService service;

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestPart(name = "properties") CreateOrUpdateAdDto ad,
                                       @RequestPart(name = "image") MultipartFile file,
                                       @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto addedAd = service.create(ad);
            return ResponseEntity.ok(addedAd);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExtendedAdDto> getAdById(@PathVariable(value = "id") Integer id,
                                                   @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            ExtendedAdDto ad = service.get(id);
            return (ad != null)
                    ? ResponseEntity.ok(ad)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAdById(@PathVariable(value = "id") Integer id,
                                             @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto foundAd = service.findAdById(id);
            if (foundAd == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                if (adBelongsToCurrentUserOrIsAdmin(foundAd)) {
                    service.delete(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<AdDto> updateAdById(@PathVariable(value = "id") Integer id,
                                              @RequestBody CreateOrUpdateAdDto ad,
                                              @NonNull Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AdDto foundAd = service.findAdById(id);
            if (foundAd == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                if (adBelongsToCurrentUserOrIsAdmin(foundAd)) {
                    AdDto updatedAd = service.update(id, ad);
                    return ResponseEntity.ok(updatedAd);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<AdsDto> getAuthorizedUserAds(@NonNull Authentication authentication) {
        return authentication.isAuthenticated()
                ? ResponseEntity.ok(service.getAuthorizedUserAds())
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping(
            path = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public Resource updateImageByAdId(@PathVariable(value = "id") Integer id,
                                      @RequestPart(name = "image") MultipartFile file) throws IOException {
        return new ByteArrayResource(Files.readAllBytes(Paths.get("mto.jpg")));
    }

    private boolean adBelongsToCurrentUserOrIsAdmin(AdDto ad) {
        User user = service.getCurrentUser();
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        Integer userId = user.getId();
        return isAdmin || Objects.equals(userId, ad.getAuthor());
    }

}
