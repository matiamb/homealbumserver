/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author matiambrosi
 */
@RestController
public class HomeAlbumController {
    @GetMapping("exists/{fileHash}")
    public ResponseEntity checkIfPhotoExists(@PathVariable String fileHash){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
