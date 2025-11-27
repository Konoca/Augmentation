package com.konoca.utils;

import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import com.konoca.objs.URLObj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class OSUtils
{
    private static Logger logger = Logger.getLogger(OSUtils.class.getName());

    public static Path getPath(String pathString)
    {
        return Paths.get(pathString);
    }

    public static boolean pathExists(String pathString)
    {
        Path path = Paths.get(pathString);
        return pathExists(path);
    }

    public static boolean pathExists(Path path)
    {
        return Files.exists(path);
    }

    public static String getMCdir(String pathString)
    {
        Path path = OSUtils.getPath(pathString);
        return getMCdir(path);
    }

    public static String getMCdir(Path instancePath)
    {
        Path dotMC = instancePath.resolve(".minecraft");
        if (pathExists(dotMC)) return ".minecraft";

        Path MC = instancePath.resolve("minecraft");
        if (pathExists(MC)) return "minecraft";

        logger.severe("No minecraft folder was found");
        return null;
    }
    public static Path getModsPath(Path instancePath)
    {
        String mcDir = getMCdir(instancePath);
        return instancePath.resolve(mcDir, "mods");
    }

    public static JSONObject getJSON(Path path)
    {
        logger.info("Reading JSON: " + path.toString());

        Object obj = null;
        try {
            Reader reader = Files.newBufferedReader(path);
            obj = new JSONParser().parse(reader);
        } catch (Exception e) {
            logger.severe("File was not found");
        }

        JSONObject jo = (JSONObject) obj;
        return jo;
    }

    public static TomlParseResult getTOML(Path path)
    {
        TomlParseResult result = null;
        try {
            result = Toml.parse(path);
        } catch (Exception e) {
            result.errors().forEach(error -> logger.severe(error.toString()));
        }
        return result;
    }

    public static boolean writeFile(String pathStr, String content)
    {
        try (FileWriter writer = new FileWriter(pathStr)) {
            writer.write(content);
            return true;
        } catch (Exception e) {
            logger.severe("Error writing file " + pathStr);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createDirectories(Path path)
    {
        try {
            Files.createDirectories(path);
            return true;
        } catch (Exception e) {
            logger.severe("Error creating directories " + path.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static String readFile(String pathStr)
    {
        try {
            Path path = Paths.get(pathStr);
            return Files.readString(path);
        } catch (Exception e) {
            logger.severe("Error reading file " + pathStr);
            e.printStackTrace();
            return "";
        }
    }

    public static boolean downloadFile(URLObj urlObj, Path instancePath)
    {
        Path absPath = urlObj.getAbsPath(instancePath);

        try {
            URI uri = new URI(urlObj.getURL());
            URL url = uri.toURL();

            String outputName = URLDecoder.decode(Paths.get(url.getPath()).getFileName().toString(), "UTF-8");
            Path outputPath = absPath.resolve(outputName);
            Files.createDirectories(absPath);

            try (InputStream in = url.openStream()) {
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Downloaded: " + outputPath.toString());
            }

            if (urlObj.getIsZip().equals("Y") || outputName.endsWith(".zip"))
            {
                logger.info("Unzipping");
                unzip(outputPath.toString(), absPath.toString());

                logger.info("Deleting zip file");
                Files.delete(outputPath);
            }

            return true;
        } catch (Exception e) {
            logger.severe("Error downloading/writing/unzipping " + absPath.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static void unzip(String zipFilePath, String destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, entry.getName());

                new File(newFile.getParent()).mkdirs();

                if (!entry.isDirectory()) {
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    public static boolean deleteFile(Path path)
    {
        try {
            Files.delete(path);
            return true;
        } catch (Exception e) {
            logger.severe("Error deleting " + path.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean moveFile(Path src, Path dst)
    {
        try {
            Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            logger.severe("Error moving " + src.toString() + " -> " + dst.toString());
            e.printStackTrace();
            return false;
        }
    }
}
