package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public ResponseEntity<AdDto> addAd(@RequestPart(name = "properties") AdDto ad,
                                       @RequestPart(name = "image") MultipartFile file) {
        AdDto addedAd = service.create(ad);
        return ResponseEntity.ok(addedAd);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExtendedAdDto> getAdById(@PathVariable(value = "id") Integer id) {
        ExtendedAdDto ad = service.get(id);
        if (ad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ad);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAdById(@PathVariable(value = "id") Integer id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<AdDto> updateAdById(@PathVariable(value = "id") Integer id,
                                              @RequestBody CreateOrUpdateAdDto ad) {
        AdDto updatedAd = service.update(id, ad);
        return ResponseEntity.ok(updatedAd);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<AdsDto> getAuthorizedUserAds() {
        Integer id = 1; //need to fix on next week - how to hand over Authorized User Id?
        return ResponseEntity.ok(service.getAuthorizedUserAds(id));
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

}
