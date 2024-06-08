package com.redhat.filestore.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class FileClientTest {
    FileClient fileClient = new FileClient();


    @Test
    void testStoreFile() {
        fileClient.storeFile(List.of("String"), "fileContent");
    }

    @Test
    void testGetFiles() {
        String result = fileClient.getFiles();
        File[] expected = {new File("file3.txt"), new File("file3.txt")};
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUpdateFile() {
        fileClient.updateFile("file3.txt", "fileContent");
    }

    @Test
    void testDeleteFile() {
        fileClient.deleteFile("fileName");
    }

    @Test
    void testWordCount() {
        Integer result = fileClient.wordCount();
        Assertions.assertEquals(Integer.valueOf(6), result);
    }

    @Test
    void testFrequentWords() {
        List<String> result = fileClient.frequentWords(0, "order");
        Assertions.assertEquals(List.of("the","filecontent","this","is","content.","updated"), result);
    }
}