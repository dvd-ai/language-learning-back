package com.example.languagelearning.util;

import com.example.languagelearning.error.ApplicationException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public final class CompletableFutureUtil {

    public static <T> T tryToExtractSingleCompletedFutureElement(CompletableFuture<T> completableFuture) {
        try {
            return completableFuture.get();
        } catch (ExecutionException e) {
            throw new ApplicationException("Future completed exceptionally: " + e.getCause().getMessage());
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new ApplicationException("Thread was interrupted while waiting for future completion: \n" + e.getCause().getMessage());
        }
    }

    public static <T> List<T> extractValuesFromCompletableFutures(List<CompletableFuture<T>> topicsCompletableFutures) {
        CompletableFuture.allOf(topicsCompletableFutures.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> null)
                .join();

        return topicsCompletableFutures
                .stream()
                .map(CompletableFutureUtil::tryToExtractSingleCompletedFutureElement)
                .toList();
    }
}
