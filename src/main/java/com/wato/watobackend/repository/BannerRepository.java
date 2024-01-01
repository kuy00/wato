package com.wato.watobackend.repository;

import com.wato.watobackend.dto.BannerDto;
import com.wato.watobackend.model.Banner;
import com.wato.watobackend.model.constant.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("select new com.wato.watobackend.dto.BannerDto(b.id, b.type, b.title, b.imageUrl, b.linkUrl, b.createDate, b.updateDate) " +
            "FROM Banner b where b.type =:type order by b.id desc")
    List<BannerDto> findAllByType(BannerType type);

    @Query("select new com.wato.watobackend.dto.BannerDto(b.id, b.type, b.title, b.imageUrl, b.linkUrl, b.createDate, b.updateDate) " +
            "FROM Banner b where b.id =:id")
    Optional<BannerDto> findDtoById(Long id);
}
