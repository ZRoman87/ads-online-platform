package ru.skypro.homework.mapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AdMapper {

private final UserRepository userRepository;

    public ExtendedAdDto toExtendedDto(@NonNull Ad ad) {
        ExtendedAdDto adDto = new ExtendedAdDto();

        adDto.setPk(ad.getPk());
        adDto.setTitle(ad.getTitle());
        adDto.setDescription(ad.getDescription());
        adDto.setPrice(ad.getPrice());
        adDto.setImage(ad.getImage());
        adDto.setAuthorFirstName(ad.getAuthor().getFirstName());
        adDto.setAuthorLastName(ad.getAuthor().getLastName());
        adDto.setEmail(ad.getAuthor().getEmail());
        adDto.setPhone(ad.getAuthor().getPhone());

        return adDto;
    }

    public AdDto toDto(@NonNull Ad ad) {
        AdDto adDto = new AdDto();

        adDto.setPk(ad.getPk());
        adDto.setTitle(ad.getTitle());
        adDto.setPrice(ad.getPrice());
        adDto.setImage(ad.getImage());
        adDto.setAuthor(ad.getAuthor().getId());

        return adDto;
    }
    public AdsDto toAdsDto(Integer count, @NonNull List<AdDto> results) {
        AdsDto adsDto = new AdsDto();

        adsDto.setCount(count);
        adsDto.setResults(results);

        return adsDto;
    }

    public Ad toEntityFromExtendedDto(ExtendedAdDto adDto) {
        Ad ad = new Ad();

        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setDescription(adDto.getDescription());
        ad.setImage(adDto.getImage());

        return ad;
    }

    public Ad toEntityFromDto(AdDto adDto) {
        Ad ad = new Ad();

        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setImage(adDto.getImage());

        Optional.ofNullable(adDto.getAuthor())
                .ifPresent(authorId ->
                        ad.setAuthor(
                                userRepository.findById(authorId)
                                        .orElseThrow(UserNotFoundException::new)
                        )
                );

        return ad;
    }

}
