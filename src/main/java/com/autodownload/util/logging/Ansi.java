package com.autodownload.util.logging;

public class Ansi {
  private static final String	RESET               = "\033[0m";
  private static final String	SANE                = "0";
	private static final String	HIGH_INTENSITY      = "1";
	private static final String	LOW_INTENSITY       = "2";

	private static final String	ITALIC				      = "3";
	private static final String	UNDERLINE			      = "4";
	private static final String	BLINK				        = "5";
	private static final String	RAPID_BLINK			    = "6";

	private static final String	BLACK				        = "30";
	private static final String	RED					        = "31";
	private static final String	GREEN				        = "32";
	private static final String	YELLOW				      = "33";
	private static final String	BLUE				        = "34";
	private static final String	MAGENTA				      = "35";
	private static final String	CYAN		    		    = "36";
	private static final String	WHITE			    	    = "37";

	private static final String	BACKGROUND_BLACK	  = "40";
	private static final String	BACKGROUND_RED		  = "41";
	private static final String	BACKGROUND_GREEN	  = "42";
	private static final String	BACKGROUND_YELLOW	  = "43";
	private static final String	BACKGROUND_BLUE		  = "44";
	private static final String	BACKGROUND_MAGENTA  = "45";
	private static final String	BACKGROUND_CYAN		  = "46";
	private static final String	BACKGROUND_WHITE	  = "47";

	public static final Ansi Bold = new Ansi(HIGH_INTENSITY);
	public static final Ansi Normal = new Ansi(LOW_INTENSITY);
	
	public static final Ansi Italic = new Ansi(ITALIC);
	public static final Ansi Underline = new Ansi(UNDERLINE);
	public static final Ansi Blink = new Ansi(BLINK);
	public static final Ansi RapidBlink = new Ansi(RAPID_BLINK);
	
	public static final Ansi Black = new Ansi(BLACK);
	public static final Ansi Red = new Ansi(RED);
	public static final Ansi Green = new Ansi(GREEN);
	public static final Ansi Yellow = new Ansi(YELLOW);
	public static final Ansi Blue = new Ansi(BLUE);
	public static final Ansi Magenta = new Ansi(MAGENTA);
	public static final Ansi Cyan = new Ansi(CYAN);
	public static final Ansi White = new Ansi(WHITE);
	
	public static final Ansi BgBlack = new Ansi(BACKGROUND_BLACK);
	public static final Ansi BgRed = new Ansi(BACKGROUND_RED);
	public static final Ansi BgGreen = new Ansi(BACKGROUND_GREEN);
	public static final Ansi BgYellow = new Ansi(BACKGROUND_YELLOW);
	public static final Ansi BgBlue = new Ansi(BACKGROUND_BLUE);
	public static final Ansi BgMagenta = new Ansi(BACKGROUND_MAGENTA);
	public static final Ansi BgCyan = new Ansi(BACKGROUND_CYAN);
	public static final Ansi BgWhite = new Ansi(BACKGROUND_WHITE);

  private String codesFull = "";
  private String codeShort = "";
  public Ansi(String codeShort) {
    this.codeShort = codeShort;
    this.codesFull += "\033["+codeShort+"m";
  } 
  public static String AnsiBuilder(Ansi[] codes) {
    String code = "\033[";
    for(int i = 0; i < codes.length; i++) {
      // Don't add ; to the last value
      if(i==codes.length-1) code += codes[i].codeShort+"m"; 
      // Separate codes with ;
      else code += codes[i].codeShort+";";
    }
    return code;
  }

  public static String colorize(Ansi ansi, String original) {
    return ansi.codesFull+original+RESET;
  } public static String colorize(String ansi, String original) {
    return ansi+original+RESET;
  }
}
