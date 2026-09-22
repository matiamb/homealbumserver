/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homealbum.homealbumserver;

import com.homealbum.homealbumserver.model.MediaFile;
import com.homealbum.homealbumserver.repository.MediaFileRepository;
import com.homealbum.homealbumserver.service.MediaFileService;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author matiambrosi
 */
@ExtendWith(MockitoExtension.class)
public class MediaFileServiceTest {
    @Mock
    private MediaFileRepository fakeMediaFileRepo;
    @InjectMocks
    private MediaFileService fakeMediaFileService;
    MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.jpg",
                "image/jpeg",
                "test content".getBytes()
        );
    @TempDir
    Path tempDir;
    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(fakeMediaFileService, "basePath", tempDir.toString());
    }
    
    @Test
    void checkIfPhotoExists_fileExistsInServer_returnsTrue(){
        when(fakeMediaFileRepo.existsByFileHash("testHash"))
                .thenReturn(true);
        boolean result = fakeMediaFileService.checkIfPhotoExists("testHash");
        assertTrue(result);
        verify(fakeMediaFileRepo).existsByFileHash("testHash");
    }
    @Test
    void checkIfPhotoExists_fileDoesNotExists_returnsFalse(){
        when(fakeMediaFileRepo.existsByFileHash("testHash"))
                .thenReturn(false);
        boolean result = fakeMediaFileService.checkIfPhotoExists("testHash");
        assertFalse(result);
        verify(fakeMediaFileRepo).existsByFileHash("testHash");
    }
    @Test
    void saveFile_fileExistsInServer_exitsMethod() throws Exception{
        when(fakeMediaFileRepo.existsByFileHash("testHash"))
                .thenReturn(true);
        fakeMediaFileService.saveFile(file, "testHash", "test");
        verify(fakeMediaFileRepo, never()).save(any(MediaFile.class));
    }
    @Test
    void saveFile_incorrectFileType_throwsExpectedException() throws Exception{
            MockMultipartFile wrongFile = new MockMultipartFile(
                "file",
                "photo.jpg",
                "/",
                "test content".getBytes()
            );            
            IOException exception = assertThrows(IOException.class,
                    ()-> fakeMediaFileService.saveFile(wrongFile, "testHash", "test"));
            assertEquals("File not supported", exception.getMessage());
            verify(fakeMediaFileRepo, never()).save(any(MediaFile.class));
    }
    @Test
    void saveFile_invalidFolderPath_throwsExpectedException(){
        assertThrows(InvalidPathException.class,
                ()-> fakeMediaFileService.saveFile(file, "testHash", "../../test")
        );
        verify(fakeMediaFileRepo, never()).save(any(MediaFile.class));
    }
    @Test
    void saveFile_fileDoesNotExistsAndIsOkForSaving_fileMetaDataSavedInDb() throws Exception{
        when(fakeMediaFileRepo.existsByFileHash("testHash"))
            .thenReturn(false);
        fakeMediaFileService.saveFile(file, "testHash", "test");
        verify(fakeMediaFileRepo).save(any(MediaFile.class));
    }
}
