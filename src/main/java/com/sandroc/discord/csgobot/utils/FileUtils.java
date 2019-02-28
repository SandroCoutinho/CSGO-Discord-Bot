package com.sandroc.discord.csgobot.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtils {
    private static Properties properties = new Properties();

    public static void changeProperty(String guild, String key, String value) {
        try {
            properties.load(new FileInputStream("configs/" + guild + ".settings"));
            properties.setProperty(key, value);
            properties.store(new FileOutputStream("configs/" + guild + ".settings"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeProperty(String guild, String key, String value) {
        try {
            if (!Files.exists(Paths.get("configs/" + guild + ".settings"))) {
                new File("configs/" + guild + ".settings").createNewFile();
            }

            properties.load(new FileInputStream("configs/" + guild + ".settings"));
            properties.setProperty(key, value);
            properties.store(new FileOutputStream("configs/" + guild + ".settings"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String guild, String key) {
        try {
            properties.load(new FileInputStream("configs/" + guild + ".settings"));
            if (key.equalsIgnoreCase("maps")
                    || key.equalsIgnoreCase("pickedMaps")) {
                return properties.getProperty(key).replace("[", "").replace("]", "");
            }

            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "None";
    }

    public static void writeDefaultConfigs() {
        try {
            FileWriter out = new FileWriter(new File("configs/default.settings"));

            for (DefaultSetting defaultSetting : DefaultSetting.values()) {
                out.write(defaultSetting.getKey() + "=" + defaultSetting.getValue() + "\n");
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean configFileExists(String guild) {
        return Files.exists(Paths.get("configs/" + guild + ".settings"));
    }
}
