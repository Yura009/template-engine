package com.example.messenger;

import com.example.messenger.service.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;

public class MessengerApp {
    private final TemplateEngine templateEngine;

    public MessengerApp(TemplateEngine templateEngine) {
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
}
