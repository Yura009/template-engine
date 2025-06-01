package com.example.messenger.service;

import com.example.messenger.exception.MissingPlaceholderValueException;
import com.example.messenger.service.TemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Messenger {
    private final TemplateEngine templateEngine;

    public Messenger(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void runConsoleMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter template: ");
        String template = scanner.nextLine();

        Map<String, String> values = new HashMap<>();
        System.out.println("Enter variables in format key=value. Type 'end' to finish:");
        while (true) {
            String input = scanner.nextLine();
            if ("end".equalsIgnoreCase(input)) {
                break;
            }

            String[] parts = input.split("=", 2);
            if (parts.length == 2) {
                values.put(parts[0], parts[1]);
            } else {
                System.out.println("Invalid input, should be key=value");
            }
        }

        try {
            String result = templateEngine.render(template, values);
            System.out.println("Result:\n" + result);
        } catch (MissingFormatArgumentException ex) {
            System.err.println("Missing value for placeholder: " + ex.getMessage());
        }
    }

    public void runFileMode(String inputPath, String outputPath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(inputPath));
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("Input file is empty");
            }

            String template = lines.get(0);
            Map<String, String> values = new HashMap<>();

            for (String line : lines) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    values.put(parts[0], parts[1]);
                }
            }

            String result = templateEngine.render(template, values);

            Files.writeString(Path.of(outputPath), result);

        } catch (MissingPlaceholderValueException e) {
            try {
                Files.writeString(Path.of(outputPath), "Error: missing value for " + e.getMessage());
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        } catch (IOException e) {
            throw new RuntimeException("File error: " + e.getMessage(), e);
        }
    }
}
