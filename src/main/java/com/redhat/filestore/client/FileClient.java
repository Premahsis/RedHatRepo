package com.redhat.filestore.client;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileClient {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String UPLOAD_FOLDER = "uploads/";

    private static final String STORE_DATA = "store ls";
    private static final String REMOVE_FILE = "store rm";
    private static final String UPDATE_FILE = "store update";
    private static final String WORD_COUNT = "store wc";
    private static final String FREQUENT_WORDS = "store freq-words";

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        String s = in.nextLine();


        FileClient client = new FileClient();

        /*File file3 = new File(UPLOAD_FOLDER + "file3.txt");
        File file4 = new File(UPLOAD_FOLDER + "file4.txt");
        String fileContent = "This is an example file content.";
        client.addFiles(file3, file4);*/

        // Get a file
        if (s.equalsIgnoreCase(STORE_DATA)) {
            String retrievedContent = client.getFiles();
            System.out.println("store ls: " + retrievedContent);
        }
        //Delete File from Store
        if (s.equalsIgnoreCase(REMOVE_FILE)) {
            // Delete the file
            String fileName = in.nextLine();
            System.out.println(REMOVE_FILE + " " + fileName);
            client.deleteFile(fileName);
        }
        //Update a file content
        if (s.equalsIgnoreCase(UPDATE_FILE)) {
            String fileName = in.nextLine();
            System.out.println(UPDATE_FILE + " " + fileName);
            String updatedContent = "This is the updated content.";
            client.updateFile(fileName, updatedContent);
        }

        //Word Count
        if (s.equalsIgnoreCase(WORD_COUNT)) {
            System.out.println(WORD_COUNT + " " + client.wordCount());
        }

        //Frequent Words
        if (s.equalsIgnoreCase(FREQUENT_WORDS)) {
            client.frequentWords(10, "asc");
            System.out.println(FREQUENT_WORDS + client.frequentWords(10, "asc"));
        }
    }

    public void addFiles(File... files) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (File file : files) {
            url = SERVER_URL + "/store/add" + file;
            body.add("files", new FileSystemResource(file));//
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<File> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, File.class);

        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        /*RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/store/add/" + files;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> requestEntity = new HttpEntity<>(fileContent, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println(response.getBody());*/
    }


    public void storeFile(List<String> fileNames, String fileContent) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/store/add" + fileNames;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> requestEntity = new HttpEntity<>(fileContent, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println(response.getBody());
    }

    public String getFiles() {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/files";
        //String url = "http://localhost:8080/files";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public void updateFile(String fileName, String fileContent) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/files/" + fileName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> requestEntity = new HttpEntity<>(fileContent, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println(response.getBody());
    }

    public void deleteFile(String fileName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/files/" + fileName;
        restTemplate.delete(url);
        System.out.println("File deleted successfully");
    }

    public Integer wordCount() {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/wc";
        ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
        return response.getBody();
    }

    public List<String> frequentWords(int limit, String order) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/freq-words";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return Collections.singletonList(response.getBody());
    }

}
