package com.sandroc.discord.csgobot.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    private static Properties guildProperties   = new Properties();
    private static Properties defaultProperties = new Properties();
    private static Properties cacheProperties   = new Properties();

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
            if (!Files.exists(Paths.get("configs/" + guild + ".settings"))) {
                new File("configs/" + guild + ".settings").createNewFile();
            }

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
            File directory = new File("configs");
            directory.mkdir();

            File file = new File("configs/default.settings");
            file.createNewFile();
            FileWriter out = new FileWriter(file);

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

    public static boolean cacheFileExists() {
        return Files.exists(Paths.get("cache/version.setting"));
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static String currentVersion() {
        try {
            URL           urlObject     = new URL("https://www.dropbox.com/s/3g6858699jhq8rz/version.txt?dl=1");
            URLConnection urlConnection = urlObject.openConnection();
            InputStream   inputStream   = urlConnection.getInputStream();

            return readFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "FAILED";
    }

    public static void newVersion() {
        try {
            if (!cacheFileExists()) {
                downloadCache();
            }

            cacheProperties.load(new FileInputStream("cache/version.setting"));
            if (Double.parseDouble(currentVersion()) > Double.parseDouble(cacheProperties.getProperty("version"))) {
                downloadCache();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void downloadCache() {
        System.out.println("Downloading cache");

        try {
            URL              url  = new URL("https://www.dropbox.com/s/smxdf6yj1u7drul/cache.zip?dl=1");
            URLConnection    conn = url.openConnection();
            InputStream      in   = conn.getInputStream();
            FileOutputStream out  = new FileOutputStream("cache.zip");
            byte[]           b    = new byte[1024];
            int              count;
            while ((count = in.read(b)) >= 0) {
                out.write(b, 0, count);
            }

            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        extractCache();
    }

    private static void extractCache() {
        byte[] buffer = new byte[1024];

        try {
            File folder = new File("cache/");
            if (!folder.exists()) {
                folder.mkdir();
            }

            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream("cache.zip"));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File   newFile  = new File("cache/" + File.separator + fileName);

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            FileWriter out = new FileWriter(new File("cache/version.setting"));
            out.write("version=" + currentVersion() + "\n");
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
