package com.wato.watobackend.service;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Announcement;
import com.wato.watobackend.repository.AnnouncementRepository;
import com.wato.watobackend.request.AnnouncementRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public List<Announcement> getAnnouncements() {
        return announcementRepository.findAll();
    }

    public Announcement getAnnouncement(Long id) {
        Optional<Announcement> optAnnouncement = announcementRepository.findById(id);
        if (optAnnouncement.isEmpty()) throw new ApiException(Error.NOT_EXIST_ANNOUNCEMENT);

        return optAnnouncement.get();
    }

    public Announcement createAnnouncement(AnnouncementRequest request) {
        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return announcementRepository.save(announcement);
    }
}
