package com.example.messenger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessengerConsoleModeTest {

    @Mock
    Scanner scanner;
    ByteArrayOutputStream outContent;
    ByteArrayOutputStream errContent;
    Messenger messenger;

    @BeforeEach
    void setup() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        messenger = new Messenger(new TemplateEngine());
    }

    @Test
    void shouldProcessTemplateAndWriteToConsole() {
        when(scanner.nextLine()).thenReturn(
                "Hello, #{user}!",
                "user=Yurii",
                "end"
        );
        messenger.runConsoleMode(scanner, new PrintStream(outContent), new PrintStream(errContent));

        String output = outContent.toString().trim();
        assertTrue(output.contains("Hello, Yurii!"), "Output should contain rendered result");

        String error = errContent.toString().trim();
        assertTrue(error.isEmpty(), "Error output should be empty");
    }

    @Test
    void shouldWriteErrorToConsoleWhenMissingPlaceholder() {
        when(scanner.nextLine()).thenReturn(
                "Hello, #{user}!",
                "end"
        );
        messenger.runConsoleMode(scanner, new PrintStream(outContent), new PrintStream(errContent));

        String output = outContent.toString();
        assertFalse(output.contains("Result:"));

        String error = errContent.toString();
        assertTrue(error.contains("Missing value for placeholder: user"));
    }
}
