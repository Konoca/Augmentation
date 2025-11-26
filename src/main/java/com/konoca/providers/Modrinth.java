package com.konoca.providers;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.tomlj.TomlParseResult;

import com.konoca.objs.URLObj;
import com.konoca.utils.OSUtils;

public class Modrinth extends Provider
{
    private static Logger logger = Logger.getLogger(Provider.class.getName());
    public final static String id = "update.modrinth";
    public final static String name = "Modrinth";

    public Modrinth(TomlParseResult toml)
    {
        super(toml);
        this.modId = this.toml.getString(id + ".mod-id");
        this.versionId = this.toml.getString(id + ".version");
    }
    public Modrinth()
    {
    }

    public String getName()
    {
        return Modrinth.name;
    }

    public String getTOML()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[update.modrinth]\n");
        sb.append("mod-id = '" + this.modId + "'\n");
        sb.append("version = '" + this.versionId + "'");
        return sb.toString();
    }

    public void download(Path instancePath, String mode, String URL)
    {
        URLObj obj = new URLObj(URL, ".minecraft/mods", false);
        logger.info("Downloading from " + obj.getURL());
        logger.info("Downloading to " + obj.getAbsPath(instancePath));

        OSUtils.downloadFile(obj, instancePath);
    }
}
