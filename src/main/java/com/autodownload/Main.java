package com.autodownload;

import com.autodownload.util.logging.Logger;

public class Main {
  public static void main( String[] args ) {
    // Default values
    int delay = 150;
    Logger.Verbosity verbosity = Logger.Verbosity.LOW;
    // Check for custom values in args
    for(int i = 0; i < args.length-1; i++) {
      if(args[i].equals("-delay") || args[i].equals("-d")) delay = Integer.parseInt(args[i+1]);
      if(args[i].equals("-verbosity") || args[i].equals("-v")) verbosity = Logger.Verbosity.fromInt(Integer.parseInt(args[i+1]));
    }

    App.startApp(delay, verbosity);
  }
}
