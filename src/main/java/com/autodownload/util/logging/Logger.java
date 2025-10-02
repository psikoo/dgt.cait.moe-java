package com.autodownload.util.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.autodownload.App;

public class Logger {
  private static Logger instance;

  private Logger() {}
  public static Logger instance() {
    if(instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  public void log(String action, String message) {
    String time = new SimpleDateFormat("[dd/MM/yyyy, HH:mm:ss:SSS]").format(new Date());
    Verbosity verbosity = Verbosity.LOW;
    LogType logType = LogType.LOG;
    fullLog(time, message, verbosity, logType, action);
  } public void log(String action, String message, Verbosity verbosity) {
    String time = new SimpleDateFormat("[dd/MM/yyyy, HH:mm:ss:SSS]").format(new Date());
    LogType logType = LogType.LOG;
    fullLog(time, message, verbosity, logType, action);
  } public void log(String action, String message, Verbosity verbosity, LogType logType) {
    String time = new SimpleDateFormat("[dd/MM/yyyy, HH:mm:ss:SSS]").format(new Date());
    fullLog(time, message, verbosity, logType, action);
  }
  
  private void fullLog(String time, String message, Verbosity verbosity, LogType logType, String action) {
    if(App.getVerbosity().level >= verbosity.level) {
      Ansi bg = Ansi.BgBlack;
      String prefix =
        Ansi.colorize(Ansi.AnsiBuilder(new Ansi[] {bg, Ansi.Green}), time+" ")+
        Ansi.colorize(Ansi.AnsiBuilder(new Ansi[] {bg, logType.color, Ansi.Bold}), logType.logType+" ")+
        Ansi.colorize(Ansi.AnsiBuilder(new Ansi[] {bg, Ansi.Yellow}), verbosity.levelStr+" ")+
        Ansi.colorize(Ansi.AnsiBuilder(new Ansi[] {bg, Ansi.Green, Ansi.Bold}), actionFormatter(action)+" > ");
      System.out.println(prefix+Ansi.colorize(bg, message));
    }
  }

  private String actionFormatter(String action) {
    int padding = 10-action.length();
    if(padding<0) padding = 0;
    return action+" ".repeat(padding);
  }

  public static enum LogType {
    LOG  ("[LOG]  ", Ansi.Green),
    ERROR("[ERROR]", Ansi.Red);
  
    public final String logType;
    public final Ansi color;
    private LogType(String logType, Ansi color) {
      this.logType = logType;
      this.color = color;
    }
  }

  // LOW will always show up.
  // MEDIUM and HIGH will only show up if 
  // App.verbosity is their level or higher
  public static enum Verbosity {
    LOW   (1, "[LOW]   "),
    MEDIUM(2, "[MEDIUM]"),
    HIGH  (3, "[HIGH]  ");

    public final int level;
    public final String levelStr;
    private Verbosity(int level, String levelStr) {
      this.level = level;
      this.levelStr = levelStr;
    }
    public static Verbosity fromInt(int number) {
      if(number == 1) return LOW;
      if(number == 2) return MEDIUM;
      if(number == 3) return HIGH;
      else return LOW;
    }
  }
}
