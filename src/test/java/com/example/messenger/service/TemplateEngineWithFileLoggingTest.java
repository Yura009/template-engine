package com.example.messenger.service;

import com.example.messenger.extension.TimingToFileExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TimingToFileExtension.class)
class TemplateEngineWithFileLoggingTest {

    @Test
    void shouldRenderSimpleTemplate() {
        TemplateEngine templateEngine = new TemplateEngine();
        String result = templateEngine.render("Hello, #{name}!", Map.of("name", "Yana"));
        assertEquals("Hello, Yana!", result);
    }
}
