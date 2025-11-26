package com.konoca.providers;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.tomlj.TomlParseResult;

public class Provider
{
    private static Logger logger = Logger.getLogger(Provider.class.getName());
    public final static String id = "";

    protected TomlParseResult toml;
    protected String modId;
    protected String versionId;

    public Provider(TomlParseResult toml)
    {
        this.toml = toml;
    }
    public Provider()
    {
    }

    public static Provider getProvider(TomlParseResult toml)
    {
        if (toml.contains(Modrinth.id))
            return new Modrinth(toml);

        if (toml.contains(Curseforge.id))
            return new Curseforge(toml);

        return new Provider(toml);
    }

    public static Provider getProvider(String providerName)
    {
        if (providerName.equals(Modrinth.name))
            return new Modrinth();

        if (providerName.equals(Curseforge.name))
            return new Curseforge();

        return new Provider();
    }

    public String getName()
    {
        return "";
    }

    public String getVersionID()
    {
        return this.versionId;
    }

    public String getModID()
    {
        return this.modId;
    }

    public void setModVersionIDs(String modId, String versionId)
    {
        this.modId = modId;
        this.versionId = versionId;
    }

    public String getTOML()
    {
        return "";
    }

    public void download(Path instancePath, String mode, String URL)
    {
    }
}
