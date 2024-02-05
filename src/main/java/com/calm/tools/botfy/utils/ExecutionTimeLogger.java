package com.calm.tools.botfy.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExecutionTimeLogger {

    /**
     * 执行指定方法，并记录执行时间。
     *
     * @param runnable 要执行的方法（无返回值，无参数）
     */
    public static void logExecutionTime(Runnable runnable) {
        long startTime = System.nanoTime();

        // 执行方法
        runnable.run();

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        System.out.println("Method took " + TimeUnit.NANOSECONDS.toMillis(executionTime) + " milliseconds to execute.");
    }

    /**
     * 执行指定方法，并记录执行时间。
     *
     * @param callable 要执行的方法（带返回值，无参数）
     * @param <T>      返回值的类型
     * @return 执行结果
     */
    public static <T> T logExecutionTime(Callable<T> callable) throws Exception {
        long startTime = System.nanoTime();

        // 执行方法
        T result = callable.call();

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        System.out.println("Method took " + TimeUnit.NANOSECONDS.toMillis(executionTime) + " milliseconds to execute.");

        return result;
    }

    /**
     * 执行指定方法，并记录执行时间。
     *
     * @param consumer 要执行的方法（无返回值，带参数）
     * @param param    参数
     * @param <T>      参数的类型
     */
    public static <T> void logExecutionTime(Consumer<T> consumer, T param) {
        long startTime = System.nanoTime();

        // 执行方法
        consumer.accept(param);

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        System.out.println("Method took " + TimeUnit.NANOSECONDS.toMillis(executionTime) + " milliseconds to execute.");
    }

    /**
     * 执行指定方法，并记录执行时间。
     *
     * @param function 要执行的方法（带返回值，带参数）
     * @param param    参数
     * @param <T>      参数的类型
     * @param <R>      返回值的类型
     * @return 执行结果
     */
    public static <T, R> R logExecutionTime(Function<T, R> function, T param) {
        long startTime = System.nanoTime();

        // 执行方法
        R result = function.apply(param);

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        System.out.println("Method took " + TimeUnit.NANOSECONDS.toMillis(executionTime) + " milliseconds to execute.");

        return result;
    }

    // 示例方法，可根据实际情况替换为具体的方法
    public static void main(String[] args) throws Exception {
        // 无参数，无返回值的方法
        logExecutionTime(() -> {
            // 执行逻辑
            System.out.println("Executing method without parameters and return value.");
        });

        // 无参数，有返回值的方法
        String result1 = logExecutionTime(() -> {
            // 执行逻辑
            return "Result of method without parameters.";
        });
        System.out.println("Result: " + result1);

        // 有参数，无返回值的方法
        logExecutionTime((param) -> {
            // 执行逻辑
            System.out.println("Executing method with parameter: " + param);
        }, "Parameter");

        // 有参数，有返回值的方法
        String result2 = logExecutionTime((param) -> {
            // 执行逻辑
            return "Result of method with parameter: " + param;
        }, "Parameter");
        System.out.println("Result: " + result2);
    }
}

