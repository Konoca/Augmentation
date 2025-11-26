package com.konoca.objs;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.konoca.utils.AugUtils;

public class Augment
{
    private static Logger logger = Logger.getLogger(Augment.class.getName());

    public ArrayList<URLObj> urls;
    public ArrayList<ModObj> mods;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("VERSION=" + AugUtils.fileVersion + "\n");

        sb.append("--URL\n");
        this.urls.forEach((URLObj obj) -> {
            sb.append(obj.toString() + "\n");
        });
        sb.append("--MOD\n");
        this.mods.forEach((ModObj obj) -> {
            sb.append(obj.toString() + "\n");
        });

        return sb.toString();
    }

    public static Augment fromString(String augStr)
    {
        Augment aug = new Augment();
        aug.urls = new ArrayList<>();
        aug.mods = new ArrayList<>();

        String[] str = augStr.split("\n");

        if (!str[0].equals("VERSION=" + AugUtils.fileVersion))
        {
            logger.severe("Bad version");
            return null;
        }

        String currentMode = "";
        for (String line : str) {
            logger.info(line);

            if (line.equals("--URL"))
            {
                currentMode = "URL";
                continue;
            }
            if (line.equals("--MOD"))
            {
                currentMode = "MOD";
                continue;
            }

            if (currentMode == null) continue;
            if (line.startsWith("VERSION=")) continue;
            if (line.equals("\n") || line.equals("")) continue;

            if (currentMode.equals("URL"))
            {
                URLObj obj = URLObj.fromString(line);
                aug.urls.add(obj);
                continue;
            }
            if (currentMode.equals("MOD"))
            {
                ModObj obj = ModObj.fromString(line);
                aug.mods.add(obj);
                continue;
            }
        }

        return aug;
    }
}
