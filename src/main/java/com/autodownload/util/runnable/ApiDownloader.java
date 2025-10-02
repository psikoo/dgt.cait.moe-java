package com.autodownload.util.runnable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;

import com.autodownload.App;
import com.autodownload.util.Get;
import com.autodownload.util.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;

public class ApiDownloader implements Runnable {
  private static final HashMap<Integer, String> WATCHLIST = new HashMap<>();
  private String uriString = "";

  public ApiDownloader(String uriString) {
    this.uriString = uriString;
  }

  @Override
  public void run() {
    WATCHLIST.clear();
    try {
      // Download full camera list
      JsonNode fullJson = Get.getJsonFromURL(uriString);
      // Loop json looking for items to add to the watchlist
      Iterator<JsonNode> jsonIterator = fullJson.elements();
      while(jsonIterator.hasNext()) {
        JsonNode next = jsonIterator.next();
        if(next.get("watch").asText().equals("true")) {
          Integer name = next.get("name").asInt();
          String url = next.get("url").asText();
          WATCHLIST.put(name, url);
        }
      }
    } catch (URISyntaxException | IOException logError) { Logger.instance().log("Getting", "Cameras", Logger.Verbosity.MEDIUM, Logger.LogType.ERROR); logError.printStackTrace(); }
    App.setWatchList(WATCHLIST);
    App.updateCameras();
    Logger.instance().log("Updated", "Cameras", Logger.Verbosity.HIGH);
  }

  public static void setup(String uriString) {
    try {
      // Download full camera list
      JsonNode fullJson = Get.getJsonFromURL(uriString);
      // Loop json looking for items to add to the watchlist
      Iterator<JsonNode> jsonIterator = fullJson.elements();
      while(jsonIterator.hasNext()) {
        JsonNode next = jsonIterator.next();
        if(next.get("watch").asText().equals("true")) {
          Integer name = next.get("name").asInt();
          String url = next.get("url").asText();
          WATCHLIST.put(name, url);
        }
      }
    } catch (URISyntaxException | IOException logError) { Logger.instance().log("Getting", "Cameras", Logger.Verbosity.MEDIUM, Logger.LogType.ERROR); }
    App.setWatchList(WATCHLIST);
    App.updateCameras();
    Logger.instance().log("Updated", "Cameras", Logger.Verbosity.HIGH);
  }
}
