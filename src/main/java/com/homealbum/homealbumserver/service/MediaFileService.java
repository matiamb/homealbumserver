/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver.service;

import com.homealbum.homealbumserver.model.MediaFile;
import com.homealbum.homealbumserver.repository.MediaFileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author matiambrosi
 */
@Service
@RequiredArgsConstructor
public class MediaFileService implements IMediaFileService{
    
    private final MediaFileRepository mediaFileRepository;
    @Value("${storage.base-path}")
    private String basePath;
    
    @Override
    public Boolean checkIfPhotoExists(String fileHash) {
        return mediaFileRepository.existsByFileHash(fileHash);
    }

    @Override
    public void saveFile(MultipartFile file, String fileHash, String folderName) throws IOException {
        Path folderPath = Paths.get(basePath, folderName);
        if(!Files.exists(folderPath)){
            Files.createDirectories(folderPath);
        }
        String fileName = file.getOriginalFilename();
        
        Path filePath = folderPath.resolve(fileName);
        String fileType = file.getContentType();
        
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        MediaFile mediaFile = MediaFile.builder()
                .fileHash(fileHash)
                .fileName(fileName)
                .folderName(folderName)
                .mimeType(fileType)
                .build();
        mediaFileRepository.save(mediaFile);
    }
}
