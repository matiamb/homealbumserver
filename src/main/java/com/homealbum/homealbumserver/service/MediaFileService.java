/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver.service;

import com.homealbum.homealbumserver.model.MediaFile;
import com.homealbum.homealbumserver.repository.MediaFileRepository;
import dto.DiskSpaceResponse;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    public void saveFile(MultipartFile file, String fileHash, String folderName) throws Exception{
        Path folderPath = Paths.get(basePath, folderName).toAbsolutePath().normalize();
        String fileName = file.getOriginalFilename();
        Path filePath = folderPath.resolve(fileName).normalize();
        String fileType = file.getContentType();        
        if (checkIfPhotoExists(fileHash)){
            throw new Exception("File already exists");
        } 
        if(!validateDiskSpace(file)) {
            throw new IOException("Disk space low");
        }
        if (!(fileType.startsWith("image/") || fileType.startsWith("video/"))){
            throw new IOException("File not supported");
        }
        if(!filePath.startsWith(basePath)){
            throw new InvalidPathException( filePath.toString(), "Invalid folder path");
        }
        else {
                if(!Files.exists(folderPath)){
                    Files.createDirectories(folderPath);
                }
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

    @Override
    public void deleteMediaFile(String fileHash) throws IOException {
        if(mediaFileRepository.existsByFileHash(fileHash)){
            Optional<MediaFile> media = mediaFileRepository.findByFileHash(fileHash);
            Path folderPath = Paths.get(basePath, media.get().getFolderName()).toAbsolutePath().normalize();
            Path filePath = folderPath.resolve(media.get().getFileName()).normalize();
            Files.deleteIfExists(filePath);
            mediaFileRepository.delete(media.get());       
        }
    }

    @Override
    public DiskSpaceResponse checkFileSystem() throws IOException {
        
        try{
            Path folderPath = Paths.get(basePath).toAbsolutePath().normalize();
            FileStore fileStore = Files.getFileStore(folderPath);

            long totalSpace = fileStore.getTotalSpace();
            long freeSpace = fileStore.getUsableSpace();
            long usedSpace = totalSpace - freeSpace;
            DiskSpaceResponse diskSpace = new DiskSpaceResponse(totalSpace, freeSpace, usedSpace);
            return diskSpace;
        } catch (IOException e){
            throw new IOException();
        }
    }
    private boolean validateDiskSpace(MultipartFile file) throws IOException{
        DiskSpaceResponse diskSpace = checkFileSystem();
        long fileSize = file.getSize();
        if (fileSize >= diskSpace.availableSpaceBytes()){
            return false;
        } else {
            return true;
        }
    }
}
