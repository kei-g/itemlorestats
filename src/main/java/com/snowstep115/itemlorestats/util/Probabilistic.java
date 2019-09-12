package com.snowstep115.itemlorestats.util;

import com.snowstep115.itemlorestats.IlsMod;

public final class Probabilistic {
    private final Lazy<Boolean> occurrence = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.possibility);
    private double possibility;

    public void increase(double possibility) {
        this.possibility += possibility;
    }

    public boolean occurred() {
        return this.occurrence.get();
    }

    @Override
    public String toString() {
        return String.format(this.possibility <= 0 ? "%.2f%%" : "+%.2f%%", this.possibility);
    }
}
