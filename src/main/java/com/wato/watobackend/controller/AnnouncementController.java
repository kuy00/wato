package com.wato.watobackend.controller;

import com.wato.watobackend.model.Announcement;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/announcement")
@RestController
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public ApiResponse getAnnouncements() {
        List<Announcement> announcements = announcementService.getAnnouncements();

        return ApiResponse.builder().data(announcements).build();
    }

    @GetMapping("/{id}")
    public ApiResponse getAnnouncement(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncement(id);

        return ApiResponse.builder().data(announcement).build();
    }
}
