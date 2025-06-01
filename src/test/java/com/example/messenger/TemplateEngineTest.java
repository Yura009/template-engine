package com.example.messenger;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateEngineTest {

    @Test
    public void shouldReplacePlaceholderWithValue() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Hello, #{name}!";
        Map<String, String> values = Map.of("name", "Yurii");

        String result = templateEngine.render(template, values);

        assertEquals("Hello, Yurii!", result);
    }
}
