/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver.repository;

import com.homealbum.homealbumserver.model.MediaFile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author matiambrosi
 */
@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long>{
    boolean existsByFileHash(String fileHash);
    Optional<MediaFile> findByFileHash(String fileHash);
}
