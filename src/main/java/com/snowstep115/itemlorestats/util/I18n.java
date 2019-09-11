package com.snowstep115.itemlorestats.util;

import java.util.LinkedList;

public final class I18n {
    public static String format(String format, Object... args) {
        LinkedList<String> list = new LinkedList<>();
        while (true) {
            int index = format.indexOf('§');
            if (index < 0)
                break;
            list.add(format.substring(index, index + 2));
            format = format.substring(0, index) + format.substring(index + 2);
        }
        String result = net.minecraft.client.resources.I18n.format(format, args);
        return list.get(0) + result + list.get(1);
    }
}
