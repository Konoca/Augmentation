package com.konoca;

import java.io.File;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.konoca.objs.URLObj;
import com.konoca.utils.OSUtils;

public class Updater {
    private static Logger logger = Logger.getLogger(Updater.class.getName());
    private static final String API = "https://api.github.com/repos/Konoca/Augmentation";

    private static JSONObject data = Updater.getLatestRelease();
    public static final String currentVersion = "v" + Constants.VERSION;

    private static JSONObject getLatestRelease() {
        try {
            URI uri = URI.create(Updater.API + "/releases/latest");
            HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(new CookieManager())
                .build();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "Augmentation")
                .header("Accept", "application/json")
                .GET()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (Constants.DEBUG) System.out.println("RESPONSE: " + response.body());

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.body());
            return jsonObject;
        } catch (Exception e) {
            logger.severe("Error fetching latest release data");
            e.printStackTrace();
        }

        return null;
    }

    public static String getLatestVersion() {
        try {
            String version = (String) Updater.data.get("tag_name");
            logger.info("Got Version: " + version);
            return version;
        } catch (Exception e) {
            logger.severe("Error fetching latest version");
            e.printStackTrace();
        }

        return Updater.currentVersion;
    }

    public static boolean canUpdate() {
        return !getLatestVersion().equals(currentVersion);
    }

    public static String downloadLatest() {
        try {
            JSONArray assets = (JSONArray) Updater.data.get("assets");
            JSONObject asset = (JSONObject) assets.get(0);

            String downloadUrl = (String) asset.get("browser_download_url");
            URLObj urlobj = new URLObj(downloadUrl, "", false);

            CodeSource codeSource = App.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();
            logger.info("Jar path: " + jarDir);

            Path jarPath = OSUtils.getPath(jarDir);
            OSUtils.downloadFile(urlobj, jarPath);
        } catch (Exception e) {
            logger.severe("Error downloading latest version of Augmentation!");
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }
}
