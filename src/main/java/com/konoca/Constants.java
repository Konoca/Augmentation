package com.konoca;

import com.konoca.utils.PropUtils;

public class Constants
{
    public static String VERSION = PropUtils.getProperty("app.version");
    public static boolean DEBUG = false;

    public static final String WindowTitle = "Augmentation";
    public static final int WindowWidth = 1000;
    public static final int WindowHeight = 500;
}
