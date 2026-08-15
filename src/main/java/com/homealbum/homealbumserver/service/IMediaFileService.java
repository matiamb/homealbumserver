/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author matiambrosi
 */
public interface IMediaFileService {
    Boolean checkIfPhotoExists(String fileHash);
    void saveFile(MultipartFile file, String hash, String folderName) throws Exception;
    void deleteMediaFile(String fileHash) throws IOException;
}
