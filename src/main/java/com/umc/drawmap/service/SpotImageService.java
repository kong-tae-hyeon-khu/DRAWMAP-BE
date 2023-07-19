package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.SpotImage;
import com.umc.drawmap.dto.SpotImageReqDto;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.SpotImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SpotImageService {

    private final SpotImageRepository spotImageRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public SpotImage create(MultipartFile file, SpotImageReqDto.CreateSpotImageDto request) throws IOException{
        Challenge challenge = challengeRepository.findById(request.getChallengeId()).get();
        SpotImage spotImage = SpotImage.builder()
                .spotTitle(request.getTitle())
                .spotArea(request.getArea())
                .spotContent(request.getContent())
                .spotImage(FileService.singleFileUpload(file))
                .challenge(challenge)
                .build();
        return spotImageRepository.save(spotImage);
    }

    @Transactional
    public SpotImage update(Long courseId, Long spotId, MultipartFile file, SpotImageReqDto.UpdateSpotImageDto request) throws IOException{
        Challenge challenge = challengeRepository.findById(courseId).get();
        SpotImage spotImage = spotImageRepository.findByIdAndChallenge(spotId, challenge);
        spotImage.update(request.getTitle(), request.getArea(), request.getContent(), FileService.singleFileUpload(file));
        return spotImage;
    }


}
