package com.example.messenger.service;

import com.example.messenger.exception.MissingPlaceholderValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TemplateEngineTest {
    TemplateEngine templateEngine = new TemplateEngine();

    @Test
    void shouldReplacePlaceholderWithValue() {
        String template = "Hello, #{name}!";
        Map<String, String> values = Map.of("name", "Yurii");

        String result = templateEngine.render(template, values);

        assertEquals("Hello, Yurii!", result);
    }

    @Test
    void shouldThrowExceptionIfPlaceholderValueIsMissing() {
        String template = "Hello, #{name}!";

        Map<String, String> values = Map.of();

        Exception exception = assertThrows(MissingPlaceholderValueException.class, () ->
                templateEngine.render(template, values));

        assertEquals("name", exception.getMessage());
    }

    @Test
    void shouldIgnoreExtraVariablesNotInTemplate() {
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
        String template = "Some text: #{value}!";
        Map<String, String> values = Map.of("value", "#{tag}");

        String result = templateEngine.render(template, values);

        assertEquals("Some text: #{tag}!", result);
    }

    @Test
    void shouldSupportLatin1CharactersInTemplateAndValues() {
        String template = "Hola señor #{name}, ¿cómo está? Su símbolo es #{symbol}";
        Map<String, String> values = Map.of(
                "name", "Jürgen",
                "symbol", "©"
        );

        String result = templateEngine.render(template, values);

        assertEquals("Hola señor Jürgen, ¿cómo está? Su símbolo es ©", result);
    }

    @ParameterizedTest
    @CsvSource({
            "'Hello, #{name}!', name=Yana, 'Hello, Yana!'",
            "'Value: #{val}', val=123, 'Value: 123'",
            "'Multiple: #{a} and #{b}', a=foo;b=bar, 'Multiple: foo and bar'",
            "'No placeholders', '', 'No placeholders'"
    })
    void shouldRenderParameterizedInputs(String template, String vars, String expected) {
        Map<String, String> values = vars.isEmpty() ? Map.of() :
                Arrays.stream(vars.split(";"))
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(a -> a[0], a -> a[1]));

        String result = templateEngine.render(template, values);
        assertEquals(expected, result);
    }

    @Test
    void shouldMockRenderMethodPartially() {
        TemplateEngine spyEngine = spy(templateEngine);

        String template = "Hello, #{name}!";
        Map<String, String> values = new HashMap<>();
        values.put("name", "Yurii");

        doReturn("Hi, Yurii!").when(spyEngine).render(template, values);

        String result = spyEngine.render(template, values);
        assertEquals("Hi, Yurii!", result);
        verify(spyEngine, times(1)).render(template, values);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldRunOnlyOnWindows() {
        String template = "Windows: #{value}!";
        Map<String, String> values = Map.of("value", "#{tag}");

        String result = templateEngine.render(template, values);

        assertEquals("Windows: #{tag}!", result);
    }
}
