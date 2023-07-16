package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    public Boolean findScrapByUserAndChallenge(User user, Challenge challenge){
        return scrapRepository.existsScrapByUserAndChallenge(user, challenge);
    }
}
