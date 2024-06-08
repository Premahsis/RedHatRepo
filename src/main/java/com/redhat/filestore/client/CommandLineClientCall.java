package com.redhat.filestore.client;

import com.redhat.filestore.constants.CommandConstants;
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

public class CommandLineClientCall {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String STORE_FOLDER = "stores/";

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        CommandLineClientCall client = new CommandLineClientCall();
        //Add Files to Store, considered two files for now
        if (s.equalsIgnoreCase(CommandConstants.ADD_FILES)) {
            String pstrFile1 = in.next();
            String pstrFile2 = in.next();
            File file1 = new File(STORE_FOLDER + pstrFile1);
            File file2 = new File(STORE_FOLDER + pstrFile2);
            addFile(file1, file2);
            in.close();
        }

        // Get a file
        if (s.equalsIgnoreCase(CommandConstants.STORE_DATA)) {
            String retrievedContent = client.getFiles();
            System.out.println(CommandConstants.STORE_DATA + " " + retrievedContent);
            in.close();
        }
        //Delete File from Store
        if (s.equalsIgnoreCase(CommandConstants.REMOVE_FILE)) {
            // Delete the file
            String fileName = in.nextLine();
            System.out.println(CommandConstants.REMOVE_FILE + " " + fileName);
            client.deleteFile(fileName);
            in.close();
        }
        //Update a file content
        if (s.equalsIgnoreCase(CommandConstants.UPDATE_FILE)) {
            String fileName = in.nextLine();
            System.out.println(CommandConstants.UPDATE_FILE + " " + fileName);
            String updatedContent = "This is the updated content.";
            client.updateFile(fileName, updatedContent);
            in.close();
        }

        //Word Count
        if (s.equalsIgnoreCase(CommandConstants.WORD_COUNT)) {
            System.out.println(CommandConstants.WORD_COUNT + " " + client.wordCount());
        }

        //Frequent Words
        if (s.equalsIgnoreCase(CommandConstants.FREQUENT_WORDS)) {
            client.frequentWords(10, "asc");
            System.out.print(CommandConstants.FREQUENT_WORDS + client.frequentWords(10, "asc"));
        }
    }

    public static void addFile(File... files) {
        RestTemplate restTemplate = new RestTemplate();
        // URL of the endpoint
        String url = "http://localhost:8080/store/add";
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // Create MultiValueMap to store the file(s)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Add MultipartFile(s) to the MultiValueMap
        try {
            for (File file : files) {
                body.add("file", new FileSystemResource(STORE_FOLDER + file.getName().toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create the request entity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        // Send the request and get the response entity
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        // Print the response
        System.out.println("Response status code: " + responseEntity.getStatusCode());
        System.out.println("Response body: " + responseEntity.getBody());
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
