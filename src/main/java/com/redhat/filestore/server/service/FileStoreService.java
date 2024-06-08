package com.redhat.filestore.server.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileStoreService {

    private static final String UPLOAD_FOLDER = "uploads/";

    public static List<String> retrieveListOfFiles() {
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

    public static int calculateWords() {
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

    public static List<String> retriveFrequentWords(int limit, String order) {
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
