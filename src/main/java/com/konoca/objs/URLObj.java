package com.konoca.objs;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class URLObj
{
    private static final String separator = "|";

    private String url;
    private String pathFromInstance;
    private boolean isZip;

    public URLObj(String URL, String pathFromInstance, boolean isZip)
    {
        this.url = URL;
        this.pathFromInstance = pathFromInstance;
        this.isZip = isZip;
    }

    public String getURL()
    {
        return this.url;
    }
    public void setURL(String URL)
    {
        this.url = URL;
    }

    public String getRelativePath()
    {
        return this.pathFromInstance;
    }
    public void setRelativePath(String path)
    {
        this.pathFromInstance = path;
    }

    public Path getAbsPath(Path instancePath)
    {
        return instancePath.resolve(this.pathFromInstance);
    }

    public String getIsZip()
    {
        return this.isZip ? "Y" : "N";
    }

    public String toString()
    {
        return String.join(
            separator,
            this.url,
            this.pathFromInstance,
            (this.isZip ? "Y" : "N")
        );
    }

    public static URLObj fromString(String objStr)
    {
        String[] str = objStr.split(Pattern.quote(separator));

        String url = str[0];
        String path = str[1];
        boolean isZip = str[2].equals("Y");

        return new URLObj(url, path, isZip);
    }
}
