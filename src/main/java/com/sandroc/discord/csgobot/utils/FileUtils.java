package com.sandroc.discord.csgobot.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtils {
    private static Properties guildProperties   = new Properties();
    private static Properties defaultProperties = new Properties();

    public static void changeProperty(String guild, String key, String value) {
        try {
            guildProperties.load(new FileInputStream("configs/" + guild + ".settings"));
            guildProperties.setProperty(key, value);
            guildProperties.store(new FileOutputStream("configs/" + guild + ".settings"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeProperty(String guild, String key, String value) {
        try {
            if (!Files.exists(Paths.get("configs/" + guild + ".settings"))) {
                new File("configs/" + guild + ".settings").createNewFile();
            }

            guildProperties.load(new FileInputStream("configs/" + guild + ".settings"));
            guildProperties.setProperty(key, value);
            guildProperties.store(new FileOutputStream("configs/" + guild + ".settings"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String guild, String key) {
        try {
            if (guild.equalsIgnoreCase("default")) {
                defaultProperties.load(new FileInputStream("configs/default.settings"));
                return defaultProperties.getProperty(key);
            } else {
                guildProperties.load(new FileInputStream("configs/" + guild + ".settings"));
                if (key.equalsIgnoreCase("maps")
                        || key.equalsIgnoreCase("pickedMaps")) {
                    return guildProperties.getProperty(key).replace("[", "").replace("]", "");
                }

                return guildProperties.getProperty(key);
            }
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
