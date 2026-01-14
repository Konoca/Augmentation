package com.konoca.utils;

import java.nio.file.Path;

public class AugUtils
{
    public final static String fileExt = ".aug";
    public final static String fileVersion = "2";

    public static String getNewFilename(Path instancePath)
    {
        String instanceName = instancePath.getFileName().toString();
        return instanceName + fileExt;
    }
}
