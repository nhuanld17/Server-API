package com.example.SERVER.service.canditate;

import com.example.SERVER.domain.entity.candidate.LinkSocial;
import com.example.SERVER.repository.candidate.LinkSocialRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LinkSocialService {
    private final LinkSocialRepository linkSocialRepository;

    public LinkSocialService(LinkSocialRepository linkSocialRepository) {
        this.linkSocialRepository = linkSocialRepository;
    }

    @Transactional
    public void saveLinkSocial(LinkSocial linkSocial) {
        linkSocialRepository.save(linkSocial);
    }
}
