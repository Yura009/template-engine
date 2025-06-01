package com.example.messenger.service;

import com.example.messenger.exception.MissingPlaceholderValueException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemplateEngineTest {

    @Test
    public void shouldReplacePlaceholderWithValue() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Hello, #{name}!";
        Map<String, String> values = Map.of("name", "Yurii");

        String result = templateEngine.render(template, values);

        assertEquals("Hello, Yurii!", result);
    }

    @Test
    public void shouldThrowExceptionIfPlaceholderValueIsMissing() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Hello, #{name}!";

        Map<String, String> values = Map.of();

        Exception exception = assertThrows(MissingPlaceholderValueException.class, () ->
                templateEngine.render(template, values));

        assertEquals("Missing value for placeholder: name", exception.getMessage());
    }

    @Test
    public void shouldIgnoreExtraVariablesNotInTemplate() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Welcome, #{user}!";
        Map<String, String> values = Map.of(
                "user", "Yurii",
                "unused", "something"
        );

        String result = templateEngine.render(template, values);

        assertEquals("Welcome, Yurii!", result);
    }
}
