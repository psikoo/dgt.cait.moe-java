package com.autodownload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.autodownload.util.logging.Logger;
import com.autodownload.util.runnable.ApiDownloader;
import com.autodownload.util.runnable.PhotoDownloader;

public class App {
  private static final int API_DELAY = 60;
  private static final int DELAY_RAND = 10;
  private static final Random RAND = new Random();
  private static final HashMap<Integer, ScheduledExecutorService> CAMERAS = new HashMap<>();
  private static int delay;
  private static Logger.Verbosity verbosity;
  private static String uriString;
  private static HashMap<Integer, String> watchList;
  
  public static Logger.Verbosity getVerbosity() { return verbosity; }
  public static void setWatchList(HashMap<Integer, String> watchList) { App.watchList = watchList; }

  public static void startApp(int delay, Logger.Verbosity verbosity) {
    App.delay = delay;
    App.verbosity = verbosity;
    // Check for env
    var props = new Properties();
    try (var inputStream = Files.newInputStream(Paths.get(".env"))) { props.load(inputStream); } 
    catch (IOException ignore) { Logger.instance().log("Opening", ".env", Logger.Verbosity.LOW, Logger.LogType.ERROR); }
    App.uriString = (String) props.get("API")+"/cameras/";
    // Setup ApiDownloader > It automatically updates CAMERAS
    startApiExecutor(uriString);
    ApiDownloader.setup(uriString);
  }

  // Ensures that the cameras in the watchList are running and
  // listed in CAMERAS so they can be stopped if needed
  public static void updateCameras() {
    try {
      // Remove cameras that are not longer in the watchList
      for (Integer camera : CAMERAS.keySet()) {
        if(watchList.get(camera) == null) stopDownloaderExecutor(CAMERAS.get(camera), camera);
      }
      // Start downloader for all cameras in watchList
      for (Integer camera : watchList.keySet()) {
        if(CAMERAS.get(camera) == null) startDownloaderExecutor(watchList.get(camera), camera);
      }
    } catch(ConcurrentModificationException ignore) {}
  }

  // Fetches the API
  private static void startApiExecutor(String uriString) {
    ScheduledExecutorService apiExecutor = Executors.newScheduledThreadPool(1);
    Logger.instance().log("Started", "URL: \""+uriString+"\" delay: "+App.delay+"s");
    apiExecutor.scheduleAtFixedRate(new ApiDownloader(uriString), 1, API_DELAY, TimeUnit.SECONDS);
  }

  // Download images from a given camera
  // Random delay to avoid rate limiting
  private static void startDownloaderExecutor(String uriString, int cameraId) {
    int randomDelay = RAND.nextInt(DELAY_RAND);
    int customDelay = (randomDelay%2 == 0) ? App.delay+randomDelay :  App.delay-randomDelay;
    ScheduledExecutorService downloader = Executors.newScheduledThreadPool(1);
    downloader.scheduleAtFixedRate(new PhotoDownloader(uriString, cameraId), 1, customDelay, TimeUnit.SECONDS);
    Logger.instance().log("Started", "Downloader for cameraId: "+cameraId);
    CAMERAS.put(cameraId, downloader);
  }

  // Stops a downloader and updates CAMERAS
  private static void stopDownloaderExecutor(ScheduledExecutorService downloader, int cameraId) {
    downloader.shutdown();
    Logger.instance().log("Stopped", "Downloader for cameraId: "+cameraId);
    CAMERAS.remove(cameraId);
    updateCameras();
  }
}
