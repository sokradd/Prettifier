package cli;


import text_transform.Color;

public class UsageHelper {
   public static void printUsage() {
       System.out.println(Color.ANSI_PURPLE + "itinerary usage:" + Color.ANSI_RESET);
       System.out.println(Color.ANSI_BLUE + "$ java Prettifier.java" + Color.ANSI_CYAN + " ./input.txt " + Color.ANSI_GREEN +"./output.txt" + Color.ANSI_YELLOW +" ./airport-lookup.csv" + Color.ANSI_RESET);
    }
}
