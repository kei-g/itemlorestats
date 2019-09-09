package com.snowstep115.itemlorestats.lang;

public interface ThrowableSupplier<T> {
    T get() throws Throwable;
}
