package com.snowstep115.itemlorestats.event;

import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class WrappedLivingDamageEvent extends LivingDamageEvent {
    public final AtomicBoolean dodged = new AtomicBoolean(false);

    public WrappedLivingDamageEvent(LivingDamageEvent event) {
        super(event.getEntityLiving(), event.getSource(), event.getAmount());
    }
}
