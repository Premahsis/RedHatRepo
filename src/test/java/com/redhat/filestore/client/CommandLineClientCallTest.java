package com.redhat.filestore.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandLineClientCallTest {
    CommandLineClientCall commandLineClientCall = new CommandLineClientCall();
    private static final String UPLOAD_FOLDER = "test/uploads/";

    @Test
    void testGetFiles() throws IOException {
        File tempDir = Files.createTempDirectory(UPLOAD_FOLDER).toFile();
        tempDir.deleteOnExit();

        // Create some sample files in the temporary directory
        File file1 = new File(tempDir, "file1.txt");
        File file2 = new File(tempDir, "file2.txt");
        File file3 = new File(tempDir, "file3.txt");
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        // Set the UPLOAD_FOLDER to the temporary directory
        String UPLOAD_FOLDER = tempDir.getAbsolutePath();

        // Call the method under test
        List<String> result = Collections.singletonList(commandLineClientCall.getFiles());

        // Verify the result
        assertEquals(List.of("file1.txt", "file2.txt", "file3.txt"), result);
    }

    @Test
    void testUpdateFile() {
        commandLineClientCall.updateFile("file3.txt", "fileContent");
    }

    @Test
    void testDeleteFile() {
        commandLineClientCall.deleteFile("fileName");
    }

    @Test
    void testWordCount() {
        Integer result = commandLineClientCall.wordCount();
        assertEquals(Integer.valueOf(14), result);
    }

    @Test
    void testFrequentWords() {
        List<String> result = commandLineClientCall.frequentWords(0, "order");
        assertEquals(List.of("the", "this", "is", "hello", "pradhan.", "updated", "premashis", "new", "update", "content"), result);
    }
}