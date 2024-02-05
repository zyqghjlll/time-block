package com.calm.tools.botfy.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AsyncApiCaller {

    public static <T> CompletableFuture<T> asyncCallWithTimeout(
            CompletableFuture<T> asyncTask,
            long timeout,
            TimeUnit timeoutUnit,
            T fallbackResult) {

        CompletableFuture<T> timeoutFuture = new CompletableFuture<>();
        CompletableFuture<T> resultFuture = asyncTask.applyToEither(
                timeoutFuture,
                result -> result
        );

        // 设置超时处理
        CompletableFuture<Void> delayedFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(timeoutUnit.toMillis(timeout));
                timeoutFuture.complete(fallbackResult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 合并超时和结果的 CompletableFuture
        return (CompletableFuture<T>) CompletableFuture.anyOf(resultFuture, timeoutFuture)
                .thenCompose(result -> {
                    delayedFuture.cancel(true);
                    return CompletableFuture.completedFuture(result);
                });
    }

    // 示例方法，模拟异步调用第三方接口
    public static CompletableFuture<String> asyncCallThirdPartyApi() {
        return CompletableFuture.supplyAsync(() -> {
            // 模拟调用第三方接口的耗时操作
            try {
                Thread.sleep(6000); // 模拟一个超时的情况
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 返回调用结果
            return "Third-party API response";
        });
    }

    public static void main(String[] args) {
        CompletableFuture<String> resultFuture = asyncCallWithTimeout(
                asyncCallThirdPartyApi(),
                5000,
                TimeUnit.MILLISECONDS,
                "Fallback result"
        );

        System.out.println("Other logic...");

        try {
            String result = resultFuture.get();
            System.out.println("Final result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
