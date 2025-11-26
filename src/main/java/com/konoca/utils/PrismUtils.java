package com.konoca.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.tomlj.TomlParseResult;

import com.konoca.objs.ModObj;

public class PrismUtils
{
    private static Logger logger = Logger.getLogger(PrismUtils.class.getName());
    private static String versionsFile = "mmc-pack.json";

    public static HashMap<String, String> getVersionInfo(Path instancePath)
    {
        logger.info("start");
        HashMap<String, String> map = new HashMap<>();

        Path path = instancePath.resolve(versionsFile);
        logger.info("Using: " + path.toString());

        if (!OSUtils.pathExists(path))
        {
            logger.severe("Version file does not exist");
            return map;
        }

        JSONObject json = OSUtils.getJSON(path);
        JSONArray components = ((JSONArray) json.get("components"));

        Iterator itr = components.iterator();
        while (itr.hasNext())
        {
            JSONObject obj = (JSONObject) itr.next();
            String name = (String) obj.get("cachedName");
            String version = (String) obj.get("version");
            map.put(name, version);
        }

        logger.info("Got: " + map.toString());
        return map;
    }

    public static ArrayList<ModObj> getModInfo(Path instancePath)
    {
        logger.info("start");
        ArrayList<ModObj> tomls = new ArrayList<>();

        Path modsPath = OSUtils.getModsPath(instancePath);
        Path path = modsPath.resolve(".index");
        logger.info("Using: " + path.toString());

        if (!OSUtils.pathExists(path))
        {
            logger.severe("Index files do not exist");
            return tomls;
        }

        List<Path> files;
        try (Stream<Path> paths = Files.walk(path)) {
            files = paths
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Something went wrong navigating index files");
            return tomls;
        }

        files.forEach((file) -> {
            ModObj obj = new ModObj(file, instancePath);
            tomls.add(obj);
        });

        return tomls;
    }

    public static boolean isModEnabled(Path instancePath, String modFileName)
    {
        Path modsPath = OSUtils.getModsPath(instancePath);
        Path path = modsPath.resolve(modFileName);
        return OSUtils.pathExists(path); // mod is enabled if file exists, disabled = modFileName.disabled
    }
}
