package com.example.messenger.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class TimingToFileExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final String START_TIME = "start time";
    private static final String LOG_FILE = "test-execution.log";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        long start = System.currentTimeMillis();
        context.getStore(ExtensionContext.Namespace.create(context.getUniqueId()))
                .put(START_TIME, start);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        ExtensionContext.Store store =
                context.getStore(ExtensionContext.Namespace.create(context.getUniqueId()));
        Long start = store.remove(START_TIME, Long.class);

        if (start == null) {
            return;
        }

        long duration = System.currentTimeMillis() - start;

        String testName = context.getDisplayName();
        String className = context.getRequiredTestClass().getSimpleName();
        String timestamp = LocalDateTime.now().toString();

        String logMessage = String.format("[%s] %s.%s took %d ms%n", timestamp, className, testName, duration);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logMessage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write test log", e);
        }
    }
}
