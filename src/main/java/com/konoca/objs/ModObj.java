package com.konoca.objs;

import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;

import com.konoca.providers.Provider;
import com.konoca.utils.OSUtils;
import com.konoca.utils.PrismUtils;

public class ModObj
{
    private static Logger logger = Logger.getLogger(ModObj.class.getName());
    private static final String separator = "|";

    public String tomlName;

    public boolean enabled;
    public String filename; // doesnt include .disabled
    public String name;
    public String side;

    public String hash;
    public String hashFormat;
    public String mode;
    public String downloadUrl;

    public String loaders;
    public String mcversions;
    public String releaseType;

    public Provider provider;

    public ModObj()
    {
    }

    public ModObj(Path tomlPath, Path instancePath)
    {
        TomlParseResult toml = OSUtils.getTOML(tomlPath);
        this.tomlName = tomlPath.getFileName().toString();

        this.provider = Provider.getProvider(toml);

        this.filename = toml.getString("filename");
        this.enabled = PrismUtils.isModEnabled(instancePath, filename);

        this.name = toml.getString("name");
        this.side = toml.getString("side");

        this.hash = toml.getString("download.hash");
        this.hashFormat = toml.getString("download.hash-format");
        this.mode = toml.getString("download.mode");
        this.downloadUrl = toml.getString("download.url");

        TomlArray loaders = toml.getArray("x-prismlauncher-loaders");
        TomlArray mcversions = toml.getArray("x-prismlauncher-mc-versions");
        this.loaders = tomlArrayToString(loaders);
        this.mcversions = tomlArrayToString(mcversions);

        this.releaseType = toml.getString("x-prismlauncher-release-type");
    }

    public static ModObj fromString(String objStr)
    {
        ModObj obj = new ModObj();

        String[] str = objStr.split(Pattern.quote(separator));
        logger.info(String.join("   ", str));

        int i = 0;
        obj.enabled = str[i++].equals("Y");
        obj.tomlName = str[i++];
        obj.name = str[i++];
        obj.side = str[i++];
        obj.filename = str[i++];

        obj.hash = str[i++];
        obj.hashFormat = str[i++];
        obj.mode = str[i++];
        obj.downloadUrl = str[i++];

        obj.loaders = str[i++];
        obj.mcversions = str[i++];
        obj.releaseType = str[i++];

        String providerName = str[i++];
        String versionId = str[i++];
        String modId = str[i++];

        obj.provider = Provider.getProvider(providerName);
        obj.provider.setModVersionIDs(modId, versionId);

        return obj;
    }

    public String toString()
    {
        return String.join(
            separator,
            this.getIsEnabled(),
            this.tomlName,
            this.name,
            this.side,
            this.filename,
            this.hash,
            this.hashFormat,
            this.mode,
            this.downloadUrl,
            this.loaders,
            this.mcversions,
            this.releaseType,
            this.provider.getName(),
            this.provider.getVersionID(),
            this.provider.getModID()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModObj)) return false;
        ModObj other = (ModObj) o;
        return this.filename.equals(other.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    private String tomlArrayToString(TomlArray arr)
    {
        if (arr == null) return "";
        return arr.toList().stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
    }

    private String stringToArrayFormat(String str)
    {
        String[] entries = str.split(", ");

        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < entries.length; i++) {
            sb.append("'").append(entries[i].trim()).append("',");
        }

        sb.append("]");
        return sb.toString();
    }

    public String getIsEnabled()
    {
        return this.enabled ? "Y" : "N";
    }

    public String toTOML()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("filename = '" + this.filename + "'\n");
        sb.append("name = '" + this.name + "'\n");
        sb.append("side = '" + this.side + "'\n");
        sb.append("x-prismlauncher-loaders = " + stringToArrayFormat(this.loaders) + "\n");
        sb.append("x-prismlauncher-mc-versions = " + stringToArrayFormat(this.mcversions) + "\n");
        sb.append("x-prismlauncher-release-type = '" + this.releaseType + "'\n\n");

        sb.append("[download]\n");
        sb.append("hash = '" + this.hash + "'\n");
        sb.append("hash-format = '" + this.hashFormat + "'\n");
        sb.append("mode = '" + this.mode + "'\n");
        sb.append("url = '" + this.downloadUrl + "'\n\n");

        sb.append(this.provider.getTOML());

        return sb.toString();
    }

    public void download(Path instancePath)
    {
        this.provider.download(instancePath, this.mode, this.downloadUrl);
    }
}
