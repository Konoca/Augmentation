package com.konoca.objs;

import java.util.Objects;
import java.util.regex.Pattern;

public class VersionObj
{
    private static final String separator = "|";

    private String name;
    private String version;

    public VersionObj(String name, String version)
    {
        this.name = name;
        this.version = version;
    }

    public String getName()
    {
        return this.name;
    }

    public String getValue()
    {
        return this.version;
    }

    public String toString()
    {
        return String.join(separator, this.name, this.version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionObj)) return false;
        VersionObj other = (VersionObj) o;
        return this.name.equals(other.name) && this.version.equals(other.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.toString());
    }

    public static VersionObj fromString(String objStr)
    {
        String[] str = objStr.split(Pattern.quote(separator));

        String name = str[0];
        String version = str[1];
        return new VersionObj(name, version);
    }
}
