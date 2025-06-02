package com.example.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessengerFileModeTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldProcessTemplateAndWriteToFile() throws IOException {
        Path inputFile = tempDir.resolve("input.txt");
        Path outputFile = tempDir.resolve("output.txt");

        List<String> inputLines = List.of("Welcome, #{user}!", "user=Yurii", "extra=ignored");
        Files.write(inputFile, inputLines);

        TemplateEngine templateEngine = new TemplateEngine();
        Messenger messenger = new Messenger(templateEngine);

        messenger.runFileMode(inputFile.toString(), outputFile.toString());

        String output = Files.readString(outputFile);
        assertEquals("Welcome, Yurii!", output);
    }

    @Test
    void shouldWriteErrorToFileWhenMissingPlaceholder() throws IOException {
        Path inputFile = tempDir.resolve("input.txt");
        Path outputFile = tempDir.resolve("output.txt");

        Files.write(inputFile, List.of("Hi #{name}!", "notUsed=value"));

        TemplateEngine templateEngine = new TemplateEngine();
        Messenger messenger = new Messenger(templateEngine);

        messenger.runFileMode(inputFile.toString(), outputFile.toString());

        String output = Files.readString(outputFile);
        assertEquals("Error: missing value for name", output.trim());
    }
}
