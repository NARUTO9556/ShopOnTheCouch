package ru.skypro.homework.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.exception.AdNotFound;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.security.logger.FormLogInfo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Transactional
@Service
@Slf4j
public class AdsServiceImpl implements AdsService {
    private final AdRepository adRepository;

    private final AdMapper adMapper;
    private final UserService userService;
    private final ImageService imageService;
    private final UserMapper userMapper;



    public AdsServiceImpl(AdRepository adRepository,  AdMapper adMapper, UserService userService, ImageService imageService, UserMapper userMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userService = userService;
        this.imageService = imageService;
        this.userMapper = userMapper;
    }

    @Override
    public Ads getAllAds() {
        List<AdEntity> ads = adRepository.findAll();
        return adMapper.toAds(ads.size(),ads);

    }

    @Override
    public Ads getAdsMe(Authentication authentication) {
        List<AdEntity> ads = adRepository.findAllByAuthorId((long) userService.getUser(authentication).getId());
        return adMapper.toAds(ads.size(),ads);
    }



    @Override
    public Ad addAds(CreateOrUpdateAd createOrUpdateAd, MultipartFile imageFile, Authentication authentication) throws IOException {
        log.info(FormLogInfo.getInfo());
        AdEntity adEntity = adMapper.toEntity(createOrUpdateAd);
        User user = userService.getUser(authentication);
        adEntity.setAuthor(userMapper.toEntity(user));
        adEntity.setImage(imageService.uploadImage(imageFile));
        adRepository.save(adEntity);
        return adMapper.toAd(adEntity);
    }


    @Override
    public void removeAdsById(Long id) {
        adRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Ad updateAds(Long adId, CreateOrUpdateAd createOrUpdateAd) {
        AdEntity ad = adRepository.findById(adId).get();
        ad.setName(createOrUpdateAd.getTitle());
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        adRepository.save(ad);
        return adMapper.toAd(ad);


    }
    @SneakyThrows
    @Override
    public void updateAdsImage(long id, MultipartFile image, Authentication authentication) {
        log.info(FormLogInfo.getInfo());
        if (image == null) {
            throw new NotFoundException("New ad image not uploaded");
        }
        AdEntity adEntity = adRepository.findById(id).orElseThrow(AdNotFound::new);
        User user = userService.getUser(authentication);
        adEntity.setAuthor(userMapper.toEntity(user));
        imageService.remove(adEntity.getImage());
        adEntity.setImage(imageService.uploadImage(image));
        adRepository.save(adEntity);
    }

    @Override
    public ExtendedAd getAds(Long id) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(AdNotFound::new);
        return adMapper.toExtendedAd(adEntity);
    }
}