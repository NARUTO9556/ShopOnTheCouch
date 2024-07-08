package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;

public interface AdsService {
    Ads getAllAds();
    Ads getAdsMe(Authentication authentication);
    Ad addAds(CreateOrUpdateAd createOrUpdateAd, MultipartFile imageFiles, Authentication authentication) throws IOException;
    void removeAdsById(Long id);
    @Transactional
    Ad updateAds(Long adId, CreateOrUpdateAd createOrUpdateAd);

    void updateAdsImage(long id, MultipartFile image, Authentication authentication);
    ExtendedAd getAds(Long id);
}
