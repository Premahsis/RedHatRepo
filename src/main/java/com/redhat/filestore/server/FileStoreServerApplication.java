package com.redhat.filestore.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class FileStoreServerApplication {

    private static final String UPLOAD_FOLDER = "uploads/";

    public static void main(String[] args) {
        SpringApplication.run(FileStoreServerApplication.class, args);
    }

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
    public List<String> listFiles() {
        List<String> fileList = new ArrayList<>();
        File folder = new File(UPLOAD_FOLDER);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                }
            }
        }
        return fileList;
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
    public int wordCount() {
        int wordCount = 0;
        File folder = new File(UPLOAD_FOLDER);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try (Scanner scanner = new Scanner(file)) {
                        while (scanner.hasNext()) {
                            scanner.next();
                            wordCount++;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return wordCount;
    }

    @GetMapping("/freq-words")
    public List<String> frequentWords(@RequestParam(defaultValue = "10") int limit,
                                      @RequestParam(defaultValue = "asc") String order) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        File folder = new File(UPLOAD_FOLDER);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try (Scanner scanner = new Scanner(file)) {
                        while (scanner.hasNext()) {
                            String word = scanner.next().toLowerCase();
                            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Comparator<Map.Entry<String, Integer>> comparator = (entry1, entry2) -> {
            if (order.equals("asc")) {
                return entry1.getValue().compareTo(entry2.getValue());
            } else {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        };

        return wordFrequency.entrySet().stream()
                .sorted(comparator)
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
