package com.konoca.providers;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.tomlj.TomlParseResult;

import com.konoca.objs.URLObj;
import com.konoca.utils.OSUtils;

public class Curseforge extends Provider
{
    private static Logger logger = Logger.getLogger(Curseforge.class.getName());
    public final static String id = "update.curseforge";
    public final static String name = "Curseforge";

    private final static String API = "https://api.curse.tools/v1/cf";

    public Curseforge(TomlParseResult toml)
    {
        super(toml);
        this.modId = this.toml.get(id + ".project-id").toString();
        this.versionId = this.toml.get(id + ".file-id").toString();
    }
    public Curseforge()
    {
    }

    public String getName()
    {
        return Curseforge.name;
    }

    public String getTOML()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[update.curseforge]\n");
        sb.append("file-id = " + this.versionId + "\n");
        sb.append("project-id = " + this.modId);
        return sb.toString();
    }

    public void download(Path instancePath, String mode, String URL)
    {
        String cfURL = API + "/mods/" + this.modId + "/files/" + this.versionId;
        try {
            URI uri = URI.create(cfURL);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.body());

            JSONObject dataObj = (JSONObject) jsonObject.get("data");
            String downloadUrl = (String) dataObj.get("downloadUrl");
            logger.info("Got URL: " + downloadUrl);

            URLObj obj = new URLObj(downloadUrl, ".minecraft/mods", false);
            logger.info("Downloading from " + obj.getURL());
            logger.info("Downloading to " + obj.getAbsPath(instancePath));

            OSUtils.downloadFile(obj, instancePath);
        } catch (Exception e) {
            logger.severe("Error downloading " + cfURL);
            e.printStackTrace();
        }
    }
}
