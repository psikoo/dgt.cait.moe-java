package com.autodownload.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.autodownload.util.logging.Logger;
import com.autodownload.util.logging.Logger.LogType;

public class Command {
  private static Command instance;

  private Command() {}
  public static Command instance() {
    if(instance == null) {
      instance = new Command();
    }
    return instance;
  }

  public void executeCommand(String[] command) {
    try {
      Process process = Runtime.getRuntime().exec(command);
      logOutput(process.getInputStream(), Logger.Verbosity.HIGH, Logger.LogType.LOG);
      logOutput(process.getErrorStream(), Logger.Verbosity.HIGH, Logger.LogType.ERROR);
      process.waitFor();
    } catch (IOException | InterruptedException logError) { Logger.instance().log("Executing", command[0], Logger.Verbosity.MEDIUM, Logger.LogType.ERROR);}
  }

  private void logOutput(InputStream inputStream, Logger.Verbosity verbosity, LogType logType) {
    new Thread(() -> {
      try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
        while (scanner.hasNextLine()) {
          synchronized (this) { 
            Logger.instance().log("Executed", scanner.nextLine(), verbosity, logType);
          }
        } 
      }
    }).start();
  }
}
