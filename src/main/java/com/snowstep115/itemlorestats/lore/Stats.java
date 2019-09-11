package com.snowstep115.itemlorestats.lore;

import com.google.common.util.concurrent.AtomicDouble;

public final class Stats {
    public final AtomicDouble damage = new AtomicDouble(0);
    public final AtomicDouble dodge = new AtomicDouble(0);
    public final AtomicDouble reduction = new AtomicDouble(0);
}
