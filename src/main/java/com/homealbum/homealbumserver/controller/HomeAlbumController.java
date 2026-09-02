/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver.controller;

import com.homealbum.homealbumserver.service.MediaFileService;
import dto.DiskSpaceResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author matiambrosi
 */
@RestController
@RequestMapping("api/v1/media")
@RequiredArgsConstructor
public class HomeAlbumController {
    
    private final MediaFileService mediaFileService;
    
    @GetMapping("/exists")
    public ResponseEntity<String> checkIfPhotoExists(@RequestParam("fileHash") String fileHash){
        boolean response = mediaFileService.checkIfPhotoExists(fileHash);
        if (response){
            return ResponseEntity.status(HttpStatus.OK).body("File is present in the server");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found in the server");
        }
    }
    @GetMapping("/ping")
    public ResponseEntity<String> checkServerConnection(){
        return ResponseEntity.status(HttpStatus.OK).body("Connection OK");
    }
    @PostMapping("/upload")
    public ResponseEntity<String> saveMediaFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileHash") String fileHash,
            @RequestParam("folderName") String folderName
    ){
        try{
            mediaFileService.saveFile(file, fileHash, folderName);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } 
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMediaFile(
    @RequestParam("fileHash") String fileHash
            ){
        try{
            mediaFileService.deleteMediaFile(fileHash);
            return ResponseEntity.status(HttpStatus.CREATED).body("File deleted successfully");
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File could not be deleted");
        }
    }
    @GetMapping("/diskspace")
    public DiskSpaceResponse checkFileSystem() throws IOException {
        return mediaFileService.checkFileSystem();
    }
    @PostMapping("/multipleUpload")
    public ResponseEntity<String> saveMultipleFiles(
            @RequestParam("file") List<MultipartFile> fileList,
            @RequestParam("fileHash") List<String> hashList,
            @RequestParam("folderName") String folderName
            
    ){
        if(fileList.size() != hashList.size()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is a discrepancy between files and hashes");
        }
        try{
            for(int i = 0; i < fileList.size(); i++){
                MultipartFile file = fileList.get(i);
                String hash = hashList.get(i);
                mediaFileService.saveFile(file, hash, folderName);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully");
        } catch (Exception e){           
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
