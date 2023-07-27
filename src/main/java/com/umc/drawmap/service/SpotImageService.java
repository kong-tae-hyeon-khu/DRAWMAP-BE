package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.SpotImage;
import com.umc.drawmap.dto.SpotImageReqDto;
import com.umc.drawmap.dto.SpotImageResDto;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.SpotImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotImageService {

    private final SpotImageRepository spotImageRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public SpotImage create(MultipartFile file, SpotImageReqDto.CreateSpotImageDto request) throws IOException{
        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new NotFoundException("도전코스를 찾을 수 없습니다."));
        SpotImage spotImage = SpotImage.builder()
                .spotTitle(request.getTitle())
                .sido(request.getSido())
                .sgg(request.getSgg())
                .spotContent(request.getContent())
                .spotImage(FileService.singleFileUpload(file))
                .challenge(challenge)
                .build();
        return spotImageRepository.save(spotImage);
    }

    @Transactional
    public SpotImage update(Long courseId, Long spotId, MultipartFile file, SpotImageReqDto.UpdateSpotImageDto request) throws IOException{
        Challenge challenge = challengeRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("도전코스를 찾을 수 없습니다."));;
        SpotImage spotImage = spotImageRepository.findByIdAndChallenge(spotId, challenge);
        spotImage.update(request.getTitle(), request.getSido(), request.getSgg(), request.getContent(), FileService.singleFileUpload(file));
        return spotImage;
    }

    public List<SpotImageResDto.SpotImageDto> findAllByCourse(Long courseId){
        Challenge challenge = challengeRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("도전코스를 찾을 수 없습니다."));
        List<SpotImage> spotImageList = spotImageRepository.findAllByChallenge(challenge);
        List<SpotImageResDto.SpotImageDto> list = new ArrayList<>();
        for(SpotImage s:spotImageList){
            SpotImageResDto.SpotImageDto image = SpotImageResDto.SpotImageDto.builder()
                    .spotImageId(s.getId())
                    .title(s.getSpotTitle())
                    .content(s.getSpotContent())
                    .sido(s.getSido())
                    .sgg(s.getSgg())
                    .image(s.getSpotImage())
                    .createdDate(s.getCreatedAt())
                    .build();
            list.add(image);
        }
        return list;
    }

    @Transactional
    public void delete(Long courseId, Long spotId){
        Challenge challenge = challengeRepository.findById(courseId)
                .orElseThrow(()-> new NotFoundException("도전코스를 찾을 수 없습니다."));
        spotImageRepository.deleteByIdAndChallenge(spotId, challenge);

    }


}
