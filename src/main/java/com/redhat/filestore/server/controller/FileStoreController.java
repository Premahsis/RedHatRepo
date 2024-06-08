package com.redhat.filestore.server.controller;

import com.redhat.filestore.server.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class FileStoreController {

    private static final String UPLOAD_FOLDER = "uploads/";

    @Autowired
    FileStoreService fileStoreService;

    @PostMapping("/store/add")
    public ResponseEntity<String> addFile(@RequestParam("file") MultipartFile[] multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                return new ResponseEntity<>("No file selected", HttpStatus.BAD_REQUEST);
            }
            String fileName = multipartFile.getOriginalFilename();
            File destFile = new File(UPLOAD_FOLDER + fileName);
            if (destFile.exists()) {
                return new ResponseEntity<>("File already exists", HttpStatus.CONFLICT);
            }

            try {
                Files.copy(multipartFile.getInputStream(), Paths.get(UPLOAD_FOLDER + fileName));
            } catch (IOException e) {
                return new ResponseEntity<>("Failed to store file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("File added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        return new ResponseEntity<>(fileStoreService.retrieveListOfFiles(), HttpStatus.OK);
    }

    @DeleteMapping("/files/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        File file = new File(UPLOAD_FOLDER + "/" + fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return "File " + fileName + " deleted successfully.";
            } else {
                return "Failed to delete " + fileName + ".";
            }
        } else {
            return "File " + fileName + " not found.";
        }
    }

    @PostMapping("/files/{fileName}")
    public String updateFile(@PathVariable String fileName, @RequestBody byte[] fileContent) {
        File file = new File(UPLOAD_FOLDER + "/" + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fileContent = "Update new content".getBytes();
            fos.write(fileContent);
            return "File " + fileName + " updated successfully.";
        } catch (IOException e) {
            return "Failed to update " + fileName + ".";
        }
    }

    @GetMapping("/wc")
    public ResponseEntity<Integer> wordCount() {
        return new ResponseEntity<>(fileStoreService.calculateWords(), HttpStatus.OK);
    }

    @GetMapping("/freq-words")
    public ResponseEntity<List<String>> frequentWords(@RequestParam(defaultValue = "10") int limit,
                                                      @RequestParam(defaultValue = "asc") String order) {

        return new ResponseEntity<>(fileStoreService.retriveFrequentWords(limit, order), HttpStatus.OK);

    }
}
