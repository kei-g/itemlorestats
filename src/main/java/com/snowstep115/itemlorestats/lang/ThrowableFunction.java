package com.snowstep115.itemlorestats.lang;

public interface ThrowableFunction<T, R> {
    R apply(T arg) throws Throwable;
}
