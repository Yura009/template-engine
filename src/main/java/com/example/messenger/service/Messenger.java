package com.example.messenger.service;

import com.example.messenger.exception.MissingPlaceholderValueException;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Messenger {
    private final TemplateEngine templateEngine;

    public Messenger(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void runConsoleMode(Scanner scanner, PrintStream out, PrintStream err) {
        out.println("Enter template: ");
        String template = scanner.nextLine();

        Map<String, String> values = new HashMap<>();
        out.println("Enter variables in format key=value. Type 'end' to finish:");
        while (true) {
            String input = scanner.nextLine();
            if ("end".equalsIgnoreCase(input)) {
                break;
            }

            String[] parts = input.split("=");
            if (parts.length == 2) {
                values.put(parts[0], parts[1]);
            } else {
                err.println("Invalid input, should be key=value");
            }
        }

        try {
            String result = templateEngine.render(template, values);
            out.println("Result:\n" + result);
        } catch (MissingPlaceholderValueException ex) {
            err.println("Missing value for placeholder: " + ex.getMessage());
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

        } catch (MissingPlaceholderValueException ex) {
            try {
                Files.writeString(Path.of(outputPath), "Error: missing value for " + ex.getMessage());
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        } catch (IOException ex) {
            throw new RuntimeException("File error: " + ex.getMessage(), ex);
        }
    }
}
