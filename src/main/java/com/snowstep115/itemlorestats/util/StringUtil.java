package com.snowstep115.itemlorestats.util;

public final class StringUtil {
    public static String[] decompose(String value) {
        int index = value.lastIndexOf(' ');
        String[] comps = new String[2];
        comps[0] = index < 0 ? value : value.substring(0, index);
        comps[1] = index < 0 ? "" : value.substring(index + 1);
        return comps;
    }

    public static void undecorate(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String c = args[i];
            while (true) {
                int index = c.indexOf('§');
                if (index < 0)
                    break;
                c = c.substring(0, index) + c.substring(index + 2);
            }
            args[i] = c;
        }
    }
}
