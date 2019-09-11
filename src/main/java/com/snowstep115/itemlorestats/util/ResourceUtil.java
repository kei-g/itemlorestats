package com.snowstep115.itemlorestats.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.lang.ThrowableFunction;

public final class ResourceUtil {
    public static <T> T enumerateResources(String path, ThrowableFunction<BufferedReader, T> function) {
        System.setProperty("file.encoding", "GB2312");
        return IlsMod.execute(() -> {
            ClassLoader loader = IlsMod.class.getClassLoader();
            URL url = loader.getResource(path);
            String[] comps = url.getPath().split(":");
            String jarPath = comps[comps.length - 1].split("!")[0];
            File file = new File(jarPath);
            JarFile jarFile = new JarFile(file);
            try {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name != null && name.startsWith(path)) {
                        InputStream is = loader.getResourceAsStream(name);
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        try {
                            T result = function.apply(br);
                            if (result == null)
                                continue;
                            return result;
                        } finally {
                            br.close();
                        }
                    }
                }
                return null;
            } finally {
                jarFile.close();
            }
        }, () -> null);
    }
}
