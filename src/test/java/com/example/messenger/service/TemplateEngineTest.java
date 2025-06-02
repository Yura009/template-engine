package com.example.messenger.service;

import com.example.messenger.exception.MissingPlaceholderValueException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemplateEngineTest {

    @Test
    void shouldReplacePlaceholderWithValue() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Hello, #{name}!";
        Map<String, String> values = Map.of("name", "Yurii");

        String result = templateEngine.render(template, values);

        assertEquals("Hello, Yurii!", result);
    }

    @Test
    void shouldThrowExceptionIfPlaceholderValueIsMissing() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Hello, #{name}!";

        Map<String, String> values = Map.of();

        Exception exception = assertThrows(MissingPlaceholderValueException.class, () ->
                templateEngine.render(template, values));

        assertEquals("name", exception.getMessage());
    }

    @Test
    void shouldIgnoreExtraVariablesNotInTemplate() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Welcome, #{user}!";
        Map<String, String> values = Map.of(
                "user", "Yurii",
                "unused", "something"
        );

        String result = templateEngine.render(template, values);

        assertEquals("Welcome, Yurii!", result);
    }

    @Test
    void shouldSupportValuesWithPlaceholderSyntaxInside() {
        TemplateEngine templateEngine = new TemplateEngine();
        String template = "Some text: #{value}!";
        Map<String, String> values = Map.of("value", "#{tag}");

        String result = templateEngine.render(template, values);

        assertEquals("Some text: #{tag}!", result);
    }

    @Test
    void shouldSupportLatin1CharactersInTemplateAndValues() {
        TemplateEngine engine = new TemplateEngine();
        String template = "Hola señor #{name}, ¿cómo está? Su símbolo es #{symbol}";
        Map<String, String> values = Map.of(
                "name", "Jürgen",
                "symbol", "©"
        );

        String result = engine.render(template, values);

        assertEquals("Hola señor Jürgen, ¿cómo está? Su símbolo es ©", result);
    }
}
