package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/ads")
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Объявления")
@RestController
public class AdsController {
    private final AdsService adsService;
    private final ImageService imageService;


    public AdsController(AdsService adsService,ImageService imageService,AdMapper adMapper) {
        this.adsService = adsService;
        this.imageService = imageService;


    }

    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(responseCode = "200",description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class)))
            }
    )
    @GetMapping
    public ResponseEntity<Ads> getAllAds(){
        return ResponseEntity.ok(adsService.getAllAds());
    }
    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(responseCode = "201",description = "Created",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(responseCode = "401",description = "Unauthorized",
                            content = {@Content()})
            }
    )


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<Ad> addAd
            (@RequestPart  CreateOrUpdateAd properties, @RequestPart MultipartFile image, Authentication authentication) throws IOException {
        System.out.println("Прошло добавление");
        return ResponseEntity.ok(adsService.addAds(properties,image,authentication));
    }
    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(responseCode = "200",description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAd.class))),
                    @ApiResponse(responseCode = "401",description = "Unauthorized",
                            content = {@Content()}),
                    @ApiResponse(responseCode = "404",description = " Not found",
                            content = {@Content()})

            }
    )

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Long id){
        return ResponseEntity.ok(adsService.getAds(id));
    }
    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(responseCode = "204",description = "No content"),
                    @ApiResponse(responseCode = "401",description = "Unauthorized"),
                    @ApiResponse(responseCode = "403",description = "Forbidden"),
                    @ApiResponse(responseCode = "404",description = " Not found")


            }
    )

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity<?> removeAd(@PathVariable int id){
        adsService.removeAdsById((long) id);
        return ResponseEntity.ok().build();
    }
    @Operation(
            tags = "Объявления",
            summary = "Обновление информации об объявлении",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateOrUpdateAd.class)
                    )
            ),

            responses = {
                    @ApiResponse(responseCode = "200",description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(responseCode = "403",description = "Forbidden",
                            content = {@Content()}),
                    @ApiResponse(responseCode = "401",description = "Unauthorized",
                            content = {@Content()}),
                    @ApiResponse(responseCode = "404",description = "Not found",
                            content = {@Content()})

            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity<Ad> updateAds(@PathVariable int id, @org.springframework.web.bind.annotation.RequestBody  CreateOrUpdateAd createOrUpdateAd){

        Ad ad = adsService.updateAds((long) id,createOrUpdateAd);
        if(ad!=null){
            return ResponseEntity.ok(ad);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @Operation(
            tags = "Объявления",
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200",description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class ))),
                    @ApiResponse(responseCode = "401",description = "Unauthorized",
                            content = {@Content()})
            }
    )
    @GetMapping(value = "/me")
    public ResponseEntity<Ads>getAdsMe(Authentication authentication){
        return ResponseEntity.ok(adsService.getAdsMe(authentication));

    }
    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(responseCode = "200",description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
                    @ApiResponse(responseCode = "403",description = "Forbidden",
                            content = {@Content()}),
                    @ApiResponse(responseCode = "401",description = " Unauthorized",
                            content = {@Content()}),
                    @ApiResponse(responseCode = "404",description = " Not found",
                            content = {@Content()})
            }
    )
    @PatchMapping(value = "{id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity<Void> updateImage(@PathVariable int id, @RequestPart MultipartFile image,Authentication authentication){
        adsService.updateAdsImage(id,image,authentication);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity<byte[]> getAdsImage(@PathVariable("id") long id) {
        log.info("updateAdsImage", "patch", "/id");
        return ResponseEntity.ok(imageService.getImageById(id).getData());
    }
}
