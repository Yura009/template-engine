package com.example.messenger.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

public class MessengerFileModeTest {

    @TempDir
    Path tempDir;

    Messenger messenger;
    Path inputFile;
    Path outputFile;

    @BeforeEach
    void setup() {
        messenger = new Messenger(new TemplateEngine());
        inputFile = tempDir.resolve("input.txt");
        outputFile = tempDir.resolve("output.txt");
    }

    @Test
    @SneakyThrows
    void shouldProcessTemplateAndWriteToFile() {
        List<String> inputLines = List.of("Welcome, #{user}!", "user=Yurii", "extra=ignored");
        Files.write(inputFile, inputLines);

        messenger.runFileMode(inputFile.toString(), outputFile.toString());

        String output = Files.readString(outputFile);
        assertEquals("Welcome, Yurii!", output);
    }

    @Test
    @SneakyThrows
    void shouldWriteErrorToFileWhenMissingPlaceholder() {
        Files.write(inputFile, List.of("Hi #{name}!", "notUsed=value"));

        messenger.runFileMode(inputFile.toString(), outputFile.toString());

        String output = Files.readString(outputFile);
        assertEquals("Error: missing value for name", output.trim());
    }

    @Test
    @SneakyThrows
    void shouldThrowRuntimeExceptionWhenWritingFails() {
        try (MockedStatic<Files> files = mockStatic(Files.class)) {
            files.when(() -> Files.readAllLines(Path.of("input.txt")))
                    .thenReturn(List.of("Hello, {{name}}", "name=John"));

            files.when(() -> Files.writeString(eq(Path.of("output.txt")), anyString()))
                    .thenThrow(new IOException("Disk error"));

            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                    messenger.runFileMode("input.txt", "output.txt")
            );

            assertTrue(ex.getMessage().contains("File error"));
        }
    }

}
