package com.autodownload.util.runnable;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Properties;

import com.autodownload.util.Command;
import com.autodownload.util.Get;
import com.autodownload.util.MD5;
import com.autodownload.util.logging.Logger;

public class PhotoDownloader implements Runnable {
  private String uriString = "";
  private int cameraId = 0;

  public PhotoDownloader(String uriString, int cameraId) {
    this.uriString = uriString;
    this.cameraId = cameraId;
  }

  @Override
  public void run() {
    // Get api key
    var props = new Properties();
    try (var inputStream = Files.newInputStream(Paths.get(".env"))) { props.load(inputStream); } 
    catch (IOException logError) { Logger.instance().log("Opening", ".env", Logger.Verbosity.LOW, Logger.LogType.ERROR); }
    String apiKey = (String) props.get("API_KEY");
    String api = (String) props.get("API");
    // Download and check for duplicate images
    try {
      String[] command = {"./request/post.sh","./images/"+cameraId+".jpg", String.valueOf(cameraId), String.valueOf(api), String.valueOf(apiKey)};
      String tmp = System.getProperty("user.dir")+"/images/"+cameraId+"tmp.jpg";
      String original = System.getProperty("user.dir")+"/images/"+cameraId+".jpg";
      // Download image into tmp (the unix time is added to invalidate caches)      
      Long unix = new Date().getTime()/1000;
      Get.getImgFromURL(uriString+"?t="+unix, tmp);
      // Compare MD5 hashes to check if the images are different
      if((!MD5.getMD5(original).equals(MD5.getMD5(tmp)) && !MD5.getMD5(tmp).equals("error")) || MD5.getMD5(original).equals("error")) {
        // Copy and replace tmp to original
        Files.copy((new FileInputStream(tmp)), Paths.get(original), StandardCopyOption.REPLACE_EXISTING);
        Logger.instance().log("Downloaded", "Image for "+cameraId);
        // Post image to Imgur and image info to the api
        Command.instance().executeCommand(command);
      } 
    } catch (IOException | URISyntaxException logError) { Logger.instance().log("Getting", "Image "+cameraId, Logger.Verbosity.MEDIUM, Logger.LogType.ERROR); }
  }
}
